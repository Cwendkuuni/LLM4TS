package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {
    private PropertyName name;
    private PropertyName wrapperName;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private TypeDeserializer typeDeser;
    private Object injectableValueId;
    private PropertyMetadata metadata;
    private CreatorProperty creatorProperty;

    @Before
    public void setUp() {
        name = new PropertyName("test");
        wrapperName = new PropertyName("testWrapper");
        contextAnnotations = null;
        param = null;
        typeDeser = null;
        injectableValueId = null;
        metadata = null;

        creatorProperty = new CreatorProperty(name, null, wrapperName, typeDeser, contextAnnotations, param, 0, injectableValueId, metadata);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(Test.class));
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
    public void testDeserializeAndSet() {
        // Setup
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();

        try {
            // Test
            creatorProperty.deserializeAndSet(p, ctxt, instance);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Setup
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();

        try {
            // Test
            Object result = creatorProperty.deserializeSetAndReturn(p, ctxt, instance);

            // Verify
            assertNotNull(result);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testSet() {
        // Setup
        Object instance = new Object();
        Object value = new Object();

        try {
            // Test
            creatorProperty.set(instance, value);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testSetAndReturn() {
        // Setup
        Object instance = new Object();
        Object value = new Object();

        try {
            // Test
            Object result = creatorProperty.setAndReturn(instance, value);

            // Verify
            assertNotNull(result);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        // Test
        String result = creatorProperty.toString();

        // Verify
        assertEquals("[creator property, name 'test'; inject id 'null']", result);
    }

    @Test
    public void testFindInjectableValue_whenInjectableValueIdIsNull() {
        // Setup
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object beanInstance = new Object();

        try {
            // Test
            creatorProperty.findInjectableValue(ctxt, beanInstance);

            // Verify
            fail("Should have thrown a JsonMappingException");
        } catch (JsonMappingException e) {
            // Verify
            assertEquals("Property 'test' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", e.getMessage());
        } catch (Exception e) {
            // Verify
            fail("Should have thrown a JsonMappingException");
        }
    }

    @Test
    public void testInject() {
        // Setup
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object beanInstance = new Object();

        try {
            // Test
            creatorProperty.inject(ctxt, beanInstance);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testSetFallbackSetter() {
        // Setup
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        creatorProperty.setFallbackSetter(fallbackSetter);

        // Verify
        assertEquals(fallbackSetter, creatorProperty.fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        // Test
        creatorProperty.markAsIgnorable();

        // Verify
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testWithNullProvider() {
        // Setup
        NullValueProvider nva = new NullValueProvider();

        // Test
        SettableBeanProperty result = creatorProperty.withNullProvider(nva);

        // Verify
        assertNotNull(result);
        assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        // Setup
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();

        try {
            // Test
            creatorProperty.fixAccess(config);
        } catch (Exception e) {
            // Verify
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testWithName() {
        // Setup
        PropertyName newName = new PropertyName("newTest");

        // Test
        SettableBeanProperty result = creatorProperty.withName(newName);

        // Verify
        assertNotNull(result);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        // Setup
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        // Test
        SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);

        // Verify
        assertNotNull(result);
        assertEquals(deser, result._valueDeserializer);
    }

    @Test
    public void testWithNames() {
        // Setup
        PropertyName newName = new PropertyName("newTest");

        // Test
        SettableBeanProperty result = creatorProperty.withName(newName);

        // Verify
        assertNotNull(result);
        assertEquals(newName, result.getName());
    }
}