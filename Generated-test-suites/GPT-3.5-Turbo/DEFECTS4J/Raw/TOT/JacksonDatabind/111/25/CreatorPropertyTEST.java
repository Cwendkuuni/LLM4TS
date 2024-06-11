package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(TestClass.class);
        PropertyName wrapperName = new PropertyName("testWrapperName");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);
        
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
    public void testSettersAndGetters() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(TestClass.class);
        PropertyName wrapperName = new PropertyName("testWrapperName");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);
        
        PropertyName newName = new PropertyName("newName");
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        
        property = property.withName(newName);
        assertEquals(newName, property.getName());
        
        property = property.withValueDeserializer(deser);
        assertEquals(deser, property.getValueDeserializer());
        
        property = property.withNullProvider(nva);
        assertEquals(nva, property.getNullValueProvider());
        
        property.fixAccess(new DeserializationConfig());
        
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property.getFallbackSetter());
        
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
        
        Annotation annotation = new Annotation();
        assertSame(annotation, property.getAnnotation(annotation.getClass()));
        
        property.inject(new DeserializationContext(), new Object());
        
        try {
            property.findInjectableValue(new DeserializationContext(), new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Property 'testName' (type " + CreatorProperty.class.getName() + ") has no injectable value id configured",
                    e.getMessage());
        }
    }
    
    @Test
    public void testDeserializationAndSet() {
        // Create mock objects
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Create a mock creator property
        CreatorProperty property = new CreatorProperty();
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        property.setFallbackSetter(fallbackSetter);
        
        // Test deserializeAndSet
        property.deserializeAndSet(p, ctxt, instance);
        //asserts
        
        // Test deserializeSetAndReturn
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        //asserts
        
        // Test set
        property.set(instance, result);
        //asserts
        
        // Test setAndReturn
        result = property.setAndReturn(instance, result);
        //asserts
    }
    
    @Test
    public void testGetInjectableValueId() {
        Object injectableValueId = new Object();
        CreatorProperty property = new CreatorProperty();
        property.setInjectableValueId(injectableValueId);
        assertEquals(injectableValueId, property.getInjectableValueId());
    }
    
    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        Object injectableValueId = new Object();
        CreatorProperty property = new CreatorProperty(name, new JavaType(), new PropertyName(),
                new TypeDeserializer(), new Annotations(), new AnnotatedParameter(), 0,
                injectableValueId, new PropertyMetadata());
        assertEquals("[creator property, name 'testName'; inject id 'java.lang.Object@hashcode']", property.toString());
    }
}