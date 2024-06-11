package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(propertyName, scope, generator, true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=true", objectIdIdInfoWithAlwaysAsId.toString());
    }

    @Test
    public void testObjectIdInfoWithAnnotation() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=true", objectIdIdInfoWithAlwaysAsId.toString());
    }

    @Test
    public void testObjectIdInfoWithDefaultResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals("ObjectIdInfo: propName=name, scope=null, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithNullArguments() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        assertNull(objectIdInfo);
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.CustomObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
    }
}