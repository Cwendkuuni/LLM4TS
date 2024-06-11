import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 }

 @Test
 public void testSerialize_Date() throws IOException {
 // Given
 Date date = new Date();
 when(mockSerializerProvider.defaultSerializeDateKey(any(Date.class), eq(mockJsonGenerator)))
         .thenCallRealMethod();

 // When
 serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(anyString());
 }

 @Test
 public void testSerialize_String() throws IOException {
 // Given
 String str = "test_string";

 // When
 serializer.serialize(str, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(str));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // When
 JsonNode schema = serializer.getSchema(mockSerializerProvider, Object.class);

 // Then
 assertThat(schema, is(createSchemaNode("string")));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 // When
 serializer.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class));

 // Then
 verify(mockSerializerProvider, times(1)).getDefaultSerializerProvider();
 }

 @Test(expected = IllegalStateException.class)
 public void testSerialize_Null() throws IOException {
 // Given
 when(mockSerializerProvider.defaultSerializeDateKey(any(Date.class), eq(mockJsonGenerator)))
         .thenThrow(new IllegalStateException());

 // When
 serializer.serialize(null, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, never()).writeFieldName(anyString());
 }

 @Test
 public void testSerialize_EmptyString() throws IOException {
 // Given
 String emptyString = "";

 // When
 serializer.serialize(emptyString, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(""));
 }

 @Test
 public void testSerialize_Long() throws IOException {
 // Given
 long longValue = 123L;
 String expected = Long.toString(longValue);

 // When
 serializer.serialize(longValue, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(expected));
 }

 @Test
 public void testSerialize_Boolean() throws IOException {
 // Given
 boolean booleanValue = true;
 String expected = Boolean.toString(booleanValue);

 // When
 serializer.serialize(booleanValue, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(expected));
 }

 @Test
 public void testSerialize_Object() throws IOException {
 // Given
 Object object = new Object();
 String expected = object.toString();

 // When
 serializer.serialize(object, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(expected));
 }
}