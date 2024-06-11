package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;
        PropertyName propertyName = new PropertyName("test");

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        Class<?> scope = Integer.class;
        Class<?> generator = ObjectIdGenerator.class;
        PropertyName propertyName = new PropertyName("test");

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAnnotationObject() {
        Class<?> scope = Object.class;
        Class<?> generator = ObjectIdGenerator.class;
        PropertyName propertyName = new PropertyName("test");

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        Class<?> scope = Long.class;
        Class<?> generator = ObjectIdGenerator.class;
        PropertyName propertyName = new PropertyName("test");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        objectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        Class<?> scope = Float.class;
        Class<?> generator = ObjectIdGenerator.class;
        PropertyName propertyName = new PropertyName("test");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.Float, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}