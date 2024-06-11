package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 1;
        Object injectableValueId = null;
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testWithMethods() {
        CreatorProperty creatorProperty = new CreatorProperty();
        
        PropertyName newName = new PropertyName("newName");
        assertNotNull(creatorProperty.withName(newName));
        
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        assertNull(creatorProperty.withValueDeserializer(deser));
        
        NullValueProvider nva = new NullValueProvider();
        assertNull(creatorProperty.withNullProvider(nva));
    }

    @Test
    public void testFixAccess() {
        CreatorProperty creatorProperty = new CreatorProperty();
        DeserializationConfig config = new DeserializationConfig();
        creatorProperty.fixAccess(config);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty creatorProperty = new CreatorProperty();
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty creatorProperty = new CreatorProperty();
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        Throwable exception = assertThrows(JsonMappingException.class, () -> {
            creatorProperty.findInjectableValue(context, beanInstance);
        });
        
        assertEquals("Property 'null' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", exception.getMessage());
    }

    @Test
    public void testInject() {
        CreatorProperty creatorProperty = new CreatorProperty();
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        Throwable exception = assertThrows(JsonMappingException.class, () -> {
            creatorProperty.inject(context, beanInstance);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty creatorProperty = new CreatorProperty();
        assertNull(creatorProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testDeserializeAndSet() {
        CreatorProperty creatorProperty = new CreatorProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testDeserializeSetAndReturn() {
        CreatorProperty creatorProperty = new CreatorProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testSet() {
        CreatorProperty creatorProperty = new CreatorProperty();
        Object instance = new Object();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty.set(instance, instance);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testSetAndReturn() {
        CreatorProperty creatorProperty = new CreatorProperty();
        Object instance = new Object();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty.setAndReturn(instance, instance);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testGetInjectableValueId() {
        CreatorProperty creatorProperty = new CreatorProperty();
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        CreatorProperty creatorProperty = new CreatorProperty();
        assertEquals("[creator property, name 'null'; inject id 'null']", creatorProperty.toString());
    }

    @Test
    public void testVerifySetter() {
        CreatorProperty creatorProperty = new CreatorProperty();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty._verifySetter();
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }

    @Test
    public void testReportMissingSetter() {
        CreatorProperty creatorProperty = new CreatorProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        Throwable exception = assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty._reportMissingSetter(p, ctxt);
        });
        
        assertEquals("No fallback setter/field defined for creator property 'null'", exception.getMessage());
    }
}