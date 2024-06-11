package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoWithDefaults() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithCustomName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("customName"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        objectIdInfo = objectIdInfo.withPropertyName(new PropertyName("newPropertyName"));
        assertEquals("ObjectIdInfo: propName=newPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithCustomScope() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), CustomScope.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class com.example.CustomScope, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithCustomGenerator() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), Object.class, CustomObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class java.lang.Object, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithCustomResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), Object.class, ObjectIdGenerator.class, false, CustomObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        objectIdInfo = objectIdInfo.withResolverType(AnotherCustomObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false, resolverType=class com.example.AnotherCustomObjectIdResolver", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=propertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        new ObjectIdInfo(new PropertyName("propertyName"), null, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGeneratorType() {
        new ObjectIdInfo(new PropertyName("propertyName"), Object.class, null, false, SimpleObjectIdResolver.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolverType() {
        new ObjectIdInfo(new PropertyName("propertyName"), Object.class, ObjectIdGenerator.class, false, null);
    }

    static class CustomScope {}
    static class CustomObjectIdGenerator implements ObjectIdGenerator<Object> {}
    static class CustomObjectIdResolver implements ObjectIdResolver {}
    static class AnotherCustomObjectIdResolver implements ObjectIdResolver {}

}