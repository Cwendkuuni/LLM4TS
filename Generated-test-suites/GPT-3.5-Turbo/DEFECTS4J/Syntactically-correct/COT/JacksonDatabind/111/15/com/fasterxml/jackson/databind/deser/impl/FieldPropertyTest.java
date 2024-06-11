

package com.fasterxml.jackson.databind.deser.impl;

import org.junit.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.io.IOException;

public class FieldPropertyTest {
    @Test
    public void testConstructor() {
        // Given
        BeanPropertyDefinition propDef = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        
        // When
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        // Then
        Assert.assertEquals(propDef, fieldProperty.getMetadata());
        Assert.assertEquals(type, fieldProperty.getType());
        Assert.assertEquals(typeDeser, fieldProperty.getValueDeserializer());
        Assert.assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        Assert.assertEquals(field, fieldProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        // Given
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        // When
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        
        // Then
        // Add assertions
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        // When
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        // Then
        // Add assertions
        Assert.assertEquals(instance, result);
    }
    
    @Test
    public void testSet() throws IOException {
        // Given
        Object instance = null;
        Object value = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        // When
        fieldProperty.set(instance, value);
        
        // Then
        // Add assertions
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        Object instance = null;
        Object value = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        // When
        Object result = fieldProperty.setAndReturn(instance, value);
        
        // Then
        // Add assertions
        Assert.assertEquals(instance, result);
    }

}

