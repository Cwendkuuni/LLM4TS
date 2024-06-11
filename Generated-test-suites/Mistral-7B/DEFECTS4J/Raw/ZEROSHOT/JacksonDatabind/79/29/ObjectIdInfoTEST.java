package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propName = new PropertyName("testProp");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        Class<CustomIdResolver> resolver = CustomIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotNull(objectIdInfo);
        assertNotNull(withAlwaysAsId);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithDefaultResolver() {
        PropertyName propName = new PropertyName("testProp");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propName = new PropertyName("testProp");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);

        String toString = objectIdInfo.toString();
        String expectedToString = "ObjectIdInfo: propName=testProp, scope=class java.lang.String, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false";

        assertEquals(expectedToString, toString);
    }
}

class CustomIdGenerator implements ObjectIdGenerator<Object> {
    // empty implementation
}

class CustomIdResolver implements ObjectIdResolver {
    // empty implementation
}