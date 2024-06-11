import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WithIdNonNullAndIdWrittenTrue_ReturnsTrue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
        verify(gen).writeObjectRef(eq("123"));
        verifyZeroInteractions(generator);
    }

    @Test
    public void testWriteAsId_WithIdNonNullAndIdWrittenFalseButAlwaysAsIdTrue_ReturnsTrueAndWritesId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
        verify(gen).writeObjectRef(eq("123"));
        verifyZeroInteractions(generator);
    }

    @Test
    public void testWriteAsId_WithIdNonNullAndIdWrittenFalseAndAlwaysAsIdFalse_ReturnsFalse() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
        verifyZeroInteractions(gen, generator);
    }

    @Test
    public void testWriteAsId_WithIdNull_ReturnsFalse() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
        verifyZeroInteractions(gen, generator);
    }

    @Test
    public void testGenerateId_WithForPojo_ReturnsGeneratedId() {
        Object forPojo = mock(Object.class);
        Object generatedId = "456";

        when(generator.generateId(forPojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(forPojo);

        assertEquals(generatedId, result);
        verify(generator).generateId(forPojo);
    }

    @Test
    public void testWriteAsField_WithIdNotWritten_WritesIdAsField() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
        verify(gen).writeFieldName(writer.propertyName);
        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
        verifyZeroInteractions(generator);
    }

    @Test
    public void testWriteAsField_WithIdWritten_WritesObjectId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
        verify(gen).writeObjectId("123");
        verifyZeroInteractions(generator);
    }

    @Test
    public void testWriteAsField_WithIdWrittenAndPropertyNameNull_WritesObjectId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
        verify(gen).writeObjectId("123");
        verifyZeroInteractions(generator);
    }

}