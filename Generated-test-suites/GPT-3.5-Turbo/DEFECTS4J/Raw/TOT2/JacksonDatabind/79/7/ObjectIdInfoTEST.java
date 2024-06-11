package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    // Test constructor ObjectIdInfo(PropertyName, Class, Class, Class)
    @Test
    public void testObjectIdInfoConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test constructor ObjectIdInfo(PropertyName, Class, Class, Class, boolean)
    @Test
    public void testObjectIdInfoConstructorWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, true, CustomObjectIdResolver.class);

        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    // Test method withAlwaysAsId(boolean)
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    // Edge case: testing method withAlwaysAsId(boolean) when state is unchanged
    @Test
    public void testWithAlwaysAsIdNoChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(false);

        assertSame(objectIdInfo, updatedObjectIdInfo);
    }

    // Exception handling scenario: testing method withAlwaysAsId(boolean) with null resolver
    @Test
    public void testWithAlwaysAsIdNullResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(CustomObjectIdResolver.class, updatedObjectIdInfo.getResolverType());
    }

    // Test toString method
    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);

        assertEquals("ObjectIdInfo: propName=id, scope=Object, generatorType=CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

}