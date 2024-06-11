import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectIdWriter mockObjectIdWriter;
 private ObjectIdGenerator<Object> mockObjectIdGenerator;
 private WritableObjectId writableObjectId;

 @Before
 public void setUp() {
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockObjectIdWriter = mock(ObjectIdWriter.class);
 mockObjectIdGenerator = mock(ObjectIdGenerator.class);

 writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 }

 @Test
 public void testWriteAsIdWithNullGenerator() {
 ObjectIdGenerator<?> generator = null;
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
 }

 @Test
 public void testWriteAsIdWithNullId() {
 when(mockObjectIdWriter.alwaysAsId).thenReturn(false);
 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
 }

 @Test
 public void testWriteAsIdWithWrittenId() {
 Object id = new Object();
 when(mockObjectIdWriter.alwaysAsId).thenReturn(false);
 writableObjectId.id = id;
 writableObjectId.idWritten = true;
 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
 }

 @Test
 public void testWriteAsIdWithAlwaysAsId() {
 Object id = new Object();
 when(mockObjectIdWriter.alwaysAsId).thenReturn(true);
 writableObjectId.id = id;
 writableObjectId.idWritten = false;
 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
 }

 @Test
 public void testGenerateIdWithNullForPojo() {
 assertNull(writableObjectId.generateId(null));
 }

 @Test
 public void testGenerateIdWithNonNullForPojo() {
 Object forPojo = new Object();
 Object id = new Object();
 when(mockObjectIdGenerator.generateId(eq(forPojo))).thenReturn(id);
 assertEquals(id, writableObjectId.generateId(forPojo));
 }

 @Test
 public void testWriteAsFieldWithNullGenerator() {
 ObjectIdWriter objectIdWriter = null;
 WritableObjectId writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 try {
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, objectIdWriter);
 fail("should have thrown exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testWriteAsFieldWithNullId() {
 ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);
 WritableObjectId writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 try {
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, objectIdWriter);
 fail("should have thrown exception");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testWriteAsFieldWithNullPropertyName() {
 ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);
 WritableObjectId writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 SerializableString propertyName = null;
 try {
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, objectIdWriter);
 fail("should have thrown exception");
 } catch (JsonMappingException e) {
 assertEquals("Null propertyName not allowed", e.getMessage());
 }
 }

 @Test
 public void testWriteAsFieldWithNullSerializer() {
 ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);
 WritableObjectId writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 TypeSerializer typeSerializer = null;
 when(mockObjectIdWriter.serializer).thenReturn(typeSerializer);
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 SerializableString propertyName = new SerializableString("testPropertyName");
 try {
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, objectIdWriter);
 fail("should have thrown exception");
 } catch (JsonGenerationException e) {
 assertEquals("Serializing instance of com.fasterxml.jackson.databind.ser.impl.WritableObjectId to JSON", e.getMessage());
 }
 }

 @Test
 public void testWriteAsFieldWithValidParameters() {
 ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);
 WritableObjectId writableObjectId = new WritableObjectId(mockObjectIdGenerator);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 when(mockObjectIdWriter.serializer).thenReturn(typeSerializer);
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 SerializableString propertyName = new SerializableString("testPropertyName");
 Object id = new Object();
 writableObjectId.id = id;
 try {
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, objectIdWriter);
 verify(mockJsonGenerator).writeFieldName(propertyName);
 verify(mockObjectIdWriter.serializer).serialize(id, eq(mockJsonGenerator), eq(mockSerializerProvider));
 } catch (IOException e) {
 fail("should not have thrown exception");
 }
 }

}