package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.*;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType listType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), listType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("[]"), List.class);
 Object output = mapper.readValue(mapper.createObjectText("[]"), Object.class, deserializer);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType setType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<String>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), setType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("[]"), Set.class);
 Object output = mapper.readValue(mapper.createObjectText("[]"), Object.class, deserializer);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType listType = new TypeReference<List<String>>() {}.getType();
 JavaType unmodifiableListType = new TypeReference<List<String>>() {}.getType().getComponentType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>() {}.getType().getComponentType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), inputType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("[\"foo\"]"), List.class);
 Object output = mapper.readValue(mapper.createObjectText("[\"foo\"]"), Object.class, deserializer);
 assertEquals(Collections.unmodifiableList(input), output);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType setType = new TypeReference<Set<String>>() {}.getType();
 JavaType unmodifiableSetType = setType.getComponentType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<String>>() {}.getType().getComponentType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), inputType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("[\"foo\"]"), Set.class);
 Object output = mapper.readValue(mapper.createObjectText("[\"foo\"]"), Object.class, deserializer);
 assertEquals(Collections.unmodifiableSet(input), output);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType mapType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String, String>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), mapType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("{\"key\":\"value\"}"), Map.class);
 Object output = mapper.readValue(mapper.createObjectText("{\"key\":\"value\"}"), Object.class, deserializer);
 assertEquals(input, output);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType mapType = new TypeReference<Map<String, String>>() {}.getType();
 JavaType unmodifiableMapType = mapType.getComponentType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(new TypeReference<Map.Entry<String, String>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), inputType);
 assertNotNull(deserializer);

 Object input = new ObjectMapper().readValue(mapper.createObjectText("{{\"key\":\"value\"}}"), Map.class);
 Object output = mapper.readValue(mapper.createObjectText("{{\"key\":\"value\"}}"), Object.class, deserializer);
 assertEquals(Collections.unmodifiableMap(input), output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<Integer>() {}.getType());
 JavaDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 fail("Should not find deserializer for Integer type");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<Integer>() {}.getType());
 JavaDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 fail("Should not find deserializer for Integer type");
 }

}