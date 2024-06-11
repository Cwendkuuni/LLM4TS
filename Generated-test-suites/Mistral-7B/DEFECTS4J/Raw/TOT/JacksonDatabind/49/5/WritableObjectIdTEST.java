import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private ObjectIdWriter idWriter;
 private JsonGenerator jsonGenerator;
 private SerializerProvider provider;
 private WritableObjectId writableObjectId;

 @Before
 public void setUp() {
 ObjectMapper mapper = new ObjectMapper();
 idGenerator = mock(ObjectIdGenerator.class);
 idWriter = new ObjectIdWriter(mapper.getSerializerProvider());
 jsonGenerator = mock(JsonGenerator.class);
 provider = new SerializerProvider();
 writableObjectId = new WritableObjectId(idGenerator);
 }

 @Test
 public void testWriteAsIdTrue() throws Exception {
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(idGenerator.generateId(any())).thenReturn("id1");
 when(idWriter.serializer.serialize(any(), eq(jsonGenerator), eq(provider))).thenThrow(IOException.class);

 assertTrue(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
 verify(jsonGenerator).writeObjectRef("id1");
 }

 @Test
 public void testWriteAsIdFalse() throws Exception {
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(idGenerator.generateId(any())).thenReturn("id1");

 assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
 verifyZeroInteractions(jsonGenerator);
 }

 @Test
 public void testWriteAsIdAlwaysAsId() throws Exception {
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(idGenerator.generateId(any())).thenReturn("id1");
 when(idWriter.alwaysAsId).thenReturn(true);

 assertTrue(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
 verify(jsonGenerator).writeObjectId("id1");
 }

 @Test
 public void testGenerateId() {
 Object pojo = new Object();
 String expectedId = "id2";
 when(idGenerator.generateId(eq(pojo))).thenReturn(expectedId);

 String actualId = writableObjectId.generateId(pojo);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testWriteAsFieldTrue() throws Exception {
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(idGenerator.generateId(any())).thenReturn("id1");
 when(idWriter.propertyName).thenReturn(new SerializableString("name"));

 writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
 verify(jsonGenerator).writeObjectId("id1");
 }

 @Test
 public void testWriteAsFieldFalse() throws Exception {
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(idGenerator.generateId(any())).thenReturn("id1");
 when(idWriter.propertyName).thenReturn(null);

 writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
 verify(jsonGenerator).writeFieldName("name");
 verify(idWriter.serializer).serialize(eq("id1"), eq(jsonGenerator), eq(provider));
 }
}