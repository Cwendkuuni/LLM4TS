package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.PropertyNamingStrategy.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.PropertyNamingStrategy.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true, resolver);

        ObjectIdInfo objectIdInfoWithFalse = new ObjectIdInfo(propName, scope, generator, false, resolver);

        assertNotNull(objectIdInfo);
        assertNotNull(objectIdInfoWithFalse);

        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertFalse(objectIdInfoWithFalse.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullProperties() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false, null);

        assertNotNull(objectIdInfo);
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<ObjectIdGenerator<Object>> nullGenerator = null;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propName, scope, nullGenerator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.PropertyNamingStrategy.class;
        Class<ObjectIdResolver> nullResolver = null;

        new ObjectIdInfo(propName, scope, generator, false, nullResolver);
    }

    @Test
    public void testToString() {
        PropertyName propName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.PropertyNamingStrategy.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

        String expectedToString = "ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerators$PropertyNamingStrategy, alwaysAsId=false";

        assertEquals(expectedToString, objectIdInfo.toString());
    }
}