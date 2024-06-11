import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testEmptyArrayDeserialization() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[ ]");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 assertEquals(Collections.emptyList(), collectionDeserializer.deserialize(jsonParser, null));
 }

 @Test
 public void testSingleValueDeserialization() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"abc\"");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 assertEquals(Collections.singletonList("abc"), collectionDeserializer.deserialize(jsonParser, null));
 }

 @Test
 public void testNullValueDeserialization() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("null");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 assertEquals(Collections.singletonList(null), collectionDeserializer.deserialize(jsonParser, null));
 }

 @Test
 public void testArrayOfValuesDeserialization() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 assertEquals(Arrays.asList(1, 2, 3), collectionDeserializer.deserialize(jsonParser, null));
 }

 @Test
 public void testArrayOfObjectsDeserialization() throws IOException {
 Class<?> objectClass = Object.class;
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"name\":\"Object1\"}, {\"name\":\"Object2\"}]");
 JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass);
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 List<Object> result = collectionDeserializer.deserialize(jsonParser, null);
 for (Object obj : result) {
 ObjectNameAssert.assertObjectName((Map<String, Object>) obj, "name");
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJsonStructureDeserialization() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"Object\"}");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 jsonParser.nextToken(); // consume START_OBJECT token
 throw new AssertionError("Should throw JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeDeserializer() throws IOException {
 ObjectMapper objectMapperWithMissingTypeDeserializer = new ObjectMapper();
 JsonParser jsonParser = objectMapperWithMissingTypeDeserializer.getFactory().createParser("[1, 2]");
 CollectionDeserializer collectionDeserializer = objectMapperWithMissingTypeDeserializer.readValue(jsonParser, CollectionDeserializer.class);
 jsonParser.nextToken(); // consume START_ARRAY token
 assertEquals(JsonMappingException.class, jsonParser.getCurrentToken().getException().getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithUnknownProperty() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"unknown\":1}]");
 CollectionDeserializer collectionDeserializer = objectMapper.readValue(jsonParser, CollectionDeserializer.class);
 jsonParser.nextToken(); // consume START_ARRAY token
 jsonParser.nextToken(); // consume VALUE_NUMBER_INT token
 throw new AssertionError("Should throw JsonMappingException");
 }
}