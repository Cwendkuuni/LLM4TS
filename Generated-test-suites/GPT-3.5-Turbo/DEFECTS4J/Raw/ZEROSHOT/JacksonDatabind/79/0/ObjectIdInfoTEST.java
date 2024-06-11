package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, alwaysAsId, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        ObjectIdInfo updatedInfo = info.withAlwaysAsId(true);

        assertEquals(name, updatedInfo.getPropertyName());
        assertEquals(scope, updatedInfo.getScope());
        assertEquals(gen, updatedInfo.getGeneratorType());
        assertEquals(resolver, updatedInfo.getResolverType());
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}