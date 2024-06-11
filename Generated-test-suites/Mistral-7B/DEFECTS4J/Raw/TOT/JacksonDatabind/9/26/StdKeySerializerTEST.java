import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleModule module = new SimpleModule();
 module.addSerializer(Date.class, serializer);
 ObjectWriter writer = objectMapper.writer(new StandardSerializer(Date.class, serializer));
 Date date = new Date();
 String expected = "\"date\"";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, gen, null);
 gen.flush(); // important to call this, serialize does not
 String actual = gen.getOutputContent();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 String key = "testKey";
 String expected = "\"testKey\"";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(key, gen, null);
 gen.flush(); // important to call this, serialize does not
 String actual = gen.getOutputContent();
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectWriter writer = objectMapper.writer(new StandardSerializer(Date.class, serializer));
 String expectedErrorMessage = "Cannot serialize null value";
 try {
 writer.writeValueAsString(null);
 fail("Expected JsonMappingException");
 } catch (JsonProcessingException e) {
 JsonMappingException ex = (JsonMappingException) e.getCause();
 assertEquals(expectedErrorMessage, ex.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringType() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Integer key = 1;
 String expectedErrorMessage = "Expected String format but was: class java.lang.Integer";
 try {
 serializer.acceptJsonFormatVisitor(objectMapper.getPolymorphicSerializer(JsonType.constructUnboundedType(), null), Object.class.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }
}