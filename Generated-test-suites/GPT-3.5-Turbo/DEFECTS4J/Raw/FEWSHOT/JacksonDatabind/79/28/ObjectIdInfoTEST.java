package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        String name = "name";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor4() {
        PropertyName prop = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId);

        assertEquals(prop, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName prop = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId);

        assertTrue(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);

        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName prop = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId);

        String expected = "ObjectIdInfo: propName=name, scope=java.lang.Object, " +
                          "generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";

        assertEquals(expected, objectIdInfo.toString());
    }
}