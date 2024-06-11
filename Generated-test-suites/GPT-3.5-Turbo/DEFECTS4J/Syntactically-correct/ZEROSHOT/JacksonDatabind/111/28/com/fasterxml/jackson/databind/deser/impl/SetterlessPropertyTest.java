package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        
        assertEquals(propDef, property.getPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        NullValueProvider nva = new NullValueProvider();
        
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        
        assertEquals(property.getPropertyDefinition(), newProperty.getPropertyDefinition());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getValueTypeDeserializer(), newProperty.getValueTypeDeserializer());
        assertEquals(property.getContextAnnotations(), newProperty.getContextAnnotations());
        assertEquals(property.getMember(), newProperty.getMember());
        assertEquals(nva, newProperty.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        
        property.fixAccess(config);
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Object instance = new Object();
        
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Object instance = new Object();
        
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        
        property.set(new Object(), new Object());
    }
    
    @Test
    public void testSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Object instance = new Object();
        
        try {
            property.setAndReturn(instance, new Object());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
}