package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MethodPropertyTest {
    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        
        assertEquals(propDef, property.getMetadata());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }
    
    @Test
    public void testWithNullProvider() {
        MethodProperty original = new MethodProperty();
        NullValueProvider nva = new NullValueProvider();
        
        MethodProperty property = original.withNullProvider(nva);
        
        assertNull(property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }
    
    // Add test cases for other methods in MethodProperty class
}