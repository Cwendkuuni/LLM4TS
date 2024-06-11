package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringArrayDeserializerTest {

 @BeforeAll
 static void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(String[].class, StringArrayDeserializer.instance);
 }});
 }

 @Test
 void testDefaultDeserialization() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[\"test1\",\"test2\",\"test3\"]");
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 String[] expected = {"test1", "test2", "test3"};
 assertArrayEquals(expected, actual);
 }

 @Test
 void testDeserializationWithCustomDeserializer() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[\"test1\",\"test2\",\"test3\"]");
 JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {};
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(String[].class, StringArrayDeserializer.instance);
 addDeserializer(String.class, customDeserializer);
 }});
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 String[] expected = {"test1_custom", "test2_custom", "test3_custom"}; // Custom deserialization result
 assertArrayEquals(expected, actual);
 }

 @Test
 void testNullValue() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[]");
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 assertArrayEquals(new String[0], actual);
 }

 @Test
 void testEmptyStringValue() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[\"\"]");
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 assertArrayEquals(new String[0], actual);
 }

 @Test
 void testNonArray() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("\"test\"");
 assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonNode, String[].class));
 }

 @Test
 void testEmptyArray() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[]");
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 assertArrayEquals(new String[0], actual);
 }

 @Test
 void testInvalidArray() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("{ \"test\": [\"test1\", 1] }");
 assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonNode, String[].class));
 }

 @Test
 void testContextualDeserializer() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[\"`test1\"`, `test2`, `test3`]");
 @JsonDeserialize(using = CustomStringDeserializer.class)
 class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString() + "_custom";
 }
 }
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(String[].class, StringArrayDeserializer.instance);
 addDeserializer(String.class, new CustomStringDeserializer());
 }});
 String[] actual = objectMapper.readValue(jsonNode, String[].class);
 String[] expected = {"test1_custom", "test2", "test3_custom"}; // Custom deserialization result
 assertArrayEquals(expected, actual);
 }

 @Test
 void testContextualDeserializerWithCustomTypeId() throws IOException {
 JsonNode jsonNode = new ObjectMapper().readTree("[\"`test1\"`, `test2`, `test3`]");
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(String[].class, StringArrayDeserializer.instance);
 addDeserializer(String.class, new CustomStringDeserializer());
 addHandler(new DefaultTypingHandler());
 }});
 JsonDeserializer<String[]> deserializer = objectMapper.getTypeFactory().constructDeserializer(JsonType.ValueType.STRING_ARRAY_TYPE);
 String[] actual = deserializer.deserialize(jsonNode, new TestContext());
 String[] expected = {"test1_custom", "test2", "test3_custom"}; // Custom deserialization result
 assertArrayEquals(expected, actual);
 }

}

class TestContext extends DeserializationContext {
 @Override
 public ObjectFinder getContextualObjectFinder() {
 return new AlwaysReturnNullObjectFinder();
 }
}

class AlwaysReturnNullObjectFinder implements ObjectFinder {
 @Override
 public Object findValue(Class<?> type) {
 return null;
 }
}