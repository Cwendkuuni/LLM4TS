package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$PropertyNameHashStash, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, alwaysAsId);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$PropertyNameHashStash, alwaysAsId=true", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithResolver() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$PropertyNameHashStash, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyName() {
        try {
            new ObjectIdInfo(null, String.class, ObjectIdGenerators.PropertyNameHashStash.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullScope() {
        PropertyName propName = new PropertyName("testProperty");
        try {
            new ObjectIdInfo(propName, null, ObjectIdGenerators.PropertyNameHashStash.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propName = new PropertyName("testProperty");
        try {
            new ObjectIdInfo(propName, String.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        try {
            new ObjectIdInfo(propName, scope, generator, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$PropertyNameHashStash, alwaysAsId=true", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyNameAndAlwaysAsId() {
        try {
            new ObjectIdInfo(null, String.class, ObjectIdGenerators.PropertyNameHashStash.class, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullPropertyNameAndNoAlwaysAsId() {
        try {
            new ObjectIdInfo(null, String.class, ObjectIdGenerators.PropertyNameHashStash.class, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullScopeAndAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        try {
            new ObjectIdInfo(propName, null, generator, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullScopeAndNoAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdGenerators.PropertyNameHashStash> generator = ObjectIdGenerators.PropertyNameHashStash.class;
        try {
            new ObjectIdInfo(propName, null, generator, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullGeneratorAndAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        try {
            new ObjectIdInfo(propName, scope, null, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testObjectIdInfoWithNullGeneratorAndNoAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        try {
            new ObjectIdInfo(propName, scope, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}