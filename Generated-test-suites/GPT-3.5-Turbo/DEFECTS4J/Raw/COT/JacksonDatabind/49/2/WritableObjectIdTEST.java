import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;

class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @BeforeEach
    void setUp() {
        ObjectIdGenerator<?> generator = Mockito.mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    void testWriteAsIdWithNonNullIdAndIdWritten() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertTrue(result);
        Mockito.verify(gen).writeObjectRef(String.valueOf(writableObjectId.id));
        Mockito.verifyNoMoreInteractions(gen, provider, w);
    }

    @Test
    void testWriteAsIdWithNonNullIdAndNotIdWritten() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        Mockito.verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        Mockito.verifyNoMoreInteractions(gen, provider, w);
    }

    @Test
    void testWriteAsIdWithNullId() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        writableObjectId.id = null;
        writableObjectId.idWritten = true;

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        Mockito.verifyNoMoreInteractions(gen, provider, w);
    }

    @Test
    void testGenerateId() {
        Object forPojo = new Object();
        ObjectIdGenerator<Object> generator = Mockito.mock(ObjectIdGenerator.class);
        writableObjectId.generator = generator;
        Object expectedId = new Object();
        Mockito.when(generator.generateId(forPojo)).thenReturn(expectedId);

        Object generatedId = writableObjectId.generateId(forPojo);

        assertSame(expectedId, generatedId);
    }

    @Test
    void testWriteAsFieldWithCanWriteObjectId() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        writableObjectId.id = new Object();
        gen.writeObjectId("objectId");

        writableObjectId.writeAsField(gen, provider, w);

        Mockito.verifyNoMoreInteractions(gen, provider, w);
    }

    @Test
    void testWriteAsFieldWithPropertyNameNotNull() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        writableObjectId.id = new Object();
        w.propertyName = Mockito.mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, w);

        Mockito.verify(gen).writeFieldName(w.propertyName);
        Mockito.verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        Mockito.verifyNoMoreInteractions(gen, provider, w);
    }
}