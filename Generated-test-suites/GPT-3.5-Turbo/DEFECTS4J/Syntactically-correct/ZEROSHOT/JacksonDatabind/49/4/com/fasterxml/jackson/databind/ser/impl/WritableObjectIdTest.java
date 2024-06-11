
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndAlwaysAsId_returnsTrue() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(objectIdWriter.alwaysAsId).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndAlwaysAsIdFalse_returnsTrue() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalse_returnsFalse() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_withNullId_returnsFalse() throws IOException {
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId_callsGeneratorGenerateId() {
        Object forPojo = new Object();
        when(generator.generateId(forPojo)).thenReturn("123");

        Object generatedId = writableObjectId.generateId(forPojo);

        assertEquals("123", generatedId);
    }

    @Test
    public void testWriteAsField_withCanWriteObjectIdTrue_callsWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectId("123");
    }

    @Test
    public void testWriteAsField_withCanWriteObjectIdFalse_andPropertyNameNull_doesNotWriteField() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeFieldName(any(SerializableString.class));
    }

    @Test
    public void testWriteAsField_withCanWriteObjectIdFalse_andPropertyNameNotNull_writesField() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString propertyName = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(propertyName);
    }

    @Test
    public void testWriteAsField_withNullId_doesNotWriteField() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString propertyName = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeFieldName(any(SerializableString.class));
    }

}
