import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> generator;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectIdWriter mockObjectIdWriter;
 private WritableObjectId writableObjectId;

 @Before
 public void setUp() {
 generator = mock(ObjectIdGenerator.class);
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockObjectIdWriter = mock(ObjectIdWriter.class);
 writableObjectId = new WritableObjectId(generator);
 }

 @Test
 public void testWriteAsIdWithAlreadyWrittenId() throws IOException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
 when(mockObjectIdWriter.alwaysAsId()).thenReturn(false);
 when(mockObjectIdWriter.propertyName()).thenReturn(new SerializableString("testPropertyName"));

 writableObjectId.id = 1L;
 writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeObjectRef(is("1"));
 }

 @Test
 public void testWriteAsIdWithGeneratedId() throws IOException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 long expectedId = 2L;
 when(generator.generateId(any())).thenReturn(expectedId);
 when(mockObjectIdWriter.alwaysAsId()).thenReturn(false);
 when(mockObjectIdWriter.propertyName()).thenReturn(null);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeFieldName("testPropertyName");
 verify(mockSerializerProvider, times(1)).findValueSerializer(Long.class);
 verify(mockSerializerProvider, times(1)).serializeValue(expectedId, mockJsonGenerator, any());
 }

 @Test
 public void testWriteAsIdWithAlwaysAsId() throws IOException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 when(mockObjectIdWriter.alwaysAsId()).thenReturn(true);
 when(mockObjectIdWriter.propertyName()).thenReturn(null);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeObjectId(is("testObjectId"));
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullGenerator() {
 writableObjectId = new WritableObjectId(null);
 writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullId() {
 writableObjectId.id = null;
 writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
 }

 @Test
 public void testGenerateId() {
 long expectedId = 3L;
 when(generator.generateId(any())).thenReturn(expectedId);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 Object id = writableObjectId.generateId(new Object());

 assertThat(id, is(notNullValue()));
 assertThat(id, is(expectedId));
 }

 @Test
 public void testWriteAsFieldWithNullIdWritten() {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 when(mockObjectIdWriter.propertyName()).thenReturn(new SerializableString("testPropertyName"));

 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeFieldName("testPropertyName");
 }

 @Test
 public void testWriteAsFieldWithIdWritten() throws IOException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 when(mockObjectIdWriter.propertyName()).thenReturn(new SerializableString("testPropertyName"));

 writableObjectId.id = 4L;
 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeObjectId(is("4"));
 }

 @Test
 public void testWriteAsFieldWithNullPropertyName() throws IOException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator, times(1)).writeNull();
 }
}