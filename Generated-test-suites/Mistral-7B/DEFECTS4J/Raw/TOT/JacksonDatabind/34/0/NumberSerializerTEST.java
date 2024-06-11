import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("1234.56");
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getJsonGenerator();
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputBuffer().toString()));
 assertEquals("1234.56", jsonParser.getText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = BigInteger.valueOf(123456);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getJsonGenerator();
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputBuffer().toString()));
 assertEquals("123456", jsonParser.getText());
 }

 @Test
 public void testSerializeOtherNumberTypes() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Integer integer = 123;
 Long longNumber = 1234L;
 Double doubleNumber = 123.45;
 Float floatNumber = 123.45f;
 Byte byteNumber = (byte) 123;
 Short shortNumber = (short) 123;
 jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getJsonGenerator();
 numberSerializer.serialize(integer, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonParser.getText());
 numberSerializer.serialize(longNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("1234", jsonParser.getText());
 numberSerializer.serialize(doubleNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonParser.getText());
 numberSerializer.serialize(floatNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonParser.getText());
 numberSerializer.serialize(byteNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonParser.getText());
 numberSerializer.serialize(shortNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonParser.getText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeString() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringNumber = "not a number";
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getJsonGenerator();
 numberSerializer.serialize(stringNumber, jsonGenerator, objectMapper.getSerializerProvider());
 }
}