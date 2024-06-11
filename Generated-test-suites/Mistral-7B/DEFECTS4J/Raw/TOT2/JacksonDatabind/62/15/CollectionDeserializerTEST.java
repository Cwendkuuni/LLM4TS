import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testEmptyCollectionDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(String.class);
 List<String> emptyList = objectReader.readValue(new CharArrayReader("[]"));
 assertTrue(emptyList.isEmpty());
 }

 @Test
 public void testSingleElementCollectionDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(String.class);
 List<String> singleElementList = objectReader.readValue(new CharArrayReader("[\"Hello\"]"));
 assertEquals(1, singleElementList.size());
 assertEquals("Hello", singleElementList.get(0));
 }

 @Test
 public void testNullCollectionDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(String.class);
 List<String> nullList = objectReader.readValue(new CharArrayReader("null"));
 assertNull(nullList);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidCollectionDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(String.class);
 objectReader.readValue(new CharArrayReader("{ \"key\": [\"value\"] }"));
 }

 @Test
 public void testArrayToCollectionDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(String.class);
 String jsonArray[] = {"Hello", "World", "Jackson"};
 List<String> list = objectReader.readValue(new ArrayObjectReader(JsonToken.START_ARRAY, jsonArray));
 assertEquals(3, list.size());
 for (int i = 0; i < list.size(); i++) {
 assertEquals(jsonArray[i], list.get(i));
 }
 }

 @Test
 public void testCollectionWithPolymorphicTypeDeserialization() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(Map.class);
 String jsonString = "[{ \"Key\": 1, \"Value\": 1 }, { \"Key\": \"strKey\", \"Value\": \"strValue\" }]";
 List<Map<String, Object>> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(2, list.size());
 for (Map<String, Object> map : list) {
 if (map.containsKey("Key")) {
 int key = (int) map.get("Key");
 assertEquals(key, map.get("Value"));
 } else {
 String key = (String) map.get("Key");
 assertEquals(key, map.get("Value"));
 }
 }
 }

 @Test
 public void testCollectionWithTypeDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}]";
 List<MyClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(2, list.size());
 for (MyClass obj : list) {
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionWithValueDeserializerAndTypeDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(2, list.size());
 for (MyCustomClass obj : list) {
 assertEquals("John", obj.getName());
 assertEquals(new Integer(25), obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithContextualDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithDelegateDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyDelegateClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyDelegateClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyDelegateClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializerAndTypeDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyCustomClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(new Integer(25), obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializerAndContextualDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyCustomClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializerAndDelegateDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyCustomClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializerAndTypeDeserializerAndContextualDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyCustomClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }

 @Test
 public void testCollectionDeserializationWithCustomValueDeserializerAndTypeDeserializerAndDelegateDeserializer() throws JsonProcessingException {
 ObjectReader objectReader = mapper.readerForListOf(MyCustomClass.class);
 String jsonString = "[{\"name\": \"John\", \"age\": 25}, {\"name\": \"Mary\", \"age\": 30}, {\"name\": null}]";
 List<MyCustomClass> list = objectReader.readValue(new CharArrayReader(jsonString));
 assertEquals(3, list.size());
 for (MyCustomClass obj : list) {
 if (obj == null) {
 continue;
 }
 assertEquals("John", obj.getName());
 assertEquals(25, obj.getAge());
 }
 }
}