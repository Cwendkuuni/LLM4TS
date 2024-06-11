import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer<String> stringSerializer;
 private StdDelegatingSerializer<Integer> integerSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringSerializer = new StdDelegatingSerializer<>(new StringToIntConverter(), Object.class, null);
 integerSerializer = new StdDelegatingSerializer<>(String.class, new IntToStringConverter(), objectMapper.findValueSerializer(Integer.class));
 }

 @Test
 public void testStringSerialization() throws JsonProcessingException {
 String input = "Hello World";
 String expectedOutput = "\"Hello World\"";

 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stringSerializer.serialize(input, gen, objectMapper.getSerializationConfig().findValueSerializer(String.class));
 gen.flush();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputStream();
 String output = new String(byteArrayOutputStream.toByteArray());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 String input = "42";
 int expectedOutput = 42;
 int actualOutput;

 JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(input.getBytes()));
 parser.nextValue();
 actualOutput = stringSerializer.convertValue(parser.getValueAsToken());
 assertEquals(expectedOutput, actualOutput);

 gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 integerSerializer.serialize(expectedOutput, gen, objectMapper.getSerializationConfig().findValueSerializer(Integer.class));
 gen.flush();
 byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputStream();
 output = new String(byteArrayOutputStream.toByteArray());
 assertEquals("42", output);
 }

 @Test(expected = JsonMappingException.class)
 public void testStringToIntConversionFailure() {
 String input = "abc";
 int output = stringSerializer.convertValue(input);
 fail("Expected JsonMappingException when converting String \"abc\" to Int");
 }

 @Test(expected = JsonMappingException.class)
 public void testIntToStringConversionFailure() {
 int input = 123;
 String output = integerSerializer.convertValue(input);
 fail("Expected JsonMappingException when converting Int 123 to String");
 }

 @Test
 public void testEmptyStringSerialization() throws JsonProcessingException {
 String input = "";
 String expectedOutput = "\"\"";

 gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stringSerializer.serialize(input, gen, objectMapper.getSerializationConfig().findValueSerializer(String.class));
 gen.flush();
 byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputStream();
 output = new String(byteArrayOutputStream.toByteArray());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNullSerialization() throws JsonProcessingException {
 Object input = null;
 String expectedOutput = "null";

 gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stringSerializer.serialize(input, gen, objectMapper.getSerializationConfig().findValueSerializer(String.class));
 gen.flush();
 byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputStream();
 output = new String(byteArrayOutputStream.toByteArray());
 assertEquals(expectedOutput, output);
 }
}