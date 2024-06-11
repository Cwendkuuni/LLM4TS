package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * JUnit 4 test cases for ObjectIdInfo class
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithNameStringScopeGenerator() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", Object.class, ObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertNotNull(objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameClassScopeGenerator() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertNotNull(objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameClassScopeGeneratorBooleanResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameClassScopeGeneratorBooleanNullResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, false, null);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertNotNull(objectIdInfo.getResolverType()); // default value: SimpleObjectIdResolver.class
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameClassScopeGenerator() {
        PropertyName propertyName = new PropertyName("name");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, ObjectIdGenerator.class);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertNotNull(objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameClassScopeGeneratorBooleanResolver() {
        PropertyName propertyName = new PropertyName("name");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullPropertyName() {
        new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullScope() {
        new ObjectIdInfo(new PropertyName("name"), null, ObjectIdGenerator.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullGenerator() {
        new ObjectIdInfo(new PropertyName("name"), Object.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullResolver() {
        new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, false, null);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdSameState() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, true);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdDifferentState() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, ObjectIdGenerator.class, true);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(false);
        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
    }
}