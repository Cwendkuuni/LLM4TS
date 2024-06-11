package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 // Add mappings for Collection and List to be of LinkedList type
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 // Test with Collection type
 JavaType type1 = new TypeReference<List<String>>(){}.getType();
 JavaType expectedType1 = new TypeReference<LinkedList<String>>(){}.getType();
 JavaType actualType1 = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), type1);
 assertEquals(expectedType1, actualType1);

 // Test with List type
 JavaType type2 = new TypeReference<List<Integer>>(){}.getType();
 JavaType expectedType2 = new TypeReference<LinkedList<Integer>>(){}.getType();
 JavaType actualType2 = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), type2);
 assertEquals(expectedType2, actualType2);

 // Test with non-abstract type
 JavaType type3 = Integer.TYPE;
 JavaType expectedType3 = null;
 JavaType actualType3 = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), type3);
 assertNull(actualType3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidArguments() {
 // Test with same class
 resolver.addMapping(LinkedList.class, LinkedList.class);

 // Test with non-abstract super type
 resolver.addMapping(ArrayList.class, LinkedList.class);
 }

 @Test
 public void testResolveAbstractType() {
 // Test with non-abstract type
 JavaType type = Integer.TYPE;
 JavaType expectedType = null;
 JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), type);
 assertNull(actualType);

 // Test with Collection type
 JavaType typeCollection = new TypeReference<List<String>>(){}.getType();
 JavaType expectedTypeCollection = null;
 JavaType actualTypeCollection = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), typeCollection);
 assertNull(actualTypeCollection);

 // Test with List type
 JavaType typeList = new TypeReference<List<Integer>>(){}.getType();
 JavaType expectedTypeList = null;
 JavaType actualTypeList = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), typeList);
 assertNull(actualTypeList);

 // Test with LinkedList type (should not be called as it's abstract)
 JavaType typeLinkedList = LinkedList.class;
 JavaType expectedTypeLinkedList = null;
 JavaType actualTypeLinkedList = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), typeLinkedList);
 assertNull(actualTypeLinkedList);
 }
}