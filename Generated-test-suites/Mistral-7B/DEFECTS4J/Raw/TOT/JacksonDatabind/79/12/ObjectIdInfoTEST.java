package com.fasterxml.jackson.databind.introspect.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoWithPropertyNameAndScope() {
        PropertyName propertyName = new PropertyName("myProperty");
        Class<String> scope = String.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, null, false, null);

        assertEquals("ObjectIdInfo: propName=myProperty, scope=class java.lang.String, generatorType=null, alwaysAsId=false", objIdInfo.toString());
        assertNotNull(objIdInfo);
        assertNotNull(objIdInfo.getPropertyName());
        assertNotNull(objIdInfo.getScope());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithPropertyNameScopeAndObjectIdGenerator() {
        PropertyName propertyName = new PropertyName("myProperty");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> objectIdGenerator = MyObjectIdGenerator.class; // some valid implementation

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, false, null);

        assertEquals("ObjectIdInfo: propName=myProperty, scope=class java.lang.String, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false", objIdInfo.toString());
        assertNotNull(objIdInfo);
        assertNotNull(objIdInfo.getPropertyName());
        assertNotNull(objIdInfo.getScope());
        assertNotNull(objIdInfo.getGeneratorType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithValidArguments() {
        PropertyName propertyName = new PropertyName("myProperty");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> objectIdGenerator = null; // or some valid implementation
        Class<? extends ObjectIdResolver> objectIdResolver = null; // or some valid implementation
        boolean alwaysAsId = false;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, alwaysAsId, objectIdResolver);

        assertEquals("ObjectIdInfo: propName=myProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, resolverType=class com.fasterxml.jackson.databind.SimpleObjectIdResolver, alwaysAsId=false", objIdInfo.toString());
        assertNotNull(objIdInfo);
        assertNotNull(objIdInfo.getPropertyName());
        assertNotNull(objIdInfo.getScope());
        assertNotNull(objIdInfo.getGeneratorType());
        assertNull(objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> objectIdGenerator = null; // or some valid implementation
        Class<? extends ObjectIdResolver> objectIdResolver = null; // or some valid implementation
        boolean alwaysAsId = false;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(null, scope, objectIdGenerator, alwaysAsId, objectIdResolver);

        fail("Expected IllegalArgumentException but it was not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("myProperty");
        Class<? extends ObjectIdGenerator<?>> objectIdGenerator = null; // or some valid implementation
        Class<? extends ObjectIdResolver> objectIdResolver = null; // or some valid implementation
        boolean alwaysAsId = false;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, null, objectIdGenerator, alwaysAsId, objectIdResolver);

        fail("Expected IllegalArgumentException but it was not thrown");
    }
}