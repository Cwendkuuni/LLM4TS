package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JavaUtilCollectionsDeserializersTest {

 private final ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(set), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(list), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_SingletonMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} ;
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(map), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 Set<Boolean> set = Collections.unmodifiableSet(new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(set), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<Boolean> list = Collections.unmodifiableList(new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(list), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws Exception {
 Map<String, Boolean> map = Collections.unmodifiableMap(new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} );
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(map), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForCollection_EmptyList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>();
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(list), javaType);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFindForCollection_EmptySet() throws Exception {
 Set<Boolean> set = new HashSet<Boolean>();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(set), javaType);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFindForCollection_EmptyMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>();
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(map), javaType);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFindForCollection_Null() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.context(), javaType);

 Object deserialized = deserializer.deserialize(null, javaType);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>() {{
 put("a", Boolean.TRUE);
 }} ;
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(map), javaType);
 assertEquals(Boolean.TRUE, deserialized);
 }

 @Test
 public void testFindForMap_EmptyMap() throws Exception {
 Map<String, Boolean> map = new HashMap<String, Boolean>();
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

 Object deserialized = deserializer.deserialize(objectMapper.writeValueAsBytes(map), javaType);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFindForMap_Null() throws Exception {
 JavaType javaType = new TypeReference<Map<String,Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.context(), javaType);

 Object deserialized = deserializer.deserialize(null, javaType);
 assertEquals(null, deserialized);
 }
}