package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {
    
    private ObjectIdReferenceProperty property;
    
    @Test
    public void testConstructor() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testCopyConstructor() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        property = new ObjectIdReferenceProperty(src, deser, nva);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testCopyConstructorWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        property = new ObjectIdReferenceProperty(src, newName);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        property = src.withName(newName);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        property = src.withValueDeserializer(deser);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        NullValueProvider nva = new NullValueProvider();
        property = src.withNullProvider(nva);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        DeserializationConfig config = new DeserializationConfig();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        property.fixAccess(config);
        
        // Assert that the fixAccess method is correctly called for the _forward property
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        Class<A> acls = A.class;
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        Annotation annotation = property.getAnnotation(acls);
        
        // Assert that the getAnnotation method is correctly called for the _forward property
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        AnnotatedMember member = property.getMember();
        
        // Assert that the getMember method is correctly called for the _forward property
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        int creatorIndex = property.getCreatorIndex();
        
        // Assert that the getCreatorIndex method is correctly called for the _forward property
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        property.deserializeAndSet(p, ctxt, instance);
        
        // Assert that the deserializeAndSet method is correctly called for the _forward property
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Assert that the deserializeSetAndReturn method is correctly called for the _forward property
        assertNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        property.set(instance, null);
        
        // Assert that the set method is correctly called for the _forward property
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty();
        property = new ObjectIdReferenceProperty(null, null);
        property._forward = forward;
        
        Object result = property.setAndReturn(instance, null);
        
        // Assert that the setAndReturn method is correctly called for the _forward property
        assertNull(result);
    }
    
    private static final class A {
    }
    
    private static final class SettableBeanProperty {
    }
    
    private static final class NullValueProvider {
    }

    private static final class DeserializationConfig {
    }

}