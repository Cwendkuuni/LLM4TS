package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    private ObjectMapper mapper;
    
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableAnyProperty property = createSettableAnyProperty();
        JsonParser parser = createJsonParser("{ \"prop_name\": \"value\" }");
        DeserializationContext ctxt = createDeserializationContext();
        Object instance = createInstance();
        String propName = createPropName();

        property.deserializeAndSet(parser, ctxt, instance, propName);

        assertEquals("value", ((Map<?,?>) instance).get("prop_name"));
    }

    @Test
    public void testDeserializeAndSet_FieldIsNull() throws IOException {
        SettableAnyProperty property = createSettableAnyProperty();
        JsonParser parser = createJsonParser("{ \"prop_name\": \"value\" }");
        DeserializationContext ctxt = createDeserializationContext();
        Object instance = createInstance();
        String propName = createPropName();
        
        ((Map<?,?>) instance).put(null, null); // Set field to null

        property.deserializeAndSet(parser, ctxt, instance, propName);

        assertEquals("value", ((Map<?,?>) instance).get("prop_name"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_Exception() throws IOException {
        SettableAnyProperty property = createSettableAnyProperty();
        JsonParser parser = createJsonParser("{ \"prop_name\": \"value\" }");
        DeserializationContext ctxt = createDeserializationContext();
        Object instance = createInstance();
        String propName = createPropName();

        property.deserializeAndSet(parser, ctxt, instance, propName);
    }

    @Test
    public void testDeserialize_NullValue() throws IOException {
        SettableAnyProperty property = createSettableAnyProperty();
        JsonParser parser = createJsonParser("null");
        DeserializationContext ctxt = createDeserializationContext();

        Object value = property.deserialize(parser, ctxt);

        assertNull(value);
    }

    @Test
    public void testDeserialize_WithTypeDeser() throws IOException {
        SettableAnyProperty property = createSettableAnyPropertyWithDeser();
        JsonParser parser = createJsonParser("\"value\"");
        DeserializationContext ctxt = createDeserializationContext();

        Object value = property.deserialize(parser, ctxt);

        assertEquals("value", value);
    }

    @Test
    public void testSet_FieldSetter() throws IOException {
        SettableAnyProperty property = createSettableAnyPropertyWithFieldSetter();
        Object instance = createInstance();

        property.set(instance, "prop_name", "value");

        assertEquals("value", ((Map<?,?>) instance).get("prop_name"));
    }

    @Test
    public void testSet_FieldSetter_NullValue() throws IOException {
        SettableAnyProperty property = createSettableAnyPropertyWithFieldSetter();
        Object instance = createInstance();

        property.set(instance, "prop_name", null);

        assertNull(((Map<?,?>) instance).get("prop_name"));
    }

    @Test
    public void testSet_MethodSetter() throws IOException {
        SettableAnyProperty property = createSettableAnyPropertyWithMethodSetter();
        Object instance = createInstance();

        property.set(instance, "prop_name", "value");

        assertEquals("value", ((Map<?,?>) instance).get("prop_name"));
    }

    @Test
    public void testSet_MethodSetter_NullValue() throws IOException {
        SettableAnyProperty property = createSettableAnyPropertyWithMethodSetter();
        Object instance = createInstance();

        property.set(instance, "prop_name", null);

        assertNull(((Map<?,?>) instance).get("prop_name"));
    }

    private SettableAnyProperty createSettableAnyProperty() {
        BeanProperty property = null;
        AnnotatedMember setter = createAnnotatedMember();
        JavaType type = createType();
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        return new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
    }

    private SettableAnyProperty createSettableAnyPropertyWithDeser() {
        BeanProperty property = null;
        AnnotatedMember setter = createAnnotatedMember();
        JavaType type = createType();
        JsonDeserializer<Object> valueDeser = createValueDeserializer();
        TypeDeserializer typeDeser = createTypeDeserializer();

        return new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
    }

    private SettableAnyProperty createSettableAnyPropertyWithFieldSetter() {
        BeanProperty property = null;
        AnnotatedMember setter = createFieldSetter();
        JavaType type = createType();
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        return new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
    }

    private SettableAnyProperty createSettableAnyPropertyWithMethodSetter() {
        BeanProperty property = null;
        AnnotatedMember setter = createMethodSetter();
        JavaType type = createType();
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        return new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
    }
    
    private AnnotatedMember createAnnotatedMember() {
        return new AnnotatedField(null, null, null);
    }

    private AnnotatedMember createFieldSetter() {
        return new AnnotatedField(null, null, null);
    }

    private AnnotatedMember createMethodSetter() {
        return new AnnotatedMethod(null, null, null, null);
    }

    private JavaType createType() {
        return mapper.constructType(Map.class);
    }

    private JsonDeserializer<Object> createValueDeserializer() throws IOException {
        return new ObjectMapper().readValue("{}", JsonDeserializer.class);
    }

    private TypeDeserializer createTypeDeserializer() {
        return null;
    }

    private JsonParser createJsonParser(String json) throws IOException {
        return mapper.getFactory().createParser(json);
    }

    private DeserializationContext createDeserializationContext() {
        return mapper.getDeserializationContext();
    }

    private Object createInstance() {
        return new HashMap<>();
    }

    private String createPropName() {
        return "prop_name";
    }
}