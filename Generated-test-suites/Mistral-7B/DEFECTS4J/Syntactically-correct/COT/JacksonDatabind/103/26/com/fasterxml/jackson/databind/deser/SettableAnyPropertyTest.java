package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private static final Object MAP_INSTANCE = new HashMap<>();

    private SettableAnyProperty _anyProperty;

    @Before
    public void setUp() {
        BeanProperty property = new BeanProperty("testProperty", null, null, null);
        AnnotatedField setter = new AnnotatedField(SettableAnyPropertyTest.class, "testSetter", Map.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        _anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testSettableAnyPropertyConstructor() {
        Assert.assertNotNull(_anyProperty);
    }

    @Test
    public void testGetProperty() throws JsonProcessingException {
        Assert.assertEquals(_anyProperty.getProperty(), _anyProperty._property);
    }

    @Test
    public void testHasValueDeserializer() throws JsonProcessingException {
        JsonDeserializer<Object> deserializer = new StandardDeserializer<>(String.class);
        SettableAnyProperty propertyWithValueDeserializer = _anyProperty.withValueDeserializer(deserializer);
        Assert.assertTrue(propertyWithValueDeserializer.hasValueDeserializer());
        Assert.assertFalse(_anyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetStringType() throws JsonProcessingException {
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        _anyProperty.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
        String value = (String) MAP_INSTANCE.get("testProperty");
        Assert.assertNotNull(value);
        Assert.assertEquals("", value);
    }

    @Test
    public void testSetValueDeserializer() throws JsonProcessingException {
        JsonDeserializer<Object> deserializer = new StandardDeserializer<>(String.class);
        SettableAnyProperty propertyWithValueDeserializer = _anyProperty.withValueDeserializer(deserializer);
        propertyWithValueDeserializer.set(MAP_INSTANCE, "testProperty", "testValue");
        String value = (String) MAP_INSTANCE.get("testProperty");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testSetValueDeserializerWithTypeDeserializer() throws JsonProcessingException {
        JsonDeserializer<Object> deserializer = new StandardDeserializer<>(String.class);
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        SettableAnyProperty propertyWithValueDeserializer = _anyProperty.withValueDeserializer(deserializer, typeDeserializer);
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        parser.nextToken();
        propertyWithValueDeserializer.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
        String value = (String) MAP_INSTANCE.get("testProperty");
        Assert.assertEquals("testValue", value);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidType() throws JsonProcessingException {
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        _anyProperty.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), new Object(), "testProperty");
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueDeserializer() throws JsonProcessingException {
        SettableAnyProperty propertyWithNullValueDeserializer = _anyProperty.withValueDeserializer(null);
        propertyWithNullValueDeserializer.deserializeAndSet(null, null, MAP_INSTANCE, "testProperty");
    }

    @Test(expected = IOException.class)
    public void testNullValue() throws JsonProcessingException {
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        parser.nextToken();
        parser.nextToken(); // null value
        _anyProperty.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
    }

    @Test(expected = JsonMappingException.class)
    public void testNullKeyDeserializer() throws JsonProcessingException {
        _anyProperty._keyDeserializer = null;
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{\"testProperty\": null}");
        _anyProperty.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPropertyType() throws JsonProcessingException {
        _anyProperty._type = ObjectType.Std_ClassLong;
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{\"testProperty\": \"testValue\"}");
        _anyProperty.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
    }

    @Test
    public void testSetValueWithField() throws JsonProcessingException {
        BeanProperty property = new BeanProperty("testProperty", null, null, new AnnotatedField(SettableAnyPropertyTest.class, "testSetterField", Map.class));
        AnnotatedField setter = new AnnotatedField(SettableAnyPropertyTest.class, "testSetterField", Map.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty propertyWithSetterField = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        propertyWithSetterField.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
        Map map = (Map) MAP_INSTANCE;
        Assert.assertTrue(map.containsKey("testProperty"));
        Assert.assertEquals("testProperty", map.keySet().iterator().next());
        Assert.assertEquals("", map.get("testProperty"));
        propertyWithSetterField.set(MAP_INSTANCE, "testProperty", "testValue");
        Assert.assertEquals("testValue", map.get("testProperty"));
    }

    @Test
    public void testSetValueWithMethod() throws JsonProcessingException {
        BeanProperty property = new BeanProperty("testProperty", null, new AnnotatedMethod(SettableAnyPropertyTest.class, "testSetterMethod", Map.class, new Class[0]), null);
        AnnotatedMethod setter = new AnnotatedMethod(SettableAnyPropertyTest.class, "testSetterMethod", Map.class, new Class[0]);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty propertyWithSetterMethod = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser("{}");
        propertyWithSetterMethod.deserializeAndSet(parser, ObjectMapper.getDefaultInstance().getDeserializationConfig(), MAP_INSTANCE, "testProperty");
        Map map = (Map) MAP_INSTANCE;
        Assert.assertTrue(map.containsKey("testProperty"));
        Assert.assertEquals("testProperty", map.keySet().iterator().next());
        Assert.assertEquals("", map.get("testProperty"));
        propertyWithSetterMethod.set(MAP_INSTANCE, "testProperty", "testValue");
        Assert.assertEquals("testValue", map.get("testProperty"));
    }
}