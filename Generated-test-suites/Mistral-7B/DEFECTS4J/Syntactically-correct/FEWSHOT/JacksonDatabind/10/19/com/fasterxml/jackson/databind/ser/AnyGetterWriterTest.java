package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Introspector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

    private static class TestBean {

        @JsonAnyGetter
        Map<String, String> getMapProperty() {
            return mapProperty;
        }

        private Map<String, String> mapProperty = new HashMap<>();

        @JsonProperty("name")
        String name = "TestBean";
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(mapSerializer.serializeFields((Map<?,?>) anyCaptor.capture(), gen, provider)).thenReturn(null);

        writer.getAndSerialize(bean, gen, provider);

        Map<String, String> map = anyCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals("TestBean", map.get("class"));
    }

    @Test
    public void testGetAndSerialize_ValueIsNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        // Test that getAndSerialize does not throw an exception when value is null
        verifyNoThrows(writer::getAndSerialize);
    }

    @Test
    public void testGetAndSerialize_ValueNotMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Object nonMapValue = new Object();
        when(accessor.getValue(bean)).thenReturn(nonMapValue);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        PropertyFilter filter = mock(PropertyFilter.class);

        when(mapSerializer.serializeFilteredFields((Map<?,?>) anyCaptor.capture(), gen, provider, filter, null)).thenReturn(null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        writer.getAndFilter(bean, gen, provider, filter);

        Map<String, String> map = anyCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals("TestBean", map.get("class"));
    }

    @Test
    public void testGetAndFilter_ValueIsNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        writer.getAndFilter(bean, gen, provider, filter);

        // Test that getAndFilter does not throw an exception when value is null
        verifyNoThrows(writer::getAndFilter);
    }

    @Test
    public void testGetAndFilter_ValueNotMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        MapSerializer mapSerializer = mock(MapSerializer.class);
        BeanProperty property = new BeanProperty(ClassUtil.findProperty(TestBean.class, "mapProperty"), null, null);
        AnnotatedMember accessor = Introspector.findAnnotatedMember(TestBean.class, "getMapProperty");

        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        PropertyFilter filter = mock(PropertyFilter.class);

        Object nonMapValue = new Object();
        when(accessor.getValue(bean)).thenReturn(nonMapValue);

        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonMapperFactory());
        Object bean = new TestBean();

        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);

        writer.resolve(provider);

        // Test that resolve does not throw an exception
        verifyNoThrows(writer::resolve);
    }
}