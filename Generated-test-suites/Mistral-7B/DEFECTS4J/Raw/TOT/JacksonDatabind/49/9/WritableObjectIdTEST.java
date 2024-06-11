import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.IntObjectIdGenerator;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ObjectIdWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdGenerator<Integer> idGenerator;
    private ObjectIdWriter idWriter;
    private WritableObjectId idContainer;

    @Before
    public void setup() {
        idGenerator = new IntObjectIdGenerator();
        idWriter = new ObjectIdWriter(null, null);
        provider = mock(SerializerProvider.class);
        gen = mock(JsonGenerator.class);
        idContainer = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsId_IdNotWrittenAndNoAlwaysAsId_DoNotWrite() throws IOException {
        // Given
        when(gen.canWriteObjectId()).thenReturn(true);

        // When
        assertFalse(idContainer.writeAsId(gen, provider, idWriter));

        // Then
        verify(gen, never()).writeObjectRef(anyString());
        verify(idWriter, never()).serializer.serialize(any(), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsId_IdNotWrittenAndNoAlwaysAsId_ForwardReference() throws IOException {
        // Given
        when(gen.canWriteObjectId()).thenReturn(false);

        // When
        assertFalse(idContainer.writeAsId(gen, provider, idWriter));

        // Then
        verify(gen, never()).writeObjectRef(anyString());
        verify(idWriter, never()).serializer.serialize(any(), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsId_IdNotWrittenAndAlwaysAsId_ObjectSerialized() throws IOException {
        // Given
        Object id = 1;
        when(idGenerator.generateId(any())).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(false);

        // When
        idContainer.id = id;
        assertTrue(idContainer.writeAsId(gen, provider, idWriter));

        // Then
        verify(idWriter, times(1)).serializer.serialize(id, eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsId_IdWrittenAndNoAlwaysAsId_ObjectIdWritten() throws IOException {
        // Given
        Object id = 1;
        when(idGenerator.generateId(any())).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(true);
        idContainer.id = id;
        idContainer.idWritten = true;

        // When
        assertTrue(idContainer.writeAsId(gen, provider, idWriter));

        // Then
        verify(gen, times(1)).writeObjectRef(eq("1"));
    }

    @Test
    public void testGenerateId_NoId_NewIdGenerated() throws IOException {
        // Given

        // When
        Object id = idContainer.generateId(null);

        // Then
        assertEquals(id, idContainer.id);
    }

    @Test
    public void testGenerateId_IdPresent_NoChange() throws IOException {
        // Given
        Object id = 1;
        idContainer.id = id;

        // When
        Object newId = idContainer.generateId(null);

        // Then
        assertEquals(id, newId);
        assertEquals(id, idContainer.id);
    }

    @Test(expected = IOException.class)
    public void testGenerateId_ErrorInIdGeneration() throws IOException {
        // Given
        doThrow(new IOException("Test Error")).when(idGenerator).generateId(any());

        // When
        idContainer.generateId(null);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testWriteAsField_ObjectIdWritten() throws IOException {
        // Given
        Object id = 1;
        String propertyName = "name";
        when(gen.canWriteObjectId()).thenReturn(false);

        // When
        idContainer.id = id;
        idContainer.writeAsField(gen, provider, idWriter);

        // Then
        verify(gen, times(1)).writeFieldName(eq("name"));
        verify(idWriter, times(1)).serializer.serialize(eq(id), any(), eq(provider));
    }

    @Test
    public void testWriteAsField_NoObjectIdWritten_NoPropertyName() throws IOException {
        // Given
        when(gen.canWriteObjectId()).thenReturn(false);

        // When
        idContainer.writeAsField(gen, provider, idWriter);

        // Then
        verify(gen, never()).writeFieldName(anyString());
        verify(idWriter, never()).serializer.serialize(any(), any(), eq(provider));
    }

    @Test
    public void testWriteAsField_NoObjectIdWritten_WithPropertyName() throws IOException {
        // Given
        String propertyName = "name";
        when(gen.canWriteObjectId()).thenReturn(false);

        // When
        idContainer.writeAsField(gen, provider, idWriter);

        // Then
        verify(gen, times(1)).writeFieldName(eq(propertyName));
        verify(idWriter, never()).serializer.serialize(any(), any(), eq(provider));
    }
}