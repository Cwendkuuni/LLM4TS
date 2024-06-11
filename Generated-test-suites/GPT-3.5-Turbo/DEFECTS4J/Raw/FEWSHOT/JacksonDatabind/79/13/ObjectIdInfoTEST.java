package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdInfoTest {
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = MyClass.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = MyClass.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator, resolver, true);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = MyClass.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generator, resolver, false);

        assertEquals(propertyName, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(objectIdInfo.getPropertyName(), updatedInfo.getPropertyName());
        assertEquals(objectIdInfo.getScope(), updatedInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), updatedInfo.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), updatedInfo.getResolverType());
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=id, scope=com.fasterxml.jackson.databind.introspect.MyClass, generatorType=com.fasterxml.jackson.databind.introspect.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("id", propertyName.getSimpleName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(MyClass.class, scope);
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        assertEquals(MyObjectIdGenerator.class, generatorType);
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
        assertEquals(MyObjectIdResolver.class, resolverType);
    }

    @Test
    public void testGetAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
}