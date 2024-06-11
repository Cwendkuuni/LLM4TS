package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @SuppressWarnings("unchecked")
 @Test
 public void testSimpleAbstractTypeResolverInstance() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();
 assertNotNull(testInstance);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAddMapping() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 Class<List> listClass = List.class;
 Class<LinkedList> linkedListClass = LinkedList.class;
 Class<Map> mapClass = Map.class;
 Class<HashMap> hashMapClass = HashMap.class;

 // Test addMapping with Collection and LinkedList
 testInstance.addMapping(Collection.class, LinkedList.class);
 testInstance.addMapping(List.class, LinkedList.class);
 Map<ClassKey, Class<?>> mappings = testInstance._mappings;
 assertEquals(2, mappings.size());
 assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
 assertTrue(mappings.containsKey(new ClassKey(List.class)));

 // Test addMapping with Map and HashMap
 testInstance.addMapping(Map.class, HashMap.class);
 mappings = testInstance._mappings;
 assertEquals(3, mappings.size());
 assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
 assertTrue(mappings.containsKey(new ClassKey(List.class)));
 assertTrue(mappings.containsKey(new ClassKey(Map.class)));

 // Test addMapping with custom classes
 Class<MyCollection> myCollectionClass = MyCollection.class;
 Class<MyLinkedList> myLinkedListClass = MyLinkedList.class;
 testInstance.addMapping(Collection.class, MyLinkedList.class);
 testInstance.addMapping(List.class, MyLinkedList.class);
 testInstance.addMapping(Collection.class, MyCollection.class);
 testInstance.addMapping(List.class, MyCollection.class);
 mappings = testInstance._mappings;
 assertEquals(5, mappings.size());
 assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
 assertTrue(mappings.containsKey(new ClassKey(List.class)));
 assertTrue(mappings.containsKey(new ClassKey(Map.class)));
 assertTrue(mappings.containsKey(new ClassKey(MyCollection.class)));
 assertTrue(mappings.containsKey(new ClassKey(MyLinkedList.class)));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindTypeMapping() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 testInstance.addMapping(Collection.class, LinkedList.class);
 testInstance.addMapping(List.class, LinkedList.class);
 testInstance.addMapping(Map.class, HashMap.class);

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType listType = typeFactory.constructType(List.class);
 JavaType mapType = typeFactory.constructType(Map.class);

 Class<List> listClass = List.class;
 Class<LinkedList> linkedListClass = LinkedList.class;
 Class<Map> mapClass = Map.class;
 Class<HashMap> hashMapClass = HashMap.class;

 JavaType resolvedType = testInstance.findTypeMapping(config, listType);
 assertEquals(linkedListClass, resolvedType.getRawClass());

 resolvedType = testInstance.findTypeMapping(config, mapType);
 assertEquals(hashMapClass, resolvedType.getRawClass());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 testInstance.addMapping(Collection.class, LinkedList.class);
 testInstance.addMapping(List.class, LinkedList.class);
 testInstance.addMapping(Map.class, HashMap.class);
 testInstance.addMapping(Collection.class, ArrayList.class); // Invalid mapping

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType listType = typeFactory.constructType(List.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, listType);
 assertNull(resolvedType);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithIncompatibleTypes() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 // Test addMapping with incompatible types
 testInstance.addMapping(Collection.class, String.class);
 testInstance.addMapping(List.class, String.class);
 testInstance.addMapping(Map.class, String.class);

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType listType = typeFactory.constructType(List.class);
 JavaType mapType = typeFactory.constructType(Map.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, listType);
 assertNull(resolvedType);

 resolvedType = testInstance.findTypeMapping(config, mapType);
 assertNull(resolvedType);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSelfMapping() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 // Test addMapping with self mapping
 testInstance.addMapping(Collection.class, Collection.class);
 testInstance.addMapping(List.class, List.class);
 testInstance.addMapping(Map.class, Map.class);

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType listType = typeFactory.constructType(List.class);
 JavaType mapType = typeFactory.constructType(Map.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, listType);
 assertNull(resolvedType);

 resolvedType = testInstance.findTypeMapping(config, mapType);
 assertNull(resolvedType);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithCyclicMapping() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 // Test addMapping with cyclic mapping
 testInstance.addMapping(Collection.class, LinkedList.class);
 testInstance.addMapping(List.class, Collection.class); // Cyclic mapping
 testInstance.addMapping(Map.class, HashMap.class);

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType listType = typeFactory.constructType(List.class);
 JavaType mapType = typeFactory.constructType(Map.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, listType);
 assertNull(resolvedType);

 resolvedType = testInstance.findTypeMapping(config, mapType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 testInstance.addMapping(Collection.class, LinkedList.class);
 testInstance.addMapping(List.class, LinkedList.class);
 testInstance.addMapping(Map.class, HashMap.class);

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType nullType = null;

 JavaType resolvedType = testInstance.findTypeMapping(config, nullType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingForClassWithoutMappings() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType stringType = typeFactory.constructType(String.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, stringType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithUnknownSuperClass() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType unknownClassType = typeFactory.constructType(UnknownClass.class);

 JavaType resolvedType = testInstance.findTypeMapping(config, unknownClassType);
 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractTypeWithNullType() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 JavaType resolvedType = testInstance.resolveAbstractType(new DeserializationConfig(), null);
 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractTypeWithUnknownType() {
 Class<SimpleAbstractTypeResolver> resolverClass = SimpleAbstractTypeResolver.class;
 SimpleAbstractTypeResolver testInstance = (SimpleAbstractTypeResolver) resolverClass.newInstance();

 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaTypeFactory typeFactory = mapper.getTypeFactory();
 JavaType unknownType = typeFactory.constructType(UnknownClass.class);

 JavaType resolvedType = testInstance.resolveAbstractType(config, unknownType);
 assertNull(resolvedType);
 }
}

class MyCollection<T> extends ArrayList<T> {}
class MyLinkedList<T> extends LinkedList<T> {}
class UnknownClass {
 static int i;
}