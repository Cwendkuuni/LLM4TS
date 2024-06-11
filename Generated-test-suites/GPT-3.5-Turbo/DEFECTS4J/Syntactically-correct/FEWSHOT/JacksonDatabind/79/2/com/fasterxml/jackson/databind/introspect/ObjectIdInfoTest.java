package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
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
        assertNull(objectIdInfo.getResolverType());
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
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        String expected = "ObjectIdInfo: propName=name, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}