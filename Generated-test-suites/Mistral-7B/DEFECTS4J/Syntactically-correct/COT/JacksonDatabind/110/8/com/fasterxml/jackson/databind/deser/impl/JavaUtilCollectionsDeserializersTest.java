package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static Object mapSingleton(ObjectMapper mapper, Object input)
         throws JsonProcessingException {
      return mapper.findAndRegisterModules().findValue(new TypeReference<Object>() {}, input);
 }

 @org.junit.Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      List<Integer> list = Arrays.asList(1, 2, 3);
      JsonDeserializer<List<Integer>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(List.class, Integer.class));
      List<Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("[]"), deserializer);
      assertEquals(list, deserialized);

      // Test for edge case: empty list
      deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(List.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("[]"), deserializer);
      assertNull(deserialized);
   }

   @org.junit.Test
   public void testFindForCollection_SingletonSet() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      Set<Integer> set = Collections.singleton(1);
      JsonDeserializer<Set<Integer>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(Set.class, Integer.class));
      Set<Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertEquals(set, deserialized);

      // Test for edge case: empty set
      deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(Set.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertNull(deserialized);
   }

   @org.junit.Test
   public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      List<Integer> list = Arrays.asList(1, 2, 3);
      JsonDeserializer<List<Integer>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(List.class, Integer.class));
      List<Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("["+Arrays.toString(list.toArray())+"]"), deserializer);
      assertEquals(Collections.unmodifiableList(list), deserialized);

      // Test for edge case: empty list
      deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(List.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("[]"), deserializer);
      assertNull(deserialized);
   }

   @org.junit.Test
   public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      Set<Integer> set = Collections.singleton(1);
      JsonDeserializer<Set<Integer>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(Set.class, Integer.class));
      Set<Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{"+set.toString()+"}"), deserializer);
      assertEquals(Collections.unmodifiableSet(set), deserialized);

      // Test for edge case: empty set
      deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(),
              mapper.getTypeFactory().constructType(Set.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertNull(deserialized);
   }

   @org.junit.Test
   public void testFindForMap_SingletonMap() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Integer> map = Collections.singletonMap("key", 1);
      JsonDeserializer<Map<String, Integer>> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(),
              mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
      Map<String, Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertEquals(map, deserialized);

      // Test for edge case: empty map
      deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(),
              mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertNull(deserialized);
   }

   @org.junit.Test
   public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Integer> map = Collections.singletonMap("key", 1);
      JsonDeserializer<Map<String, Integer>> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(),
              mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
      Map<String, Integer> deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{"+map.toString()+"}"), deserializer);
      assertEquals(Collections.unmodifiableMap(map), deserialized);

      // Test for edge case: empty map
      deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(),
              mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
      deserialized = mapper.deserializeValue(mapper.getFactory().createParser("{}"), deserializer);
      assertNull(deserialized);
   }

}