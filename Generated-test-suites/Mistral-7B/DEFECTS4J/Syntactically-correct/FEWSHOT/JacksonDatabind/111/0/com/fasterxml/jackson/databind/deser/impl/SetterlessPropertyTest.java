package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

@JsonDeserialize(using = SetterlessProperty.class)
public static class TestBean {
    private List<String> list;
    private Map<String, Integer> map;

    @JsonProperty("list")
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @JsonProperty("map")
    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}

public class SetterlessPropertyTest extends TestBase {

    @Test
    public void testSetterlessProperty() throws IOException, JsonProcessingException {
        JavaType listType = mapper().constructType(List.class, String.class);
        JavaType mapType = mapper().constructType(Map.class, String.class, Integer.class);

        TestBean bean = new TestBean();

        ObjectNode node = mapper().createObjectNode();
        node.put("list", "[\"one\",\"two\"]");
        node.put("map", "{\"one\":1,\"two\":2}");

        BeanDescription beanDesc = mapper().findBeanDescription(TestBean.class, null);

        DeserializationContext ctxt = context();
        JsonParser p = createParser(node);

        SetterlessProperty property = (SetterlessProperty) beanDesc.findProperty("list", List.class);
        property.deserializeAndSet(p, ctxt, bean);
        assertArrayEquals(Arrays.asList("one", "two"), bean.list);

        property = (SetterlessProperty) beanDesc.findProperty("map", Map.class);
        Object mapObject = property.deserializeAndSet(p, ctxt, bean);
        Map<String, Integer> map = (Map<String, Integer>) mapObject;
        assertArrayEquals(new String[]{"one", "two"}, map.keySet().toArray(new String[0]));
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        
        bean = new TestBean();
        property = (SetterlessProperty) beanDesc.findProperty("list", List.class);
        property.deserializeAndSet(p, ctxt, bean);
        assertNull(bean.list);
        
        property = (SetterlessProperty) beanDesc.findProperty("map", Map.class);
        property.deserializeAndSet(p, ctxt, bean);
        assertNull(bean.map);
    }

    @Test
    public void testSetterlessPropertyWithAnnotations() throws IOException, JsonProcessingException {
        JavaType listType = mapper().constructType(List.class, String.class);
        JavaType mapType = mapper().constructType(Map.class, String.class, Integer.class);

        TestBean bean = new TestBean();

        ObjectNode node = mapper().createObjectNode();
        node.put("list", "[\"one\",\"two\"]");
        node.put("map", "{\"one\":1,\"two\":2}");

        BeanDescription beanDesc = mapper().findBeanDescription(TestBean.class, null);

        DeserializationContext ctxt = context();
        JsonParser p = createParser(node);

        SetterlessProperty propertyList = (SetterlessProperty) beanDesc.findProperty("list", List.class);
        JsonDeserializer<List<String>> valueDeserializerList = mock(JsonDeserializer.class);
        when(valueDeserializerList.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenReturn(Arrays.asList("one", "two"));
        propertyList.setValueDeserializer(valueDeserializerList);

        SetterlessProperty propertyMap = (SetterlessProperty) beanDesc.findProperty("map", Map.class);
        JsonDeserializer<Map<String, Integer>> valueDeserializerMap = mock(JsonDeserializer.class);
        when(valueDeserializerMap.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenReturn(new HashMap<>());
        propertyMap.setValueDeserializer(valueDeserializerMap);

        propertyList.deserializeAndSet(p, ctxt, bean);
        assertSame(bean.list, Arrays.asList("one", "two"));

        propertyMap.deserializeAndSet(p, ctxt, bean);
        Map<String, Integer> map = (Map<String, Integer>) propertyMap.deserializeAndSet(p, ctxt, bean);
        assertArrayEquals(new String[]{"one", "two"}, map.keySet().toArray(new String[0]));
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));

        bean = new TestBean();

        propertyList.deserializeAndSet(p, ctxt, bean);
        assertNull(bean.list);

        propertyMap.deserializeAndSet(p, ctxt, bean);
        assertNull(bean.map);
    }

    @Test
    public void testNullValues() throws IOException, JsonProcessingException {
        JavaType listType = mapper().constructType(List.class, String.class);
        JavaType mapType = mapper().constructType(Map.class, String.class, Integer.class);

        TestBean bean = new TestBean();

        ObjectNode node = mapper().createObjectNode();
        node.put("list", null);
        node.put("map", "{\"one\":null,\"two\":null}");

        BeanDescription beanDesc = mapper().findBeanDescription(TestBean.class, null);

        DeserializationContext ctxt = context();
        JsonParser p = createParser(node);

        SetterlessProperty propertyList = (SetterlessProperty) beanDesc.findProperty("list", List.class);
        propertyList.deserializeAndSet(p, ctxt, bean);
        assertNull(bean.list);

        SetterlessProperty propertyMap = (SetterlessProperty) beanDesc.findProperty("map", Map.class);
        propertyMap.deserializeAndSet(p, ctxt, bean);
        Map<String, Object> map = (Map<String, Object>) propertyMap.deserializeAndSet(p, ctxt, bean);
        assertArrayEquals(new String[]{"one", "two"}, map.keySet().toArray(new String[0]));
        assertNull(map.get("one"));
        assertNull(map.get("two"));
    }

    @Test
    public void testFixAccess() throws IOException, JsonProcessingException {
        JavaType listType = mapper().constructType(List.class, String.class);
        JavaType mapType = mapper().constructType(Map.class, String.class, Integer.class);

        TestBean bean = new TestBean();

        ObjectNode node = mapper().createObjectNode();
        node.put("list", "[\"one\",\"two\"]");
        node.put("map", "{\"one\":1,\"two\":2}");

        BeanDescription beanDesc = mapper().findBeanDescription(TestBean.class, null);

        DeserializationContext ctxt = context();
        JsonParser p = createParser(node);

        SetterlessProperty propertyList = (SetterlessProperty) beanDesc.findProperty("list", List.class);
        propertyList.fixAccess(ctxt);
        Method getter = ClassUtil.findMethod(TestBean.class, "getList", new Class[]{});
        assertTrue(getter.isAccessible());

        SetterlessProperty propertyMap = (SetterlessProperty) beanDesc.findProperty("map", Map.class);
        propertyMap.fixAccess(ctxt);
        Method getterMap = ClassUtil.findMethod(TestBean.class, "getMap", new Class[]{});
        assertTrue(getterMap.isAccessible());
    }
}