package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

public class SettableAnyPropertyTest {
    
    @Test
    public void testGetProperty() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMember();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals(property, anyProperty.getProperty());
    }
    
    @Test
    public void testHasValueDeserializer() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMember();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals(true, anyProperty.hasValueDeserializer());
    }
    
    @Test
    public void testGetType() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMember();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals(type, anyProperty.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithMissingMethod() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = null;
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        String propName = "prop";
        anyProperty.deserializeAndSet(p, ctxt, instance, propName);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMethod setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        String propName = "prop";
        anyProperty.deserializeAndSet(p, ctxt, instance, propName);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMember();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(new JsonDeserializer<Object>().deserialize(p, ctxt), anyProperty.deserialize(p, ctxt));
    }
    
    @Test
    public void testSet() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMethod setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        Object instance = new Object();
        Object propName = "prop";
        Object value = "value";
        anyProperty.set(instance, propName, value);
    }
    
    @Test(expected = JsonMappingException.class)
    public void test_Set_throwAsIOE() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMethod setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        Object instance = new Object();
        Object propName = "prop";
        Object value = new Object();
        anyProperty.set(instance, propName, value);
    }
    
    @Test
    public void test_GetClassName() {
        BeanProperty property = new BeanProperty();
        AnnotatedMethod setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals(setter.getDeclaringClass().getName(), anyProperty.getClassName());
    }

    @Test
    public void testToString() {
        BeanProperty property = new BeanProperty();
        AnnotatedMethod setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals("[any property on class " + anyProperty.getClassName() + "]", anyProperty.toString());
    }
}