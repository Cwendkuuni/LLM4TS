import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void writeAsId_WhenIdIsNotNullAndIdWrittenIsTrue_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = 1;
        writableObjectId.idWritten = true;

        // Act
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertTrue(result);
        verify(mockGenerator).writeObjectRef("1");
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void writeAsId_WhenIdIsNotNullAndIdWrittenIsFalseButWriterAlwaysAsIdIsTrue_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;

        // Act
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertTrue(result);
        verify(mockGenerator).writeObjectRef("1");
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void writeAsId_WhenIdIsNull_ReturnsFalse() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = null;
        writableObjectId.idWritten = false;

        // Act
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertFalse(result);
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void writeAsId_WhenIdIsNullAndWriterAlwaysAsIdIsTrue_ReturnsFalse() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = null;
        writableObjectId.idWritten = false;

        // Act
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertFalse(result);
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void generateId_WhenForPojoIsNotNull_GeneratesAndReturnsId() {
        // Arrange
        ObjectIdGenerator<Object> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        Object forPojo = new Object();

        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        assertNotNull(result);
        verify(mockGenerator).generateId(forPojo);
        verifyNoMoreInteractions(mockGenerator);
    }
    
    @Test
    public void generateId_WhenForPojoIsNull_GeneratesAndReturnsId() {
        // Arrange
        ObjectIdGenerator<Object> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        Object forPojo = null;

        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        assertNotNull(result);
        verify(mockGenerator).generateId(forPojo);
        verifyNoMoreInteractions(mockGenerator);
    }

    @Test
    public void writeAsField_WhenIdWrittenIsFalseAndGeneratorCanWriteObjectId_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;

        // Act
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertTrue(writableObjectId.idWritten);
        verify(mockGenerator).canWriteObjectId();
        verify(mockGenerator).writeObjectId("1");
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void writeAsField_WhenIdWrittenIsFalseAndGeneratorCannotWriteObjectId_WritesFieldNameAndId() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;
        when(mockWriter.propertyName()).thenReturn(mock(SerializableString.class));

        // Act
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertTrue(writableObjectId.idWritten);
        verify(mockGenerator, never()).canWriteObjectId();
        verify(mockGenerator, never()).writeObjectId(anyString());
        verify(mockGenerator).writeFieldName(any(SerializableString.class));
        verify(mockWriter).propertyName();
        verify(mockWriter).serializer();
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }

    @Test
    public void writeAsField_WhenIdWrittenIsTrueAndGeneratorCanWriteObjectId_DoesNotWriteId() throws IOException {
        // Arrange
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        WritableObjectId writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
        writableObjectId.id = 1;
        writableObjectId.idWritten = true;

        // Act
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        // Assert
        assertTrue(writableObjectId.idWritten);
        verify(mockGenerator).canWriteObjectId();
        verify(mockGenerator, never()).writeObjectId(anyString());
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter);
    }
}