package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.ClassIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, generatorType, false, resolverType);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$ClassIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithAlwaysAsIdTrue() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.ClassIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, generatorType, true, resolverType);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$ClassIdGenerator, alwaysAsId=true", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithNullPropertyName() {
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.ClassIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, generatorType, false, resolverType);
        assertNull(objectIdInfo);
    }

    @Test
    public void testObjectIdInfoConstructionWithNullScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.ClassIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generatorType, false, resolverType);
        assertEquals("ObjectIdInfo: propName=name, scope=null, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$ClassIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.ClassIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, generatorType, false, resolverType);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$ClassIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(objectIdInfo.getScope(), objectIdInfoWithAlwaysAsId.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), objectIdInfoWithAlwaysAsId.getResolverType());
    }
}