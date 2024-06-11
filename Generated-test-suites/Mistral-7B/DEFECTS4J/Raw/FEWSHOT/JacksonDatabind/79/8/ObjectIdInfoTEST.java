package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        String name = "name";
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructor4() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        boolean alwaysAsId = true;
        Class<CustomIdResolver> resolver = CustomIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=true, resolverType=class com.example.CustomIdResolver", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructor5() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=true, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        boolean initialState = false;
        boolean newState = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, initialState);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(newState);
        assertNotSame(objectIdInfo, newObjectIdInfo);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.example.CustomIdGenerator, alwaysAsId=true", newObjectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoGetters() {
        PropertyName propertyName = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(CustomIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(CustomIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
}