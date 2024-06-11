import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> generator;
    private WritableObjectId objectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private SerializableString serializableString;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        objectId = new WritableObjectId(generator);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        serializableString = mock(SerializableString.class);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrue_ThenReturnsTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        objectIdWriter.alwaysAsId = false;

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdTrue_ThenReturnsTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrue_ThenReturnsTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        objectIdWriter.alwaysAsId = true;

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalseAndCanWriteObjectIdTrue_ThenReturnsTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalseAndCanWriteObjectIdFalse_ThenReturnsFalse() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdNullAndIdWrittenTrue_ThenReturnsFalse() throws IOException {
        objectId.id = null;
        objectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdNullAndIdWrittenFalse_ThenReturnsFalse() throws IOException {
        objectId.id = null;
        objectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;

        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertFalse(result);
    }

    @Test
    public void testGenerateId_WhenForPojoNotNull_ThenGenerateId() {
        Object forPojo = new Object();

        objectId.generateId(forPojo);

        verify(generator, times(1)).generateId(forPojo);
        assertNotNull(objectId.id);
        assertEquals(objectId.id, generator.generateId(forPojo));
    }

    @Test
    public void testGenerateId_WhenForPojoNull_ThenDoNotGenerateId() {
        Object forPojo = null;

        objectId.generateId(forPojo);

        verify(generator, times(0)).generateId(forPojo);
        assertNull(objectId.id);
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdTrue_ThenWriteObjectIdAndReturn() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeObjectId(String.valueOf(objectId.id));
        verify(jsonGenerator, times(0)).writeFieldName(serializableString);
        verify(objectIdWriter, times(0)).serializer.serialize(objectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdFalseAndPropertyNameNotNull_ThenWriteFieldNameAndSerializeId() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(serializableString);

        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(0)).writeObjectId(String.valueOf(objectId.id));
        verify(jsonGenerator, times(1)).writeFieldName(serializableString);
        verify(objectIdWriter, times(1)).serializer.serialize(objectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdFalseAndPropertyNameNull_ThenDoNotWriteFieldNameAndSerializeId() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(null);

        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(0)).writeObjectId(String.valueOf(objectId.id));
        verify(jsonGenerator, times(0)).writeFieldName(serializableString);
        verify(objectIdWriter, times(1)).serializer.serialize(objectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenFalse_ThenDoNotWriteFieldNameAndSerializeId() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(serializableString);

        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(0)).writeObjectId(String.valueOf(objectId.id));
        verify(jsonGenerator, times(0)).writeFieldName(serializableString);
        verify(objectIdWriter, times(0)).serializer.serialize(objectId.id, jsonGenerator, serializerProvider);
    }
}