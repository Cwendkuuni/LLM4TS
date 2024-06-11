import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DefaultPrettyPrinterTest {
    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeRootValueSeparator(null));
        assertEquals(1, defaultPrettyPrinter.writeRootValueSeparator(1));
        assertEquals(0.5, defaultPrettyPrinter.writeRootValueSeparator(0.5));
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeStartObject(null));
        assertEquals(1, defaultPrettyPrinter.writeStartObject(1));
        assertEquals(0.5, defaultPrettyPrinter.writeStartObject(0.5));
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.beforeObjectEntries(null));
        assertEquals("", defaultPrettyPrinter.beforeObjectEntries(""));
        assertEquals("abc", defaultPrettyPrinter.beforeObjectEntries("abc"));
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeObjectFieldValueSeparator(null));
        assertEquals(1, defaultPrettyPrinter.writeObjectFieldValueSeparator(1));
        assertEquals(0.5, defaultPrettyPrinter.writeObjectFieldValueSeparator(0.5));
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeObjectEntrySeparator(null));
        assertEquals("", defaultPrettyPrinter.writeObjectEntrySeparator(""));
        assertEquals("abc", defaultPrettyPrinter.writeObjectEntrySeparator("abc"));
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeEndObject(null));
        assertEquals(1, defaultPrettyPrinter.writeEndObject(1));
        assertEquals(0.5, defaultPrettyPrinter.writeEndObject(0.5));
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeStartArray(null));
        assertEquals("", defaultPrettyPrinter.writeStartArray(""));
        assertEquals("abc", defaultPrettyPrinter.writeStartArray("abc"));
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.beforeArrayValues(null));
        assertEquals(1, defaultPrettyPrinter.beforeArrayValues(1));
        assertEquals(0.5, defaultPrettyPrinter.beforeArrayValues(0.5));
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeArrayValueSeparator(null));
        assertEquals("", defaultPrettyPrinter.writeArrayValueSeparator(""));
        assertEquals("abc", defaultPrettyPrinter.writeArrayValueSeparator("abc"));
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(null, defaultPrettyPrinter.writeEndArray(null));
        assertEquals(1, defaultPrettyPrinter.writeEndArray(1));
        assertEquals(0.5, defaultPrettyPrinter.writeEndArray(0.5));
    }
}