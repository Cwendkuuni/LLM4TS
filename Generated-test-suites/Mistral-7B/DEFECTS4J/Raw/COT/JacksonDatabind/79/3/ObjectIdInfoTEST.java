package com.example.objectidinfo;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameScopeGeneratorResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameScopeGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameScopeGeneratorResolver() {
        String name = "testProperty";
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<?> nullGenerator = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, nullGenerator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<?> nullResolver = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, nullResolver);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        String expected = "ObjectIdInfo: propName=propName, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}