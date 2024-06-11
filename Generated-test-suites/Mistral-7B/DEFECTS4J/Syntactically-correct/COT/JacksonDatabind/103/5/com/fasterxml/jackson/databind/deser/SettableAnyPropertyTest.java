package com.example.junit; // replace "com.example.junit" with your test package name

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class SettableAnyPropertyTest {

    private SettableAnyProperty _property;
    private Object _instance;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        AnnotatedField field = mapper.findDeserProperty("testField", Map.class, javaType);
        AnnotatedMember setter = field.getMember();
        _property = new SettableAnyProperty(new BeanProperty(field), setter, javaType, null, null, null);
        _instance = new Object();
    }

    @Test
    public void testBasicSettableAnyPropertyConstruction() {
        assertNotNull(_property);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        String propName = "testProp";
        Object value = new Object();
        JsonParser p = mapper.getFactory().createParser("{\"key\":\"testProp\", \"value\":\"value\"}");
        _property.deserializeAndSet(p, null, _instance, propName);
        assertTrue(_instance instanceof Map);
        Map<?, ?> map = (Map<?, ?>) _instance;
        assertTrue(map.containsKey(propName));
        assertTrue(map.get(propName) instanceof Object);
        assertTrue(map.get(propName).equals(value));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetNullValueDeserializer() throws IOException {
        String propName = "testProp";
        JsonParser p = mapper.getFactory().createParser("{\"key\":\"testProp\", \"value\":null}");
        _property.deserializeAndSet(p, null, _instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetUnexpectedValueType() throws IOException {
        String propName = "testProp";
        JsonParser p = mapper.getFactory().createParser("{\"key\":\"testProp\", \"value\":42}");
        _property.deserializeAndSet(p, null, _instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetMissingKeyDeserializer() throws IOException {
        String propName = "testProp";
        _property = new SettableAnyProperty(_property.getProperty(), _property.getSetter(), _property.getType(), null, _property.getValueDeserializer, _property.getValueTypeDeserializer);
        JsonParser p = mapper.getFactory().createParser("{\"key\":\"missingKey\"}");
        _property.deserializeAndSet(p, null, _instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetUnresolvedForwardReference() throws IOException {
        String propName = "testProp";
        JsonParser p = mapper.getFactory().createParser("{\"key\":\"testProp\"}");
        _property = new SettableAnyProperty(_property.getProperty(), _property.getSetter(), _property.getType(), null, _property.getValueDeserializer, _property.getValueTypeDeserializer);
        _property.deserializeAndSet(p, null, _instance, propName);
    }

    @Test
    public void testSettableAnyPropertySet() throws IOException {
        String propName = "testProp";
        Object value = new Object();
        _property.set(_instance, propName, value);
        assertTrue(_instance instanceof Map);
        Map<?, ?> map = (Map<?, ?>) _instance;
        assertTrue(map.containsKey(propName));
        assertTrue(map.get(propName) instanceof Object);
        assertTrue(map.get(propName).equals(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertySetNullInstance() throws IOException {
        String propName = "testProp";
        Object value = new Object();
        _property.set(null, propName, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertySetNullPropName() throws IOException {
        Object value = new Object();
        _property.set(_instance, null, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertySetNullValue() throws IOException {
        String propName = "testProp";
        _property.set(_instance, propName, null);
    }
}