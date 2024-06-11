import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigDecimal number = new BigDecimal("1234567890.123456789");
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "1234567890.123456789";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigInteger number = new BigInteger("123456789012345678901234567890");
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "123456789012345678901234567890";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Integer number = 12345;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "12345";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Long number = 123456789012345L;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "123456789012345";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Double number = 123456.789;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "123456.789";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Float number = 123.45f;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "123.45";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Byte number = (byte) 127;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "127";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Short number = (short) 12345;
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(number, generator, new SerializerProvider().builder().build());
 String actual = generator.getOutputBuilder().toString();
 String expected = "12345";
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 String invalidType = new String("InvalidType");
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 serializer.serialize(invalidType, generator, new SerializerProvider().builder().build());
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}