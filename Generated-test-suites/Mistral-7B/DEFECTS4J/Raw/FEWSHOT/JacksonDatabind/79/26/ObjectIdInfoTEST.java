package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, false, resolverType);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.json.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.json.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyName() {
        assertNull(new ObjectIdInfo(null, null, null, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullGeneratorType() {
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, null, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullResolverType() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        assertNull(new ObjectIdInfo(propertyName, null, generatorType, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        assertNull(new ObjectIdInfo(propertyName, null, generatorType, false, resolverType));
    }

    @Test
    public void testObjectIdInfoWithDeprecatedConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.json.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.json.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
    }
}