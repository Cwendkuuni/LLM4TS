import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testStringValueSerializer() throws JsonProcessingException {
 String input = "Hello World";
 String expected = "\"Hello World\"";

 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);
 assertEquals(expected, serializer.serializeValue(input, null, null));
 }

 @Test
 public void testIntegerValueSerializer() throws JsonProcessingException {
 Integer input = 123;
 String expected = "123";

 JsonValueSerializer serializer = objectMapper.findValueSerializer(Integer.class, null);
 assertEquals(expected, serializer.serializeValue(input, null, null));
 }

 @Test
 public void testBooleanValueSerializer() throws JsonProcessingException {
 Boolean input = true;
 String expected = "true";

 JsonValueSerializer serializer = objectMapper.findValueSerializer(Boolean.class, null);
 assertEquals(expected, serializer.serializeValue(input, null, null));
 }

 @Test
 public void testSerializableClassValueSerializer() throws JsonProcessingException {
 MySerializableClass input = new MySerializableClass("Test");
 String expected = "\"Test\"";

 JsonValueSerializer serializer = objectMapper.findValueSerializer(MySerializableClass.class, null);
 assertEquals(expected, serializer.serializeValue(input, null, null));
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullValueSerializer() throws JsonProcessingException {
 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);
 serializer.serializeValue(null, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownValueSerializer() throws JsonProcessingException {
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyUnknownClass.class, null);
 serializer.serializeValue(new MyUnknownClass(), null, null);
 }
}

class MySerializableClass {
 private final String value;

 MySerializableClass(String value) {
 this.value = value;
 }

 @JsonValue
 public String getValue() {
 return value;
 }
}

class MyUnknownClass {
 // this class is used to test handling of unknown types
}