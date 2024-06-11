import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setUp() {
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteStartObject() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyChar());
    }

    @Test
    public void testBeforeObjectEntries() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteEndObject() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 0);
        verify(jsonGenerator).writeRaw(anyChar());
    }

    @Test
    public void testWriteStartArray() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyChar());
    }

    @Test
    public void testBeforeArrayValues() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(anyString());
    }

    @Test
    public void testWriteEndArray() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 0);
        verify(jsonGenerator).writeRaw(anyChar());
    }
}