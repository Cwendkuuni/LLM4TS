package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(TestClass.class);
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, 
                                                              contextAnnotations, param, index, 
                                                              injectableValueId, metadata);
        
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        CreatorProperty newCreatorProperty = creatorProperty.withNullProvider(nva);
        
        assertEquals(nva, newCreatorProperty.getNullValueProvider());
    }

    @Test
    public void testWithFallbackSetter() {
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        creatorProperty.markAsIgnorable();
        
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, new Object(),
                                                              null);
        
        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, new Object(),
                                                              null);
        
        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (IOException e) {
            // Expected IOException to be thrown
        }
    }

    @Test
    public void testGetAnnotation() {
        Class cls = new Class();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        assertNull(creatorProperty.getAnnotation(cls));
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (IOException e) {
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (IOException e) {
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        try {
            creatorProperty.set(instance, value);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (IOException e) {
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        try {
            creatorProperty.setAndReturn(instance, value);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (IOException e) {
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null,
                                                              null, null, 0, null,
                                                              null);
        
        assertEquals("[creator property, name 'null'; inject id 'null']", creatorProperty.toString());
    }
}