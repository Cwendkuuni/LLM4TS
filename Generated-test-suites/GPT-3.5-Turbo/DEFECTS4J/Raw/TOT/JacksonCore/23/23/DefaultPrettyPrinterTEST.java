import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter printer;

    @Before
    public void setUp() {
        printer = new DefaultPrettyPrinter();
    }

    // Test writeRootValueSeparator
    @Test
    public void testWriteRootValueSeparator() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeRootValueSeparator(generator);
        
        verify(generator).writeRaw(any(SerializedString.class));
    }

    // Test writeStartObject
    @Test
    public void testWriteStartObject() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeStartObject(generator);
        
        verify(generator).writeRaw('{');
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test beforeObjectEntries
    @Test
    public void testBeforeObjectEntries() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.beforeObjectEntries(generator);
        
        verify(generator, never()).writeRaw('(');
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test writeObjectFieldValueSeparator
    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeObjectFieldValueSeparator(generator);
        
        verify(generator).writeRaw(any(SerializedString.class));
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test writeObjectEntrySeparator
    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeObjectEntrySeparator(generator);
        
        verify(generator).writeRaw(any(SerializedString.class));
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test writeEndObject
    @Test
    public void testWriteEndObject() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeEndObject(generator, 0);
        
        verify(generator).writeRaw(anyChar());
        verify(generator, never()).writeRaw(any(SerializedString.class));
    }

    // Test writeStartArray
    @Test
    public void testWriteStartArray() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeStartArray(generator);
        
        verify(generator).writeRaw(anyChar());
        verify(generator, never()).writeRaw(any(SerializedString.class));
    }

    // Test beforeArrayValues
    @Test
    public void testBeforeArrayValues() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.beforeArrayValues(generator);
        
        verify(generator, never()).writeRaw('(');
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test writeArrayValueSeparator
    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeArrayValueSeparator(generator);
        
        verify(generator).writeRaw(any(SerializedString.class));
        verify(generator, never()).writeRaw(anyChar());
    }

    // Test writeEndArray
    @Test
    public void testWriteEndArray() throws Exception {
        JsonGenerator generator = mock(JsonGenerator.class);
        printer.writeEndArray(generator, 0);
        
        verify(generator).writeRaw(anyChar());
        verify(generator, never()).writeRaw(any(SerializedString.class));
    }
}