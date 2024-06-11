package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, false, resolverType);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(resolverType, objectIdInfo.getResolverType());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPropertyName() {
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        try {
            new ObjectIdInfo(null, scope, generatorType);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullScope() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        try {
            new ObjectIdInfo(propertyName, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    // Test cases for other methods and edge cases...

    @Test
    public void testWithAlwaysAsIdNullGenerator() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsIdNullResolver() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, new IdGenerator(), null);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, false);
        String actualToString = objectIdInfo.toString();
        String expectedToString = "ObjectIdInfo: propName=testPropertyName, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdGenerator, alwaysAsId=false";
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        PropertyName propertyName1 = new PropertyName("testPropertyName1");
        PropertyName propertyName2 = new PropertyName("testPropertyName2");
        Class<?> scope1 = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType1 = IdGenerator.class;
        Class<? extends ObjectIdGenerator<?>> generatorType2 = IdGenerator.class;
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName1, scope1, generatorType1, false);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName2, scope1, generatorType2, false);
        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propertyName1, scope1, generatorType1, false);
        ObjectIdInfo objectIdInfo4 = new ObjectIdInfo(propertyName1, scope1, generatorType1, true);
        assertNotSame(objectIdInfo1, objectIdInfo2);
        assertEquals(objectIdInfo1, objectIdInfo3);
        assertNotEquals(objectIdInfo1, objectIdInfo4);
    }

    // Add test cases for other methods...

}