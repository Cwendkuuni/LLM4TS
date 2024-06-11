import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer<String> stringDelegateSerializer;
 private StdDelegatingSerializer<Integer> intDelegateSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringDelegateSerializer = new StdDelegatingSerializer<String>(
 String.class, new StringToIntegerConverter(), JsonSerializers.stdSerializers().findValueSerializer(Integer.class));
 intDelegateSerializer = new StdDelegatingSerializer<Integer>(
 Integer.class, new IntegerToStringConverter(), JsonSerializers.stdSerializers().findValueSerializer(String.class));
 }

 @Test
 public void testStringToIntegerSerialization() throws JsonGenerationException, JsonMappingException {
 ObjectNode source = objectMapper.createObjectNode();
 source.put("stringValue", "42");
 ObjectNode result = objectMapper.writeValueAsTree(stringDelegateSerializer, source);
 assertEquals(42, result.get("stringValue").intValue());
 }

 @Test
 public void testEmptyStringToNullSerialization() throws JsonGenerationException, JsonMappingException {
 ObjectNode source = objectMapper.createObjectNode();
 ObjectNode result = objectMapper.writeValueAsTree(stringDelegateSerializer, source);
 assertNull(result.get("stringValue"));
 }

 @Test
 public void testIntegerToStringSerialization() throws JsonGenerationException, JsonMappingException {
 ObjectNode source = objectMapper.createObjectNode();
 source.put("integerValue", 42);
 ObjectNode result = objectMapper.writeValueAsTree(intDelegateSerializer, source);
 assertEquals("42", result.get("integerValue").asText());
 }

 @Test
 public void testNullToNullSerialization() throws JsonGenerationException, JsonMappingException {
 ObjectNode source = objectMapper.createObjectNode();
 ObjectNode result = objectMapper.writeValueAsTree(intDelegateSerializer, source);
 assertNull(result.get("integerValue"));
 }

 @Test(expected = IllegalStateException.class)
 public void testStringToIntegerSerializationWithSameType() throws JsonGenerationException, JsonMappingException {
 ObjectNode source = objectMapper.createObjectNode();
 source.put("stringValue", "42");
 StdDelegatingSerializer<String> sameTypeSerializer = new StdDelegatingSerializer<String>(
 String.class, new StringToIntegerConverter(), JsonSerializers.stdSerializers().findValueSerializer(String.class));
 objectMapper.writeValueAsTree(sameTypeSerializer, source);
 }
}