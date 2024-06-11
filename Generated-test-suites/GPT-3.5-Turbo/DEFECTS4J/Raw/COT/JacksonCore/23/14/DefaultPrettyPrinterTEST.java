
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Root separator is null, no separator should be printed
        assertEquals("", printer.writeRootValueSeparator());
    }
    
    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Start object should write '{' and increase nesting level
        assertEquals("{}", printer.writeStartObject());
        assertEquals(1, printer.getNesting());
    }
    
    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Before object entries should write indentation based on nesting level
        assertEquals("", printer.beforeObjectEntries());
        printer.increaseNesting();
        assertEquals("  ", printer.beforeObjectEntries());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // By default, spaces should be used around object field value separator
        assertEquals(" : ", printer.writeObjectFieldValueSeparator());
        // Disable spaces in object entries
        printer.withoutSpacesInObjectEntries();
        assertEquals(":", printer.writeObjectFieldValueSeparator());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Object entry separator should write separator and indentation based on nesting level
        assertEquals("", printer.writeObjectEntrySeparator());
        printer.increaseNesting();
        assertEquals(",\n  ", printer.writeObjectEntrySeparator());
    }
    
    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // End object should decrease nesting level and write '}' with appropriate indentation
        assertEquals("{}", printer.writeEndObject(0));
        assertEquals(0, printer.getNesting());
        printer.increaseNesting();
        assertEquals("\n}", printer.writeEndObject(0));
        assertEquals(0, printer.getNesting());
    }
    
    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Start array should write '[' and increase nesting level
        assertEquals("[]", printer.writeStartArray());
        assertEquals(1, printer.getNesting());
    }
    
    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Before array values should write indentation based on nesting level
        assertEquals("", printer.beforeArrayValues());
        printer.increaseNesting();
        assertEquals("  ", printer.beforeArrayValues());
    }
    
    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Array value separator should write separator and indentation based on nesting level
        assertEquals("", printer.writeArrayValueSeparator());
        printer.increaseNesting();
        assertEquals(",\n  ", printer.writeArrayValueSeparator());
    }
    
    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // End array should decrease nesting level and write ']' with appropriate indentation
        assertEquals("[]", printer.writeEndArray(0));
        assertEquals(0, printer.getNesting());
        printer.increaseNesting();
        assertEquals("\n]", printer.writeEndArray(0));
        assertEquals(0, printer.getNesting());
    }
}
