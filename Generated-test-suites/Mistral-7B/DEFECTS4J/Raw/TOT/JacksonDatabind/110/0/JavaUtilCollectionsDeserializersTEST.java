package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() throws Exception {
 mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(List.class, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(List.class)));
 module.addDeserializer(Set.class, JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(Set.class)));
 module.addDeserializer(Map.class, JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(Map.class)));
 mapper.registerModule(module);
 mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testDeserializeSingletonList() throws JsonMappingException {
 List<Integer> list = mapper.readValue("[1]", List.class);
 assertEquals(Collections.singletonList(1), list);
 }

 @Test
 public void testDeserializeSingletonSet() throws JsonMappingException {
 Set<Integer> set = mapper.readValue("[1]", Set.class);
 assertEquals(Collections.singleton(1), set);
 }

 @Test
 public void testDeserializeSingletonMap() throws JsonMappingException {
 Map<String, Integer> map = mapper.readValue("{\"key\":1}", Map.class);
 assertEquals(Collections.singletonMap("key", 1), map);
 }

 @Test
 public void testDeserializeUnmodifiableList() throws JsonMappingException {
 List<Integer> list = mapper.readValue("[1]", List.class);
 List<Integer> unmodifiableList = Collections.unmodifiableList(list);
 assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(List.class)).deserialize(mapper, null, unmodifiableList.getClass()));
 }

 @Test
 public void testDeserializeUnmodifiableSet() throws JsonMappingException {
 Set<Integer> set = mapper.readValue("[1]", Set.class);
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(set);
 assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(Set.class)).deserialize(mapper, null, unmodifiableSet.getClass()));
 }

 @Test
 public void testDeserializeUnmodifiableMap() throws JsonMappingException {
 Map<String, Integer> map = mapper.readValue("{\"key\":1}", Map.class);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);
 assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), TypeFactory.defaultInstance().constructType(Map.class)).deserialize(mapper, null, unmodifiableMap.getClass()));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidCollectionType() throws JsonMappingException {
 mapper.readValue("[1, 2]", Integer.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyCollectionType() throws JsonMappingException {
 mapper.readValue("[]", List.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyMapType() throws JsonMappingException {
 mapper.readValue("{}", Map.class);
 }
}