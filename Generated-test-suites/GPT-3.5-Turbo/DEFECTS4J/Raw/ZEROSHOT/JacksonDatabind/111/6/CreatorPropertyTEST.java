package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getMember());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }
    
    @Test
    public void testCopyConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty src = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        PropertyName newName = new PropertyName("newTest");
        
        CreatorProperty property = new CreatorProperty(src, newName);
        
        assertEquals(newName, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getMember());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        // Assert that there are no exceptions thrown
        property.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        // Assert that there are no exceptions thrown
        property.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        // Assert that there are no exceptions thrown
        property.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.builder().build();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        // Assert that there are no exceptions thrown
        property.setAndReturn(instance, value);
    }

}