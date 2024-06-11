import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNode;
import mockit.Mock;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  Date date = new Date();
  String expected = "\"2022-12-12T12:12:12.123Z\""; // ISO formatted date string
  String actual = mapper.writeValueAsString(mapper.readValue(mapper.writeValueAsString(date), Date.class));
  assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  String value = "test";
  String expected = "\"test\"";
  String actual = mapper.writeValueAsString(mapper.readValue(mapper.writeValueAsString(value), String.class));
  assertEquals(expected, actual);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException, JsonMappingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonNode expected = mapper.readTree("{\"type\":\"string\"}");
  JsonNode actual = new StdKeySerializer().getSchema(mock(SerializerProvider.class), mapper.getTypeFactory().constructType(Object.class));
  assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitorInvalidObjectType() throws JsonMappingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
  new StdKeySerializer().acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(int.class));
  verifyNoMoreInteractions(visitor);
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorIOException() throws JsonMappingException, IOException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
  SerializerProvider provider = mock(SerializerProvider.class);
  when(provider.getActiveRootValue()).thenReturn(new Date());
  new StdKeySerializer().acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Object.class), provider);
  IOException ioException = new IOException();
  doThrow(ioException).when(provider).defaultSerializeDateKey((Date) anyObject(), anyInstance(JsonGenerator.class));
  new StdKeySerializer().serialize((Date) anyObject(), anyInstance(JsonGenerator.class), provider);
 }

 @Test
 public void testAcceptJsonFormatVisitorUnexpectedObjectType() throws JsonMappingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
  new StdKeySerializer().acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(List.class));
  verify(visitor).expectStringFormat(mapper.getTypeFactory().constructType(Object.class));
 }

}