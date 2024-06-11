package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

private SimpleAbstractTypeResolver resolver;

@Before
public void setUp() {
resolver = new SimpleAbstractTypeResolver();
}

@Test
public void testAddMapping() {
Class<?> superType = List.class;
Class<?> subType = LinkedList.class;

resolver.addMapping(superType, subType);

// verify that the mapping was added correctly
Class<?> resolvedSubType = resolver._mappings.get(new ClassKey(superType));
assertNotNull(resolvedSubType);
assertTrue(superType.isAssignableFrom(resolvedSubType));
assertEquals(subType, resolvedSubType);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_sameClass() {
Class<?> superType = List.class;

// trying to map a class to itself should throw an exception
resolver.addMapping(superType, superType);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_notSubtype() {
Class<?> superType = List.class;
Class<?> subType = LinkedList.class;
Class<?> notSubType = String.class;

// trying to map a non-subtype should throw an exception
resolver.addMapping(superType, notSubType);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_notAbstract() {
Class<?> concreteType = ArrayList.class;
Class<?> subType = LinkedList.class;

// trying to map a non-abstract class should throw an exception
resolver.addMapping(concreteType, subType);
}

@Test
public void testFindTypeMapping_mappingExists() {
Class<?> superType = List.class;
Class<?> subType = LinkedList.class;

resolver.addMapping(superType, subType);

// create a JavaType with the superType
JavaType type = TypeFactory.defaultInstance().constructType(superType);

// find the type mapping
JavaType resolvedType = resolver.findTypeMapping(config, type);

// verify that the type mapping was found and is correct
assertEquals(type.narrowBy(dst), resolvedType);
}

@Test
public void testFindTypeMapping_mappingDoesNotExist() {
Class<?> superType = List.class;

// create a JavaType with the superType
JavaType type = TypeFactory.defaultInstance().constructType(superType);

// find the type mapping
JavaType resolvedType = resolver.findTypeMapping(config, type);

// verify that the type mapping was not found
assertNull(resolvedType);
}

@Test
public void testResolveAbstractType() {
Class<?> superType = List.class;
Class<?> subType = LinkedList.class;

resolver.addMapping(superType, subType);

// create a JavaType with the superType
JavaType type = TypeFactory.defaultInstance().constructType(superType);

// resolve the abstract type
JavaType resolvedType = resolver.resolveAbstractType(config, type);

// verify that the abstract type was resolved correctly
assertNull(resolvedType);
}

}