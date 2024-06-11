import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private JsonGenerator jsonGenerator;
 private Object id;
 private SerializerProvider serializerProvider;
 private ObjectIdWriter objectIdWriter;

 @Before
 public void setUp() {
 idGenerator = mock(ObjectIdGenerator.class);
 jsonGenerator = mock(JsonGenerator.class);
 serializerProvider = mock(SerializerProvider.class);
 objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider);
 }

 @Test
 public void testWriteAsId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 id = idGenerator.generateId(new Object());
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(jsonGenerator.writeObjectRef(anyString())).thenReturn(jsonGenerator);

 assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 assertEquals(id.toString(), jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteAsIdWithAlwaysAsId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 id = idGenerator.generateId(new Object());
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeObjectRef(anyString())).thenThrow(IOException.class);
 when(idGenerator.generateId(any())).thenReturn(null);
 when(objectIdWriter.alwaysAsId).thenReturn(true);

 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 assertNull(id);
 }

 @Test
 public void testGenerateId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object expectedId = idGenerator.generateId(new Object());
 when(idGenerator.generateId(any())).thenReturn(expectedId);

 assertEquals(expectedId, writableObjectId.generateId(new Object()));
 }

 @Test(expected = IOException.class)
 public void testGenerateIdWithException() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 when(idGenerator.generateId(any())).thenThrow(IOException.class);

 writableObjectId.generateId(new Object());
 }

 @Test
 public void testWriteAsField() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 id = idGenerator.generateId(new Object());
 SerializableString name = new SerializableString("name");
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeFieldName(anyString())).thenReturn(jsonGenerator);
 when(jsonGenerator.writeString(anyString())).thenReturn(jsonGenerator);
 when(jsonGenerator.writeObject(any())).thenReturn(jsonGenerator);

 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 assertEquals("name", objectIdWriter.propertyName);
 assertEquals(id.toString(), jsonGenerator.getOutputBuffer().toString());
 }
}