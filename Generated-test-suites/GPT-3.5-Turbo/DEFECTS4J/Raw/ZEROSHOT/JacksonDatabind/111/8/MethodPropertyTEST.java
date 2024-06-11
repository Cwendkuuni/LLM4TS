package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        BeanPropertyDefinition def = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty property = new MethodProperty(def, type, typeDeser, contextAnnotations, method);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
        
        Assert.assertEquals("John", instance.getName());
        Assert.assertEquals(30, instance.getAge());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        BeanPropertyDefinition def = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty property = new MethodProperty(def, type, typeDeser, contextAnnotations, method);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        Assert.assertEquals("John", result.getName());
        Assert.assertEquals(30, result.getAge());
    }
    
    @Test
    public void testSet() throws Exception {
        BeanPropertyDefinition def = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty property = new MethodProperty(def, type, typeDeser, contextAnnotations, method);
        
        Object instance = new Object();
        property.set(instance, "John");
        
        Assert.assertEquals("John", instance.getName());
    }
    
    @Test
    public void testSetAndReturn() throws Exception {
        BeanPropertyDefinition def = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty property = new MethodProperty(def, type, typeDeser, contextAnnotations, method);
        
        Object instance = new Object();
        Object result = property.setAndReturn(instance, "John");
        
        Assert.assertEquals("John", result.getName());
    }
}