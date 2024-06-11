import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private ObjectIdGenerator objectIdGenerator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setUp() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(objectIdGenerator);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
    }

    @Test
    public void testWriteAsId_WhenIdIsNullAndAlwaysAsIdIsFalse_ShouldReturnFalse() throws IOException {
        objectIdWriter.alwaysAsId = false;

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNullAndAlwaysAsIdIsTrue_ShouldReturnFalse() throws IOException {
        objectIdWriter.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrue_ShouldWriteObjectRefAndReturnTrue() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectRef("123");
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndGenCanWriteObjectIdIsTrue_ShouldWriteObjectRefAndReturnTrue() throws IOException {
        writableObjectId.id = 123;
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectRef("123");
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndGenCanWriteObjectIdIsFalse_ShouldSerializeIdAndReturnTrue() throws IOException {
        writableObjectId.id = 123;
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(objectIdWriter.serializer).serialize(123, jsonGenerator, serializerProvider);
        assertTrue(result);
    }

    @Test
    public void testGenerateId_ShouldGenerateIdAndUpdateIdField() {
        Object pojo = new Object();
        Object generatedId = new Object();
        when(objectIdGenerator.generateId(pojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(pojo);

        assertEquals(generatedId, result);
        assertEquals(generatedId, writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalseAndGenCanWriteObjectIdIsTrue_ShouldWriteObjectId() throws IOException {
        writableObjectId.idWritten = false;
        writableObjectId.id = 123;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectId("123");
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalseAndGenCanWriteObjectIdIsFalse_ShouldWriteFieldNameAndSerializeId() throws IOException {
        writableObjectId.idWritten = false;
        writableObjectId.id = 123;
        SerializableString propertyName = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(propertyName);
        verify(objectIdWriter.serializer).serialize(123, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsTrue_ShouldNotWriteObjectId() throws IOException {
        writableObjectId.idWritten = true;
        writableObjectId.id = 123;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeObjectId(anyString());
    }
}