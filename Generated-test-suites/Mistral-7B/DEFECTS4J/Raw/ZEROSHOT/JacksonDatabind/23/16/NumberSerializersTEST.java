package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 @BeforeAll
 static void setup() {
 // Initialize any shared resources or setup necessary for all tests
 }

 @AfterAll
 static void tearDown() {
 // Perform any necessary cleanup after all tests have finished
 }

 @BeforeEach
 void setUp() {
 // Set up state for each test
 }

 @AfterEach
 void tearDown() {
 // Perform any necessary cleanup after each test
 }

 @Test
 public void testShortSerializer() throws JsonMappingException, JsonProcessingException {
 ShortSerializer shortSerializer = new ShortSerializer();
 Short testShort = Short.parseShort("123");
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testShort);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Short resultShort = jsonParser.parseAs(Short.class);
 assertEquals(testShort, resultShort);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 shortSerializer.serialize(testShort, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testIntegerSerializer() throws JsonMappingException, JsonProcessingException {
 IntegerSerializer integerSerializer = new IntegerSerializer();
 Integer testInteger = Integer.valueOf(123);
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testInteger);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Integer resultInteger = jsonParser.parseAs(Integer.class);
 assertEquals(testInteger, resultInteger);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 integerSerializer.serialize(testInteger, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testIntLikeSerializer() throws JsonMappingException, JsonProcessingException {
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 Byte testByte = Byte.valueOf((byte) 123);
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testByte);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Byte resultByte = jsonParser.parseAs(Byte.class);
 assertEquals(testByte, resultByte);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 intLikeSerializer.serialize(testByte, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testLongSerializer() throws JsonMappingException, JsonProcessingException {
 LongSerializer longSerializer = new LongSerializer();
 Long testLong = Long.parseLong("1234567890");
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testLong);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Long resultLong = jsonParser.parseAs(Long.class);
 assertEquals(testLong, resultLong);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 longSerializer.serialize(testLong, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testFloatSerializer() throws JsonMappingException, JsonProcessingException {
 FloatSerializer floatSerializer = new FloatSerializer();
 Float testFloat = Float.valueOf(123.45f);
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testFloat);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Float resultFloat = jsonParser.parseAs(Float.class);
 assertEquals(testFloat, resultFloat);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 floatSerializer.serialize(testFloat, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testDoubleSerializer() throws JsonMappingException, JsonProcessingException {
 DoubleSerializer doubleSerializer = new DoubleSerializer();
 Double testDouble = Double.valueOf(123.456789);
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonString = objectWriter.writeValueAsString(testDouble);
 JsonParser jsonParser = new ObjectMapper().readTree(jsonString);
 Double resultDouble = jsonParser.parseAs(Double.class);
 assertEquals(testDouble, resultDouble);

 JsonGenerator jsonGenerator = new ObjectMapper().createGenerator();
 doubleSerializer.serialize(testDouble, jsonGenerator, new ObjectMapper().getSerializerProvider());
 String actualJsonString = jsonGenerator.getOutputContent().toString();
 assertEquals(jsonString, actualJsonString);
 }

 @Test
 public void testJsonFormatAnnotation() throws JsonMappingException, JsonProcessingException {
 ShortSerializer shortSerializer = new ShortSerializer();
 Short testShort = Short.parseShort("123");
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 Short serializedShort = objectWriter.writeValueAsString(testShort).parseAs(Short.class);
 assertEquals(testShort, serializedShort);

 JsonParser jsonParser = new ObjectMapper().readTree("{\"value\": 32000}");
 JsonNode jsonNode = jsonParser.getCodec().treeToValue(jsonParser.readValueAsTree(), Short.class);
 assertEquals(Short.valueOf((short) 32000), jsonNode);

 Short deserializedShort = objectWriter.writeValueAsString(Short.valueOf((short) 32000)).parseAs(Short.class);
 assertEquals(Short.valueOf((short) 32000), deserializedShort);

 Short testShortWithJsonFormat = new Short((short) 123);
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Short testShortWithJsonFormatAnnotation = new Short((short) 32000);
 ObjectWriter objectWriterWithJsonFormatAnnotation = new ObjectMapper()
 .writer()
 .withDefaultPrettyPrinter()
 .withSerializerFactory(new JsonSerializerFactory() {
 @Override
 public JsonSerializer<?> createValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
 if (type == Short.class) {
 return new MyShortSerializer();
 }
 return super.createValueSerializer(type, serializers);
 }
 });
 String jsonStringWithJsonFormatAnnotation = objectWriterWithJsonFormatAnnotation.writeValueAsString(testShortWithJsonFormatAnnotation);
 JsonParser jsonParserWithJsonFormatAnnotation = new ObjectMapper().readTree(jsonStringWithJsonFormatAnnotation);
 Short resultShortWithJsonFormatAnnotation = jsonParserWithJsonFormatAnnotation.parseAs(Short.class);
 assertEquals(testShortWithJsonFormatAnnotation, resultShortWithJsonFormatAnnotation);

 JsonGenerator jsonGeneratorWithJsonFormatAnnotation = new ObjectMapper().createGenerator();
 jsonGeneratorWithJsonFormatAnnotation.setCodec(new MyObjectMapper());
 shortSerializer.serialize(testShortWithJsonFormatAnnotation, jsonGeneratorWithJsonFormatAnnotation, new ObjectMapper().getSerializerProvider());
 String actualJsonStringWithJsonFormatAnnotation = jsonGeneratorWithJsonFormatAnnotation.getOutputContent().toString();
 assertEquals(jsonStringWithJsonFormatAnnotation, actualJsonStringWithJsonFormatAnnotation);
 }

 @Test
 public void testJsonFormatAnnotationWithCustomSerializer() throws JsonMappingException, JsonProcessingException {
 ShortSerializer shortSerializer = new ShortSerializer();
 Short testShort = Short.parseShort("123");
 ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
 Short serializedShort = objectWriter.writeValueAsString(testShort).parseAs(Short.class);
 assertEquals(testShort, serializedShort);

 Short testShortWithJsonFormat = new Short((short) 32000);
 @JsonFormat(shape = JsonFormat.Shape.STRING, numberFormat = "E")
 Short testShortWithJsonFormatAnnotation = new Short((short) 32000);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriterWithJsonFormatAnnotation = objectMapper.writer().withDefaultPrettyPrinter();
 ObjectWriter objectWriterWithCustomSerializer = objectMapper.writer()
 .withSerializerFactory(new JsonSerializerFactory() {
 @Override
 public JsonSerializer<?> createValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
 if (type == Short.class) {
 return new MyShortSerializerWithCustomNumberFormat();
 }
 return super.createValueSerializer(type, serializers);
 }
 });
 String jsonStringWithJsonFormatAnnotation = "";
 try {
 jsonStringWithJsonFormatAnnotation = objectWriterWithCustomSerializer.writeValueAsString(testShortWithJsonFormatAnnotation);
 } catch (JsonProcessingException e) {
 fail("Expected to be able to serialize with custom serializer and JSON format annotation");
 }
 assertEquals("1.22e+04", jsonStringWithJsonFormatAnnotation);

 JsonParser jsonParserWithJsonFormatAnnotation = new ObjectMapper().readTree(jsonStringWithJsonFormatAnnotation);
 Short resultShortWithJsonFormatAnnotation = jsonParserWithJsonFormatAnnotation.parseAs(Short.class);
 assertEquals(Short.valueOf((short) 32000), resultShortWithJsonFormatAnnotation);
 }

 class MyShortSerializer extends ShortSerializer {
 @Override
 public void serializeWithType(Short value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
 super.serializeWithType(value, gen, provider, typeSer);
 gen.writeString("MyShortSerializer");
 }
 }

 class MyObjectMapper extends ObjectMapper {
 @Override
 public JsonSerializer<? extends JsonSerializer> findValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
 if (type == Short.class) {
 return new MyShortSerializerWithCustomNumberFormat();
 }
 return super.findValueSerializer(type, serializers);
 }
 }

 class MyShortSerializerWithCustomNumberFormat extends ShortSerializer {
 @Override
 public void serializeWithType(Short value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
 super.serializeWithType(value, gen, provider, typeSer);
 gen.writeString("MyShortSerializerWithCustomNumberFormat");
 }

 @Override
 public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumberFormat(value.shortValue(), "E");
 }
 }
}