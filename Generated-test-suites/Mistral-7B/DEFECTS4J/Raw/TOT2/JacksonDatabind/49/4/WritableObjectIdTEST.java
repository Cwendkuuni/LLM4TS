import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.ObjectIdWriter.Serializer;
import org.mockito.Mockito;

public class WritableObjectIdTest {

  @Test
  public void testWriteAsId() throws IOException {
    ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
    JsonGenerator genMock = mock(JsonGenerator.class);
    SerializerProvider providerMock = mock(SerializerProvider.class);
    ObjectIdWriter wMock = mock(ObjectIdWriter.class);

    WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

    when(generatorMock.generateId(null)).thenReturn("id1");
    when(genMock.canWriteObjectId()).thenReturn(true);
    when(providerMock.getObjectIdWriter(null)).thenReturn(wMock);

    writableObjectId.writeAsId(genMock, providerMock, wMock);

    verify(genMock).writeObjectRef("id1");
  }

  @Test
  public void testWriteAsId_WriteObjectIdWhenCanWriteObjectIdIsFalse() throws IOException {
    ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
    JsonGenerator genMock = mock(JsonGenerator.class);
    SerializerProvider providerMock = mock(SerializerProvider.class);
    ObjectIdWriter wMock = mock(ObjectIdWriter.class);
    Serializer serializerMock = mock(Serializer.class);

    WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

    when(generatorMock.generateId(null)).thenReturn(null);
    when(genMock.canWriteObjectId()).thenReturn(false);
    when(providerMock.getObjectIdWriter(null)).thenReturn(wMock);
    when(wMock.serializer).thenReturn(serializerMock);

    Object testObject = new Object();

    writableObjectId.writeAsId(genMock, providerMock, wMock);

    verify(wMock).serializer.serialize(Mockito.any(), Mockito.eq(genMock), Mockito.eq(providerMock));
  }

  @Test
  public void testGenerateId() {
    ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
    WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
    Object testObject = new Object();

    when(generatorMock.generateId(testObject)).thenReturn("id1");

    assertEquals("id1", writableObjectId.generateId(testObject));
  }

  @Test
  public void testGenerateId_UseExistingId() {
    ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
    WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
    Object testObject = new Object() {
      Object id = "id1";
    };

    when(generatorMock.generateId(testObject)).thenReturn(null);

    assertEquals("id1", writableObjectId.generateId(testObject));
  }

  @Test(expected = IOException.class)
  public void testWriteAsField() throws IOException {
    JsonGenerator genMock = mock(JsonGenerator.class);
    SerializerProvider providerMock = mock(SerializerProvider.class);
    ObjectIdWriter wMock = mock(ObjectIdWriter.class);

    WritableObjectId writableObjectId = new WritableObjectId(null);

    writableObjectId.writeAsField(genMock, providerMock, wMock);
  }

  @Test
  public void testWriteAsField_WriteObjectIdWhenCanWriteObjectIdIsFalse() throws IOException {
    JsonGenerator genMock = mock(JsonGenerator.class);
    SerializerProvider providerMock = mock(SerializerProvider.class);
    ObjectIdWriter wMock = mock(ObjectIdWriter.class);

    WritableObjectId writableObjectId = new WritableObjectId(null);

    when(genMock.canWriteObjectId()).thenReturn(false);

    writableObjectId.writeAsField(genMock, providerMock, wMock);

    verify(wMock, times(1)).serializer.serialize(Mockito.any(), Mockito.eq(genMock), Mockito.eq(providerMock));
  }

}