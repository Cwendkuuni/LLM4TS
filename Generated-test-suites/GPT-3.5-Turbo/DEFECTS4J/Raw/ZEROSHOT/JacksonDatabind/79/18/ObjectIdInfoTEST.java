package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructor() {
        String nameStr = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(nameStr, scope, generator);

        assertEquals(new PropertyName(nameStr), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver).withAlwaysAsId(alwaysAsId);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        String expected = "ObjectIdInfo: propName=" + name + ", scope=" + scope.getName()
                + ", generatorType=" + generator.getName() + ", alwaysAsId=false";

        assertEquals(expected, objectIdInfo.toString());
    }
}