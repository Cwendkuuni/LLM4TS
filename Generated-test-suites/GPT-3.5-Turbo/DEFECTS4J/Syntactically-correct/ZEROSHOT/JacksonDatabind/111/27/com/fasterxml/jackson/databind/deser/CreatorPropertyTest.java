package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() {
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    }

    @Test
    public void testGetName() {
        assertNull(creatorProperty.getName());
    }

    @Test
    public void testGetType() {
        assertNull(creatorProperty.getType());
    }

    @Test
    public void testGetWrapperName() {
        assertNull(creatorProperty.getWrapperName());
    }

    @Test
    public void testGetTypeDeserializer() {
        assertNull(creatorProperty.getTypeDeserializer());
    }

    @Test
    public void testGetContextAnnotations() {
        assertNull(creatorProperty.getContextAnnotations());
    }

    @Test
    public void testGetMetadata() {
        assertNull(creatorProperty.getMetadata());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);
        assertEquals(result, creatorProperty);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = creatorProperty.withNullProvider(nva);
        assertEquals(result, creatorProperty);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        creatorProperty.fixAccess(config);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = null;
        creatorProperty.setFallbackSetter(fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() {
        DeserializationContext context = null;
        Object beanInstance = null;
        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals(e.getMessage(), "Property 'null' (type null) has no injectable value id configured");
        }
    }

    @Test
    public void testInject() {
        DeserializationContext context = null;
        Object beanInstance = null;
        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testToString() {
        String result = creatorProperty.toString();
        assertEquals(result, "[creator property, name 'null'; inject id 'null']");
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object value = null;
        try {
            creatorProperty.set(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = null;
        Object value = null;
        try {
            creatorProperty.setAndReturn(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }
}