import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Test class for StdKeySerializer
 */
public class StdKeySerializerTest {

 private ObjectMapper objectMapper;
 private StdKeySerializer stdKeySerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stdKeySerializer = new StdKeySerializer();
 }

 // Test for serialize method with null value
 @Test
 public void testSerializeNull() throws JsonProcessingException {
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stdKeySerializer.serialize(null, generator, objectMapper.getSerializationConfig().getSerializerProvider());
 String actual = generator.getOutputContentAsString();
 String expected = "null";
 assertEquals(expected, actual);
 }

 // Test for serialize method with Date value
 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stdKeySerializer.serialize(date, generator, objectMapper.getSerializationConfig().getSerializerProvider());
 String actual = generator.getOutputContentAsString();
 String expected = "\"" + date.toISO8601String() + "\"";
 assertEquals(expected, actual);
 }

 // Test for serialize method with String value
 @Test
 public void testSerializeString() throws JsonProcessingException {
 String value = "TestString";
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 stdKeySerializer.serialize(value, generator, objectMapper.getSerializationConfig().getSerializerProvider());
 String actual = generator.getOutputContentAsString();
 String expected = "\"" + value + "\"";
 assertEquals(expected, actual);
 }

 // Test for getSchema method
 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode actual = stdKeySerializer.getSchema(objectMapper.getSerializationConfig().getSerializerProvider(), objectMapper.getTypeFactory().constructType(String.class));
 JsonNode expected = objectMapper.readTree("{\"type\":\"string\"}");
 assertEquals(expected, actual);
 }

 // Test for acceptJsonFormatVisitor method
 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = objectMapper.getFactory().createJsonFormatVisitor(null, null, null, null, null, null, null, null, null, null, null, null);
 stdKeySerializer.acceptJsonFormatVisitor(visitor, objectMapper.getTypeFactory().constructType(String.class));
 String actual = visitor.getFormatName();
 String expected = "string";
 assertEquals(expected, actual);
 }
}