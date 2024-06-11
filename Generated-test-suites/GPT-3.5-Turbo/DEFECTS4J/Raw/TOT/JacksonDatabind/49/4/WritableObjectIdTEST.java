
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_WithNonNullIdAndIdWrittenFalse_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = "123";

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithNonNullIdAndIdWrittenTrue_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = "123";
        objectId.idWritten = true;

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithNonNullIdAndAlwaysAsIdTrue_ReturnsTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = "123";
        w.alwaysAsId = true;

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithNullIdAndIdWrittenFalse_ReturnsFalse() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = null;

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WithNullIdAndIdWrittenTrue_ReturnsFalse() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = null;
        objectId.idWritten = true;

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WithNullIdAndAlwaysAsIdTrue_ReturnsFalse() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = null;
        w.alwaysAsId = true;

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGenerateId_WithNonNullId_ReturnsId() throws IOException {
        // Arrange
        Object forPojo = new Object();
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(objectIdGenerator);

        objectId.id = null;

        // Act
        Object result = objectId.generateId(forPojo);

        // Assert
        assertNotNull(result);
        assertEquals(result, objectId.id);
    }

    @Test
    public void testGenerateId_WithNullId_ReturnsId() throws IOException {
        // Arrange
        Object forPojo = new Object();
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(objectIdGenerator);

        objectId.id = "123";

        // Act
        Object result = objectId.generateId(forPojo);

        // Assert
        assertNotNull(result);
        assertEquals(result, objectId.id);
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalseAndGenCanWriteObjectIdTrue_WritesObjectId() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = "123";

        // Act
        objectId.writeAsField(gen, provider, w);

        // Assert
        verify(gen).writeObjectId(String.valueOf(objectId.id));
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalseAndGenCanWriteObjectIdFalse_WritesFieldNameAndId() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());
        SerializableString name = mock(SerializableString.class);
        when(w.propertyName).thenReturn(name);

        objectId.id = "123";

        // Act
        objectId.writeAsField(gen, provider, w);

        // Assert
        verify(gen).writeFieldName(name);
        verify(w.serializer).serialize(objectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_WithIdWrittenTrueAndGenCanWriteObjectIdTrue_DoesNotWriteAnything() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator());

        objectId.id = "123";
        objectId.idWritten = true;

        // Act
        objectId.writeAsField(gen, provider, w);

        // Assert
        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(w.serializer, never()).serialize(any(Object.class), eq(gen), eq(provider));
    }
}

