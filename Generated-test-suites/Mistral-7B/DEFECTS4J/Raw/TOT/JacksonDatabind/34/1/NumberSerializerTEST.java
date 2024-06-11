import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 String expected = "123.456";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("98765432109876543210");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 String expected = "98765432109876543210";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 int intValue = 123;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(intValue, jsonGenerator, mapper.getSerializerProvider());
 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 String expected = "123";
 assertEquals(expected, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeUnsupportedType() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String unsupportedType = new String("unsupportedType");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 numberSerializer.serialize(unsupportedType, jsonGenerator, mapper.getSerializerProvider());
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String jsonString = "123.456";
 Number deserializedNumber = mapper.readValue(jsonString, Number.class);
 assertEquals(new BigDecimal(jsonString), deserializedNumber);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String jsonString = "98765432109876543210";
 Number deserializedNumber = mapper.readValue(jsonString, Number.class);
 assertEquals(new BigInteger(jsonString), deserializedNumber);
 }

 @Test
 public void testDeserializeInt() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String jsonString = "123";
 Number deserializedNumber = mapper.readValue(jsonString, Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }
}