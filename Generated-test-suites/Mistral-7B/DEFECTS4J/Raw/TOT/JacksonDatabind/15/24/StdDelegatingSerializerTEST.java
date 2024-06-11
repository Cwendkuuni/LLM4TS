import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer<String> stringDelegateSerializer;
 private StdDelegatingSerializer<Integer> intDelegateSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringDelegateSerializer = new StdDelegatingSerializer<String>(
 String.class,
 Object::toString,
 ObjectMapper.getDefaultInstance().findValueSerializer(String.class)
 );
 intDelegateSerializer = new StdDelegatingSerializer<Integer>(
 Integer.class,
 Integer::toString,
 ObjectMapper.getDefaultInstance().findValueSerializer(Integer.class)
 );
 }

 @Test
 public void testStringSerialization() throws JsonProcessingException {
 ObjectNode stringNode = objectMapper.createObjectNode();
 stringNode.put("value", "Test String");
 JsonNode serializedString = objectMapper.writeValueAsTree(stringDelegateSerializer, "Test String");
 assertEquals(stringNode, serializedString);
 }

 @Test
 public void testStringSerializationWithType() throws JsonProcessingException {
 ObjectNode stringNode = objectMapper.createObjectNode();
 stringNode.put("value", "Test String");
 ObjectNode serializedString = objectMapper.valueToTree(stringDelegateSerializer, "Test String");
 assertEquals(stringNode, serializedString);
 }

 @Test
 public void testInvalidTypeConversion() {
 assertThrows(IllegalArgumentException.class, () -> new StdDelegatingSerializer<Object>(
 Object.class,
 Object::toString,
 ObjectMapper.getDefaultInstance().findValueSerializer(String.class)
 ));
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 ObjectNode nullNode = objectMapper.createObjectNode();
 JsonNode serializedNull = objectMapper.writeValueAsTree(stringDelegateSerializer, null);
 assertNull(serializedNull.get("value"));
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException, JsonGenerationException {
 ObjectNode intNode = objectMapper.createObjectNode();
 intNode.put("value", 123);
 ObjectNode serializedInt = objectMapper.writeValueAsTree(intDelegateSerializer, 123);
 assertEquals(intNode, serializedInt);
 }

 @Test
 public void testIntegerSerializationWithType() throws JsonProcessingException {
 ObjectNode intNode = objectMapper.createObjectNode();
 intNode.put("value", 123);
 ObjectNode serializedInt = objectMapper.valueToTree(intDelegateSerializer, 123);
 assertEquals(intNode, serializedInt);
 }

 @Test
 public void testInvalidConverter() {
 assertThrows(NullPointerException.class, () -> new StdDelegatingSerializer<Object>(
 Object.class,
 null,
 ObjectMapper.getDefaultInstance().findValueSerializer(String.class)
 ));
 }

 @Test
 public void testNullConverter() {
 assertThrows(NullPointerException.class, () -> new StdDelegatingSerializer<Object>(
 Object.class,
 null,
 null
 ));
 }

 @Test
 public void testNullDelegateSerializer() {
 assertThrows(NullPointerException.class, () -> new StdDelegatingSerializer<Object>(
 Object.class,
 Object::toString,
 null
 ));
 }

 @Test
 public void testSerializingNullDelegateSerializer() throws JsonProcessingException {
 ObjectNode nullDelegateSerializerNode = objectMapper.createObjectNode();
 JsonNode serializedNullDelegateSerializer = objectMapper.writeValueAsTree(intDelegateSerializer, null);
 assertNull(serializedNullDelegateSerializer.get("value"));
 }

 @Test
 public void testEmptyListSerialization() throws JsonProcessingException {
 ObjectNode emptyListNode = objectMapper.createObjectNode();
 emptyListNode.put("value", Collections.emptyList());
 ObjectNode serializedEmptyList = objectMapper.writeValueAsTree(
 new StdDelegatingSerializer<Object>(
 Object.class,
 Object::toString,
 ObjectMapper.getDefaultInstance().findValueSerializer(List.class)
 ), Collections.emptyList()
 );
 assertEquals(emptyListNode, serializedEmptyList);
 }
}