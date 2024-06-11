import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class NumberSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = 1;
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = 1L;
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1L);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = 1.0f;
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1.0f);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = 1.0;
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1.0);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = BigInteger.valueOf(1);
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = new BigDecimal("1");
 numberSerializer.serialize(number, gen, null);
 JsonNode actual = MAPPER.readTree(gen.asString());
 JsonNode expected = MAPPER.valueToTree(1);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnknownType() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = new Object();
 numberSerializer.serialize(number, gen, null);
 }

 @Test(expected = IOException.class)
 public void testSerializeNull() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = MAPPER.getPolymorphicSerializer(Number.class).getGenerator();
 Number number = null;
 numberSerializer.serialize(number, gen, null);
 }

 @Test
 public void testGetSchemaInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonSchema schema = numberSerializer.getSchema(null, null);
 JsonNode expected = MAPPER.valueToTree("integer");
 assertEquals(expected, schema);
 }

 @Test
 public void testGetSchemaBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonSchema schema = numberSerializer.getSchema(null, null);
 JsonNode expected = MAPPER.valueToTree("number");
 assertEquals(expected, schema);
 }

 @Test
 public void testAcceptJsonFormatVisitorIntFormat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper visitor = MAPPER.schemaFactory().getNumberFormatVisitor(null);
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 JsonNode expected = MAPPER.valueToTree("integer");
 JsonNode actual = visitor.getSchema();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitorUnknownType() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper visitor = MAPPER.schemaFactory().getNumberFormatVisitor(null);
 Class<?> handledType = numberSerializer.handledType();
 if (handledType == BigDecimal.class) {
 visitor.expectNumberFormat(JsonParser.NumberType.BIG_DECIMAL);
 } else {
 visitor.expectNumberFormat(JsonParser.NumberType.INTEGER);
 }
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 }
}