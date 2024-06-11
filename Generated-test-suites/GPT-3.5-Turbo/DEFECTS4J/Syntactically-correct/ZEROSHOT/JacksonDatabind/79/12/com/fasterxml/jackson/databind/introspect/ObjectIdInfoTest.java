package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);

        assertEquals("propertyName", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class);

        assertEquals("propertyName", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdInfo info = new ObjectIdInfo("propertyName", String.class, ObjectIdGenerator.class);

        assertEquals("propertyName", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class)
                .withAlwaysAsId(true);

        assertEquals("propertyName", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class);

        String expected = "ObjectIdInfo: propName=propertyName, scope=java.lang.String, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}