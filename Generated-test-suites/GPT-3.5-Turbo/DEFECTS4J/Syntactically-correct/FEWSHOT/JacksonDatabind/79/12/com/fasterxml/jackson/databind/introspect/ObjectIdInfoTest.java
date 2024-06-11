package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithProperties() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
        
        info = new ObjectIdInfo(propertyName, scope, generator, resolver, true);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertFalse(info.getAlwaysAsId());

        info = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringNameScopeAndGenerator() {
        String propertyName = "id";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(new PropertyName(propertyName), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertFalse(info.getAlwaysAsId());

        info = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals(new PropertyName(propertyName), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testGetterAndSetter() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());

        info = info.withAlwaysAsId(true);

        assertTrue(info.getAlwaysAsId());

        info = info.withAlwaysAsId(false);

        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator, resolver);

        String expectedToString = "ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        assertEquals(expectedToString, info.toString());
    }
}