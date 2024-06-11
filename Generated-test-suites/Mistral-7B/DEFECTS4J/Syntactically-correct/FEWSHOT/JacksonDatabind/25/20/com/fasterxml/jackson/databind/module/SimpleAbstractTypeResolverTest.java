package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = config.constructType(Collection.class);

        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_InvalidSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = config.constructType(LinkedList.class);

        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testFindTypeMapping_InvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType javaType = config.constructType(ArrayList.class);

        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testFindTypeMapping_NonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType javaType = config.constructType(ArrayList.class);

        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(config, javaType));
    }
}