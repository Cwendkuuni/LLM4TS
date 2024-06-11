package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = LinkedList.class;

        resolver.addMapping(abstractType, concreteType);
        assertEquals(concreteType, resolver._mappings.get(new ClassKey(abstractType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelfType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> selfType = Collection.class;

        resolver.addMapping(selfType, selfType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> invalidSubtype = String.class;

        resolver.addMapping(abstractType, invalidSubtype);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> nonAbstractSuperType = List.class;

        resolver.addMapping(nonAbstractSuperType, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = LinkedList.class;

        resolver.addMapping(abstractType, concreteType);

        JavaType javaType = TypeFactory.defaultInstance().constructType(abstractType);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(concreteType);

        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMappingEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;

        JavaType javaType = TypeFactory.defaultInstance().constructType(abstractType);
        JavaType expectedType = null;

        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testMultipleMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> collectionType = LinkedList.class;
        Class<?> listType = ArrayList.class;

        resolver.addMapping(abstractType, collectionType);
        resolver.addMapping(abstractType, listType);

        JavaType javaType = TypeFactory.defaultInstance().constructType(abstractType);
        JavaType expectedType = null; // Neither LinkedList nor ArrayList is more specific than Collection

        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
        assertEquals(expectedType, actualType);

        expectedType = TypeFactory.defaultInstance().constructType(collectionType);
        JavaType javaTypeForCollection = TypeFactory.defaultInstance().constructType(collectionType);
        JavaType javaTypeForList = TypeFactory.defaultInstance().constructType(listType);

        JavaType actualTypeForCollection = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaTypeForCollection);
        assertEquals(expectedType, actualTypeForCollection);

        JavaType actualTypeForList = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaTypeForList);
        assertEquals(expectedType, actualTypeForList);
    }
}