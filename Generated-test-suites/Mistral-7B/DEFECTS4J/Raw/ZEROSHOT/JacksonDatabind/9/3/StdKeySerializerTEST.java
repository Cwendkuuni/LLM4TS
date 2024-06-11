import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdKeySerializerTest {

 private static final StdKeySerializer TEST_SERIALIZER = new StdKeySerializer();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
 SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

 when(mockSerializerProvider.defaultSerializeDateKey(date, mockJsonGenerator)).thenCallRealMethod();

 TEST_SERIALIZER.serialize(date, mockJsonGenerator, mockSerializerProvider);

 // Verify the mocked methods have been called with the correct arguments
 assertEquals("2022-10-11T12:13:14Z", mockJsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 Object testString = "TestString";
 JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
 SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

 TEST_SERIALIZER.serialize(testString, mockJsonGenerator, mockSerializerProvider);

 // Verify the serialize method writes the correct field name
 assertEquals("TestString", mockJsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode expectedSchema = createSchemaNode("string");
 SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
 JavaType mockTypeHint = mock(JavaType.class);

 when(mockSerializerProvider.isEnabled(Mockito.anyInt())).thenReturn(true);
 when(mockTypeHint.getRawClass()).thenReturn(String.class);

 assertEquals(expectedSchema, TEST_SERIALIZER.getSchema(mockSerializerProvider, mockTypeHint));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
 JavaType mockTypeHint = mock(JavaType.class);

 TEST_SERIALIZER.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockTypeHint);

 // Verify the mocked method expectStringFormat has been called with the correct arguments
 verify(mockJsonFormatVisitorWrapper).expectStringFormat(mockTypeHint);
 }

 private static JsonNode createSchemaNode(String type) {
 // Create a JSON schema node as expected by the acceptJsonFormatVisitor method
 // This method is just for mocking, you can use any library or way to create the schema node
 //...
 return null;
 }
}