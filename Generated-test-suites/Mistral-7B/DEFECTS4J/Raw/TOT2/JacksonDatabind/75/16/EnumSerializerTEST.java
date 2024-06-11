import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Enum.class);
 }

 /**
 * Test serialization of an Enum using the default configuration.
 */
 @Test
 public void testSerializeEnumDefault() throws IOException {
 Enum testEnum = Enum.valueOf(MyEnum.class, "VALUE_1");
 ObjectNode result = mapper.writeValueAsTree(serializer, testEnum);
 assertEquals("\"VALUE_1\"", result.get("$value").asText());
 }

 /**
 * Test serialization of an Enum using index instead of name.
 */
 @Test
 public void testSerializeEnumIndex() throws IOException {
 Enum testEnum = Enum.valueOf(MyEnum.class, "VALUE_1");
 ObjectMapper mapperWithIndex = new ObjectMapper();
 mapperWithIndex.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
 EnumSerializer serializerWithIndex = mapperWithIndex.findValueSerializer(Enum.class);
 ObjectNode result = mapperWithIndex.writeValueAsTree(serializerWithIndex, testEnum);
 assertEquals("0", result.get("$value").asText());
 }

 /**
 * Test serialization of an Enum with custom serialization using the @JsonFormat annotation.
 */
 @Test
 public void testSerializeEnumCustomSerialization() throws IOException {
 Enum testEnum = Enum.valueOf(MyCustomEnum.class, "VALUE_CUSTOM");
 ObjectNode result = mapper.writeValueAsTree(serializer, testEnum);
 assertEquals("\"CUSTOM_VALUE\"", result.get("$value").asText());
 }

 /**
 * Test deserialization of an Enum using the default configuration.
 */
 @Test
 public void testDeserializeEnumDefault() throws IOException {
 String jsonString = "\"VALUE_1\"";
 Enum testEnum = mapper.readValue(jsonString, Enum.class);
 assertEquals(MyEnum.VALUE_1, testEnum);
 }

 /**
 * Test deserialization of an Enum using index instead of name.
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeEnumIndexError() throws IOException {
 String jsonString = "0";
 try {
 Enum testEnum = mapper.readValue(jsonString, Enum.class);
 } catch (JsonMappingException e) {
 // expected error
 String errorMessage = e.getMessage();
 assertTrue(errorMessage.contains("Cannot deserialize value of type"));
 assertTrue(errorMessage.contains("using index"));
 }
 }

 /**
 * Test deserialization of an Enum with custom serialization using the @JsonFormat annotation.
 */
 @Test
 public void testDeserializeEnumCustomSerialization() throws IOException {
 String jsonString = "\"CUSTOM_VALUE\"";
 Enum testEnum = mapper.readValue(jsonString, Enum.class);
 assertEquals(MyCustomEnum.VALUE_CUSTOM, testEnum);
 }
}