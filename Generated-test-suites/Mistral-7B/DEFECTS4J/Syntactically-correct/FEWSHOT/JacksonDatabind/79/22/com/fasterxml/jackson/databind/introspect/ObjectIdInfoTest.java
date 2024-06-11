package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.MyCustomIdGenerator, alwaysAsId=true", withAlwaysAsId.toString());
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.MyCustomIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithNullProperties() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false, null);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
    }

    @Test
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, null, false, null);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=null, alwaysAsId=true", objectIdInfoWithAlwaysId.toString());
        assertNull(objectIdInfo.getGeneratorType());
    }

    @Test
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.MyCustomIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysId.toString());
        assertNotNull(objectIdInfo.getResolverType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());

        ObjectIdInfo objectIdInfoWithCustomResolver = objectIdInfo.withResolver(MyCustomResolver.class);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.MyCustomIdGenerator, alwaysAsId=false, resolverType=class com.example.MyCustomResolver", objectIdInfoWithCustomResolver.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false, null);
        objectIdInfo.withAlwaysAsId(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propName = new PropertyName("name");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, null, null, false, null);
        objectIdInfo.withAlwaysAsId(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGeneratorType() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, null, false, null);
        objectIdInfo.withAlwaysAsId(true);
    }
}

class MyCustomIdGenerator {}
class MyCustomResolver implements ObjectIdResolver {}