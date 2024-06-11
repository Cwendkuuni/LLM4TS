import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private WritableObjectId id;
    private ObjectIdGenerator<?> generator;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter writer;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        id = new WritableObjectId(generator);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        writer = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(generator.generateId(any())).thenReturn("123");
        when(gen.canWriteObjectId()).thenReturn(true);

        id.id = "123";
        id.idWritten = true;

        assertTrue(id.writeAsId(gen, provider, writer));

        verify(gen).writeObjectRef(eq("123"));
        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(writer, never()).serializer.serialize(any(), eq(gen), eq(provider));

        reset(gen, writer);

        id.idWritten = false;
        id.writeAsId(gen, provider, writer);

        verify(gen).writeFieldName(any(SerializableString.class));
        verify(writer).serializer.serialize(eq("123"), eq(gen), eq(provider));
        verify(gen, never()).writeObjectRef(anyString());
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);

        id.id = "123";

        id.writeAsField(gen, provider, writer);

        verify(gen).writeObjectId(eq("123"));
        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(writer, never()).serializer.serialize(any(), eq(gen), eq(provider));

        reset(gen, writer);

        id.id = null;
        id.writeAsField(gen, provider, writer);

        verify(gen, never()).writeObjectId(anyString());
        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(writer, never()).serializer.serialize(any(), eq(gen), eq(provider));

        reset(gen, writer);

        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName).thenReturn(mock(SerializableString.class));

        id.id = "123";
        id.writeAsField(gen, provider, writer);

        verify(gen, never()).writeObjectId(anyString());
        verify(gen).writeFieldName(any(SerializableString.class));
        verify(writer).serializer.serialize(eq("123"), eq(gen), eq(provider));
    }

    @Test
    public void testGenerateId() {
        Object obj = new Object();
        Object generatedId = new Object();

        when(generator.generateId(eq(obj))).thenReturn(generatedId);

        assertEquals(generatedId, id.generateId(obj));
        assertEquals(generatedId, id.id);
    }
}