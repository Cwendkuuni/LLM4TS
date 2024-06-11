import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
 public void testSerializeDate() throws JsonProcessingException {
 // Given
 Date date = new Date();

 // When
 serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeNumber(anyLong(), anyString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 // Given
 String str = "test";

 // When
 serializer.serialize(str, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(eq(str));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // Given
 Type typeHint = Object.class;

 // When
 ObjectNode schema = serializer.getSchema(mockSerializerProvider, typeHint);

 // Then
 JsonNode expectedSchema = JsonNodeFactory.instance.textNode("string");
 assertEquals(expectedSchema, schema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 // Given
 JavaType typeHint = null;

 // When
 serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), typeHint);

 // Then
 verify(mockSerializerProvider, times(1)).defaultSerializeDateKey(any(Date.class), eq(mockJsonGenerator));
 }
}