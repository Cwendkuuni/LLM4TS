import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {
    
    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    private SerializableString name;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = mock(ObjectIdWriter.class);
        name = mock(SerializableString.class);
        
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalseAndAlwaysAsIdFalse() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.serializer.serialize(any(), any(), any())).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertTrue(result);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalseAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.serializer.serialize(any(), any(), any())).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertTrue(result);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndAlwaysAsIdFalse() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        w.alwaysAsId = false;

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.serializer.serialize(any(), any(), any())).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertTrue(result);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.serializer.serialize(any(), any(), any())).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertTrue(result);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_withNullIdAndIdWrittenFalseAndAlwaysAsIdFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;

        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsId_withNullIdAndIdWrittenFalseAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsId_withNullIdAndIdWrittenTrueAndAlwaysAsIdFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        w.alwaysAsId = false;

        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsId_withNullIdAndIdWrittenTrueAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        assertFalse(result);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsField_withNonNullIdAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId(String.valueOf(writableObjectId.id));
        assertTrue(writableObjectId.idWritten);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsField_withNonNullIdAndCanWriteObjectIdFalse() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.propertyName).thenReturn(name);

        when(name.toString()).thenReturn("TestName");

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeFieldName(name);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_withNullIdAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);

        assertFalse(writableObjectId.idWritten);
        verifyNoInteractions(gen);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testWriteAsField_withNullIdAndCanWriteObjectIdFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;

        when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(gen, provider, w);

        assertFalse(writableObjectId.idWritten);
        verifyNoInteractions(gen);
        verifyNoInteractions(w.serializer);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();

        writableObjectId.generateId(forPojo);

        verify(generator).generateId(forPojo);
        assertEquals(writableObjectId.id, writableObjectId.id);
    }
}