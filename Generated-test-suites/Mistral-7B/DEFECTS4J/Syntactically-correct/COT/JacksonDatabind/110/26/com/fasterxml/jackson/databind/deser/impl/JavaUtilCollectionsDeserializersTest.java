import com.fasterxml.jackson.core.base.Converter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

 private JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 List list = Arrays.asList(1, 2, 3);
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 assertEquals(List.class, deserializer.getClass());

 List deserializedList = deserializer.deserialize(mockContext(), "[]");
 assertEquals(3, deserializedList.size());
 assertEquals(1, deserializedList.get(0));
 }

 @Test
 public void testFindForCollection_SingletonList_Empty() throws JsonMappingException {
 List list = Collections.singletonList(1);
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 assertEquals(List.class, deserializer.getClass());

 List deserializedList = deserializer.deserialize(mockContext(), "[]");
 assertEquals(0, deserializedList.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnsupportedType() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 fail("Unsupported type should not return deserializer");
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 Map map = new HashMap() {{
 put("key", "value");
 }} ;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 JsonDeserializer deserializer = deserializers.findForMap(mockContext(), javaType);
 assertEquals(Map.class, deserializer.getClass());

 Map deserializedMap = deserializer.deserialize(mockContext(), "{}");
 assertEquals(1, deserializedMap.size());
 assertEquals("value", deserializedMap.get("key"));
 }

 @Test
 public void testFindForMap_UnsupportedType() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 JsonDeserializer deserializer = deserializers.findForMap(mockContext(), javaType);
 fail("Unsupported type should not return deserializer");
 }

 @Test
 public void testConverter_SingletonSet() throws JsonMappingException {
 Set set = Collections.singleton(1);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Set.class);
 assertEquals(Set.class, converter.getClass());

 Object deserializedSet = converter.deserialize(mockContext(), "1");
 assertInstanceOf(Integer.class, deserializedSet);
 }

 @Test(expected = JsonMappingException.class)
 public void testConverter_EmptySet() throws JsonMappingException {
 Set set = Collections.emptySet();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Set.class);
 fail("Empty set should not be deserialized");
 }

 @Test
 public void testConverter_UnmodifiableSet() throws JsonMappingException {
 Set set = Collections.unmodifiableSet(Collections.singleton(1));
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Set.class);
 assertEquals(Set.class, converter.getClass());

 Object deserializedSet = converter.deserialize(mockContext(), "1");
 assertInstanceOf(Integer.class, deserializedSet);
 assertTrue(deserializedSet instanceof Collections.UnmodifiableSet);
 }

 @Test
 public void testConverter_UnsupportedType() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Set.class);
 fail("Unsupported type should not return converter");
 }

 @Test
 public void testConverter_UnmodifiableList() throws JsonMappingException {
 List list = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JsonDeserializer deserializer = deserializers.findForCollection(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, List.class);
 assertEquals(List.class, converter.getClass());

 Object deserializedList = converter.deserialize(mockContext(), "[1,2,3]");
 assertInstanceOf(List.class, deserializedList);
 assertTrue(deserializedList instanceof Collections.UnmodifiableList);
 }

 @Test
 public void testConverter_UnmodifiableMap() throws JsonMappingException {
 Map map = Collections.unmodifiableMap(new HashMap() {{
 put("key", "value");
 }} );
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 JsonDeserializer deserializer = deserializers.findForMap(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Map.class);
 assertEquals(Map.class, converter.getClass());

 Object deserializedMap = converter.deserialize(mockContext(), "{\"key\":\"value\"}");
 assertInstanceOf(Map.class, deserializedMap);
 assertTrue(deserializedMap instanceof Collections.UnmodifiableMap);
 }

 @Test
 public void testConverter_SingletonMap() throws JsonMappingException {
 Map map = new HashMap() {{
 put("key", "value");
 }} ;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 JsonDeserializer deserializer = deserializers.findForMap(mockContext(), javaType);
 JsonDeserializer converter = deserializers.converter(deserializer.getId(), javaType, Map.class);
 assertEquals(Map.class, converter.getClass());

 Object deserializedMap = converter.deserialize(mockContext(), "{\"key\":\"value\"}");
 assertInstanceOf(HashMap.class, deserializedMap);
 assertEquals(1, deserializedMap.size());
 assertEquals("value", deserializedMap.get("key"));
 }

 private DeserializationContext mockContext() {
 return new TestDeserializationContext();
 }

}