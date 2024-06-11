import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 BigDecimal number = new BigDecimal("123.45");
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(number.toString(), parser.getText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 BigInteger number = new BigInteger("123456789012345678901234567890");
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(number.toString(), parser.getText());
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 Integer number = 123;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Integer.toString(number), parser.getText());
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 Long number = 1234567890123456789L;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Long.toString(number), parser.getText());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 Double number = 123.45;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Double.toString(number), parser.getText());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 Float number = 123.45f;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Float.toString(number), parser.getText());
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 Byte number = (byte) 123;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Byte.toString(number), parser.getText());
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 Short number = (short) 123;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 assertEquals(Short.toString(number), parser.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeInvalidType() throws JsonProcessingException {
 String number = "abc";
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new NumberSerializer().serialize(number, null, null));
 }
}