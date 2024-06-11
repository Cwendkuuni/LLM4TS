import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigDecimal bigDecimal = new BigDecimal("123.456");

 // Serialize test
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getGenerator(numberSerializer, mapper.findTypeValueIdentifier(BigDecimal.class), null, null);
 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputShutdownStream().getBytes().toString();
 String expected = "123.456";
 assertEquals(expected, actual);

 // Deserialize test
 JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(expected));
 Number deserialized = mapper.readValue(jsonParser, Number.class);
 assertEquals(bigDecimal, deserialized);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigInteger bigInteger = BigInteger.valueOf(1234567890L);

 // Serialize test
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getGenerator(numberSerializer, mapper.findTypeValueIdentifier(BigInteger.class), null, null);
 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputShutdownStream().getBytes().toString();
 String expected = "1234567890";
 assertEquals(expected, actual);

 // Deserialize test
 JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(expected));
 Number deserialized = mapper.readValue(jsonParser, Number.class);
 assertEquals(bigInteger, deserialized);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 int integer = 123;

 // Serialize test
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getGenerator(numberSerializer, mapper.findTypeValueIdentifier(Integer.class), null, null);
 numberSerializer.serialize(integer, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputShutdownStream().getBytes().toString();
 String expected = "123";
 assertEquals(expected, actual);

 // Deserialize test
 JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(expected));
 Number deserialized = mapper.readValue(jsonParser, Number.class);
 assertEquals(integer, deserialized);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedNumberType() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String unsupportedNumberType = "unsupported";

 // Serialize test
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getGenerator(numberSerializer, mapper.findTypeValueIdentifier(unsupportedNumberType.getClass()), null, null);
 numberSerializer.serialize(unsupportedNumberType, jsonGenerator, mapper.getSerializerProvider());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();

 JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory().getJsonFormatVisitor(numberSerializer, mapper.findTypeValueIdentifier(BigDecimal.class), null);
 numberSerializer.acceptJsonFormatVisitor(visitor, mapper.constructType(Number.class));
 }
}