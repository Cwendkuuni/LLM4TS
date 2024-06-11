import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.ObjectIdWriter;

public class WritableObjectIdTest {

 private WritableObjectId writableObjectId;
 private ObjectIdGenerator<Object> objectIdGenerator;
 private ObjectIdWriter objectIdWriter;
 private JsonGenerator jsonGenerator;
 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectIdGenerator = mock(ObjectIdGenerator.class);
 objectIdWriter = new ObjectIdWriter(null, null, null);
 jsonGenerator = mock(JsonGenerator.class);
 serializerProvider = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 writableObjectId = new WritableObjectId(objectIdGenerator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 when(objectIdGenerator.generateId(any())).thenReturn("testId");
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(jsonGenerator.writeObjectRef(anyString())).thenReturn(jsonGenerator);

 assert(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter) == true);

 verify(jsonGenerator, times(1)).writeObjectRef(eq("testId"));
 }

 @Test
 public void testWriteAsId_CannotWriteObjectId() throws IOException {
 when(objectIdGenerator.generateId(any())).thenReturn("testId");
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(objectIdWriter.serializer.serialize(any(), eq(jsonGenerator), eq(serializerProvider))).thenReturn(jsonGenerator);

 SerializableString name = new SerializableString("name");
 when(objectIdWriter.propertyName).thenReturn(name);

 assert(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter) == false);

 verify(jsonGenerator, never()).writeObjectRef(anyString());
 verify(objectIdWriter, times(1)).serializer.serialize(eq("testId"), eq(jsonGenerator), eq(serializerProvider));
 verify(jsonGenerator, times(1)).writeFieldName(eq("name"));
 }

 @Test
 public void testGenerateId() throws Exception {
 when(objectIdGenerator.generateId(any())).thenReturn("testId");

 Object testObj = new Object();
 Object testId = writableObjectId.generateId(testObj);

 assertEquals("testId", testId);
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteAsField_IdNotGenerated() throws IOException {
 when(objectIdGenerator.generateId(any())).thenThrow(new IllegalStateException());

 assert(writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter) == null);

 verify(jsonGenerator, never()).writeObjectId(anyString());
 }
}