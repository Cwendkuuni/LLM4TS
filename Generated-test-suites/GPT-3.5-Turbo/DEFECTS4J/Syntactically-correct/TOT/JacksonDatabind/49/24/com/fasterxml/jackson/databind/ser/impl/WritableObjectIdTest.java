import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_Success() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Set id and idWritten to true
        writableObjectId.id = 123;
        writableObjectId.idWritten = true;

        // Perform the test
        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        // Verifying the behaviour
        Mockito.verify(gen).writeObjectRef("123");
        Mockito.verifyZeroInteractions(provider);
        Mockito.verifyZeroInteractions(w.serializer);
    }

    @Test
    public void testWriteAsId_Failure_IfIdIsNull() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Set id to null
        writableObjectId.id = null;

        // Perform the test
        assertFalse(writableObjectId.writeAsId(gen, provider, w));

        // Verifying the behaviour
        Mockito.verifyZeroInteractions(gen);
        Mockito.verifyZeroInteractions(provider);
        Mockito.verifyZeroInteractions(w.serializer);
    }

    @Test
    public void testWriteAsId_Failure_IfIdNotWrittenAndNotAlwaysAsId() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Set idWritten and alwaysAsId to false
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;

        // Perform the test
        assertFalse(writableObjectId.writeAsId(gen, provider, w));

        // Verifying the behaviour
        Mockito.verifyZeroInteractions(gen);
        Mockito.verifyZeroInteractions(provider);
        Mockito.verifyZeroInteractions(w.serializer);
    }

    @Test
    public void testWriteAsField_Success_WithNativeObjectIdSupported() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Enable native object id support
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);

        // Set id and idWritten to true
        writableObjectId.id = 123;
        writableObjectId.idWritten = true;

        // Perform the test
        writableObjectId.writeAsField(gen, provider, w);

        // Verifying the behaviour
        Mockito.verify(gen).writeObjectId("123");
        Mockito.verifyZeroInteractions(provider);
        Mockito.verifyZeroInteractions(w.serializer);
    }

    @Test
    public void testWriteAsField_Success_WithoutNativeObjectIdSupport() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Disable native object id support
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);

        // Set id
        writableObjectId.id = "test_id";
        writableObjectId.idWritten = true;

        // Perform the test
        writableObjectId.writeAsField(gen, provider, w);

        // Verifying the behaviour
        Mockito.verifyZeroInteractions(provider);
        Mockito.verify(gen).writeFieldName(w.propertyName);
        Mockito.verify(w.serializer).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_Failure_IfWriteFieldNameIsNull() throws IOException {
        // Initialize test data
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Disable native object id support
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);

        // Set idWritten to true and propertyName to null
        writableObjectId.idWritten = true;
        w.propertyName = null;

        // Perform the test
        writableObjectId.writeAsField(gen, provider, w);

        // Verifying the behaviour
        Mockito.verifyZeroInteractions(gen);
        Mockito.verifyZeroInteractions(provider);
        Mockito.verifyZeroInteractions(w.serializer);
    }

    @Test
    public void testGenerateId_Success() {
        // Initialize test data
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));

        // Generate id
        Object id = writableObjectId.generateId(forPojo);

        // Verify the generated id
        assertNotNull(id);
        assertEquals(id, writableObjectId.id);
    }

}