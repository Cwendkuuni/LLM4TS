import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

public class StdKeySerializerTest {

 private StdKeySerializer serializer = new StdKeySerializer();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator mockGenerator = mock(JsonGenerator.class);
 SerializerProvider mockProvider = mock(SerializerProvider.class);

 when(mockGenerator.writeFieldName(anyString())).thenCallRealMethod();
 when(mockProvider.defaultSerializeDateKey(eq(date), eq(mockGenerator))).thenCallRealMethod();

 serializer.serialize(date, mockGenerator, mockProvider);

 // Check that the Date has been serialized correctly
 JsonNode actual = JsonTestUtils.readTree(mockGenerator.getOutputStream());
 JsonNode expected = JsonTestUtils.parseString("\"\" : { \"@type\": \"@type/String\", \"value\": \"\" }");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 Object value = "testValue";
 JsonGenerator mockGenerator = mock(JsonGenerator.class);
 SerializerProvider mockProvider = mock(SerializerProvider.class);

 when(mockGenerator.writeFieldName(anyString())).thenCallRealMethod();

 serializer.serialize(value, mockGenerator, mockProvider);

 // Check that the string has been serialized correctly
 JsonNode actual = JsonTestUtils.readTree(mockGenerator.getOutputStream());
 JsonNode expected = JsonTestUtils.parseString("\"testValue\" : { \"@type\": \"@type/String\", \"value\": \"testValue\" }");
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 Object value = null;
 JsonGenerator mockGenerator = mock(JsonGenerator.class);
 SerializerProvider mockProvider = mock(SerializerProvider.class);

 serializer.serialize(value, mockGenerator, mockProvider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode expected = JsonTestUtils.parseString("{ \"$id\": \"/Definitions/Strings\", \"title\": \"String\", \"type\": \"string\" }");
 JsonNode actual = serializer.getSchema(mock(SerializerProvider.class), TypeFactory.defaultInstance().constructType(String.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = TypeFactory.defaultInstance().constructType(String.class);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 }
}