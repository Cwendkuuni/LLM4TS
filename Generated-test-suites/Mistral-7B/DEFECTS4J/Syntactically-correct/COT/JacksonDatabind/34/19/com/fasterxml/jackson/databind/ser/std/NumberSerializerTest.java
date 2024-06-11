import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializationShape;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class NumberSerializerTest {

 private ObjectMapper mapper;
 private NumberSerializer numberSerializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.setAnnotationIntrospector(new AnnotationIntrospector());
 numberSerializer = NumberSerializer.instance;
 }

 @Test
 public void testSerializeNumber() throws IOException {
 Number testNumbers[] = {new BigDecimal("123.45"), new BigInteger("567"), 1, 123, 123L, 123.0f, 123.0d, (byte) 1, (short) 123, 32767, 32768};

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 mapper.writeValue(gen, testNumbers);
 String json = gen.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Number deserializedNumbers[] = new ObjectMapper().readValue(parser, Number[].class);

 assertArrayEquals(testNumbers, deserializedNumbers);
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 Number testNumber = new BigDecimal("123.45");
 String expectedJson = "123.45";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 Number testNumber = BigInteger.valueOf(123);
 String expectedJson = "123";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 Number testNumber = 1;
 String expectedJson = "1";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 Number testNumber = 123.0d;
 String expectedJson = "123.0";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeFloat() throws IOException {
 Number testNumber = 123.0f;
 String expectedJson = "123.0";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeByte() throws IOException {
 Number testNumber = (byte) 1;
 String expectedJson = "1";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeShort() throws IOException {
 Number testNumber = (short) 123;
 String expectedJson = "123";

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeMaxByte() throws IOException {
 Number testNumber = (byte) Byte.MAX_VALUE;
 String expectedJson = Byte.MAX_VALUE;

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeMinByte() throws IOException {
 Number testNumber = (byte) Byte.MIN_VALUE;
 String expectedJson = Byte.MIN_VALUE;

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeMaxShort() throws IOException {
 Number testNumber = (short) Short.MAX_VALUE;
 String expectedJson = Short.MAX_VALUE;

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test
 public void testSerializeMinShort() throws IOException {
 Number testNumber = (short) Short.MIN_VALUE;
 String expectedJson = Short.MIN_VALUE;

 String json = serializeAndGetJson(testNumber);
 assertNotNull(json);
 assertEquals(expectedJson, json);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws IOException {
 Number testNumber = null;
 mapper.writeValue(new ByteArrayOutputStream(), testNumber, numberSerializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNonNumberType() throws IOException {
 Object testObject = new Object();
 mapper.writeValue(new ByteArrayOutputStream(), testObject, numberSerializer);
 }

 private String serializeAndGetJson(Number number) throws IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 mapper.writeValue(gen, number, numberSerializer);
 String json = gen.getOutputContent();
 return json;
 }
}