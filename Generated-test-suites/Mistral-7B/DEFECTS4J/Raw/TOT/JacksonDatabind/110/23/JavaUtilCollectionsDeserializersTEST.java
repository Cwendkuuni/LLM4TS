import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 List<Integer> expectedList = Arrays.asList(1, 2, 3);
 String json = mapper.writerWithFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(expectedList);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 List<Integer> actualList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForListOf(Integer.class).setDeserializer(deserializer);
 List<Integer> deserializedList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertEquals(expectedList, actualList);
 assertEquals(expectedList, deserializedList);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 List<Integer> unmodifiableList = Arrays.asList(1, 2, 3);
 List<Integer> expectedList = Collections.unmodifiableList(unmodifiableList);
 String json = mapper.writerWithFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(expectedList);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 List<Integer> actualList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForListOf(Integer.class).setDeserializer(deserializer);
 List<Integer> deserializedList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertEquals(expectedList, actualList);
 assertEquals(expectedList, deserializedList);
 }

 @Test
 public void testFindForCollectionEmptyList() throws Exception {
 String json = "[]";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 List<Integer> actualList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForListOf(Integer.class).setDeserializer(deserializer);
 List<Integer> deserializedList = mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertNull(actualList);
 assertNull(deserializedList);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionInvalidType() throws Exception {
 String json = "{\"type\":\"INVALID\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<java.util.List<Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 mapper.readerForListOf(Integer.class).setDeserializer(deserializer);
 mapper.readerForListOf(Integer.class).readValue(mapper.getBufferWriter(), javaType);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 Map<String, Integer> expectedMap = new HashMap<>();
 expectedMap.put("key1", 1);
 expectedMap.put("key2", 2);
 String json = mapper.writerWithFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(expectedMap);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 Map<String, Integer> actualMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForMapOf(String.class, Integer.class).setDeserializer(deserializer);
 Map<String, Integer> deserializedMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertEquals(expectedMap, actualMap);
 assertEquals(expectedMap, deserializedMap);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 Map<String, Integer> map = new HashMap<>();
 map.put("key1", 1);
 map.put("key2", 2);
 Map<String, Integer> expectedMap = Collections.unmodifiableMap(map);
 String json = mapper.writerWithFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(expectedMap);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 Map<String, Integer> actualMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForMapOf(String.class, Integer.class).setDeserializer(deserializer);
 Map<String, Integer> deserializedMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertEquals(expectedMap, actualMap);
 assertEquals(expectedMap, deserializedMap);
 }

 @Test
 public void testFindForMapEmptyMap() throws Exception {
 String json = "{}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 Map<String, Integer> actualMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 mapper.readerForMapOf(String.class, Integer.class).setDeserializer(deserializer);
 Map<String, Integer> deserializedMap = mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 assertNull(actualMap);
 assertNull(deserializedMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMapInvalidType() throws Exception {
 String json = "{\"type\":\"INVALID\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<java.util.Map<String, Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 mapper.readerForMapOf(String.class, Integer.class).setDeserializer(deserializer);
 mapper.readerForMapOf(String.class, Integer.class).readValue(mapper.getBufferWriter(), javaType);
 }

}