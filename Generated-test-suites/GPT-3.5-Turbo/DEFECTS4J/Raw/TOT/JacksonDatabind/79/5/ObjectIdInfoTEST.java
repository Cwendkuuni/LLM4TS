package com.fasterxml.jackson.databind.introspect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotEquals(objectIdInfo, updatedObjectIdInfo);
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }

    public static class CustomObjectIdGenerator<T> extends ObjectIdGenerator<T> {
        // CustomObjectIdGenerator implementation
    }

    public static class CustomObjectIdResolver extends ObjectIdResolver {
        // CustomObjectIdResolver implementation
    }
}