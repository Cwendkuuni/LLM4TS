package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNumberNode;
import com.fasterxml.jackson.databind.node.JsonProcessableNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for {@link NumberSerializer}
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 objectMapper = new JsonMapperBuilder().build();
 objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
 BigDecimal bigDecimal = new BigDecimal("3.141592653589793238462643383279502884169729482848611647062");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("3.141592653589793238462643383279502884169729482848611647062");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException {
 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("123456789012345678901234567890");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeInt() throws JsonGenerationException, IOException {
 int intValue = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(intValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("123");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, IOException {
 long longValue = 12345678901234567890L;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(longValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("12345678901234567890");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, IOException {
 float floatValue = 123.45f;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(floatValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("123.45");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, IOException {
 double doubleValue = 123.45;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(doubleValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("123.45");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, IOException {
 byte byteValue = 127;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(byteValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("127");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, IOException {
 short shortValue = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(shortValue, jsonGenerator, serializerProvider);
 JsonNumberNode expectedJsonNode = JsonNodeFactory.instance.numberNode("123");
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testSerializeOtherNumberTypes() throws JsonGenerationException, IOException {
 Short shortValue = 123;
 BigDecimal bigDecimal = new BigDecimal("3.141592653589793238462643383279502884169729482848611647062");
 Object otherNumberTypes[] = { shortValue, bigDecimal };
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 for (Object number : otherNumberTypes) {
 numberSerializer.serialize(number, jsonGenerator, serializerProvider);
 }
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputStream());
 JsonProcessableNode expectedJsonNode = JsonNodeFactory.instance.objectNode();
 expectedJsonNode.put("123", JsonNodeFactory.instance.numberNode("123"));
 expectedJsonNode.put("3.141592653589793238462643383279502884169729482848611647062", JsonNodeFactory.instance.numberNode("3.141592653589793238462643383279502884169729482848611647062"));
 assertEquals(expectedJsonNode, actualJsonNode);
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"3.141592653589793238462643383279502884169729482848611647062\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(BigDecimal.valueOf(3.141592653589793238462643383279502884169729482848611647062), number);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123456789012345678901234567890\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(BigInteger.valueOf(12345678901234567890L), number);
 }

 @Test
 public void testDeserializeInt() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(123, number.intValue());
 }

 @Test
 public void testDeserializeLong() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"12345678901234567890\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(12345678901234567890L, number.longValue());
 }

 @Test
 public void testDeserializeFloat() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123.45\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(123.45f, number.floatValue(), 0.001f);
 }

 @Test
 public void testDeserializeDouble() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123.45\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals(123.45, number.doubleValue(), 0.001);
 }

 @Test
 public void testDeserializeByte() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"127\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals((byte) 127, number.byteValue());
 }

 @Test
 public void testDeserializeShort() throws JsonParseException, IOException {
 ObjectMapper objectMapper = new JsonMapperBuilder().build();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123\"");
 JsonProcessingContext jsonProcessingContext = jsonParser.getCodec().getSerializationContext().withAttribute("_rootElementName", "root");
 ObjectReader objectReader = objectMapper.getFactory().createObjectReader(jsonParser, jsonProcessingContext);
 Number number = objectReader.readValueAs(Number.class);
 assertEquals((short) 123, number.shortValue());
 }
}