package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithProperties() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectInfo.getPropertyName());
        assertEquals(scope, objectInfo.getScope());
        assertEquals(generator, objectInfo.getGeneratorType());
        assertEquals(resolver, objectInfo.getResolverType());
        assertEquals(false, objectInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithDeprecatedProperties() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectInfo.getPropertyName());
        assertEquals(scope, objectInfo.getScope());
        assertEquals(generator, objectInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectInfo.getResolverType());
        assertEquals(false, objectInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringProperty() {
        String propertyName = "name";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(new PropertyName(propertyName), objectInfo.getPropertyName());
        assertEquals(scope, objectInfo.getScope());
        assertEquals(generator, objectInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectInfo.getResolverType());
        assertEquals(false, objectInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo updatedObjectInfo = objectInfo.withAlwaysAsId(true);

        assertEquals(propertyName, updatedObjectInfo.getPropertyName());
        assertEquals(scope, updatedObjectInfo.getScope());
        assertEquals(generator, updatedObjectInfo.getGeneratorType());
        assertEquals(resolver, updatedObjectInfo.getResolverType());
        assertEquals(true, updatedObjectInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        ObjectIdInfo objectInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(expected, objectInfo.toString());
    }
}