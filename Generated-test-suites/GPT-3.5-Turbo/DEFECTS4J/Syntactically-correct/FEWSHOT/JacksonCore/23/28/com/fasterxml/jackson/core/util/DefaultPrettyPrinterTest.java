import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Root separator is not null
        printer.writeRootValueSeparator(g);
        
        assertEquals(DEFAULT_ROOT_VALUE_SEPARATOR, g.getRaw());
    }
    
    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Object indenter is not inline
        printer.writeStartObject(g);
        
        assertEquals("{", g.getRaw());
        assertEquals(1, printer.getNesting());
    }
    
    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Object indenter is not inline
        printer.beforeObjectEntries(g);
        
        assertEquals("", g.getRaw());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Spaces in object entries enabled
        printer.writeObjectFieldValueSeparator(g);
        
        assertEquals(" " + DEFAULT_SEPARATORS.getObjectFieldValueSeparator() + " ", g.getRaw());
        
        // Spaces in object entries disabled
        printer.withoutSpacesInObjectEntries();
        printer.writeObjectFieldValueSeparator(g);
        
        assertEquals(DEFAULT_SEPARATORS.getObjectFieldValueSeparator(), g.getRaw());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Object indenter is not inline
        printer.writeObjectEntrySeparator(g);
        
        assertEquals(DEFAULT_SEPARATORS.getObjectEntrySeparator(), g.getRaw());
        assertEquals("", g.getRaw());
    }
    
    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Object indenter is not inline
        printer.writeEndObject(g, 1);
        
        assertEquals(" ", g.getRaw());
        assertEquals("}", g.getRaw());
        assertEquals(0, printer.getNesting());
        
        // Object indenter is inline
        printer.writeObjectIndenter(new NopIndenter());
        printer.writeEndObject(g, 1);
        
        assertEquals("", g.getRaw());
        assertEquals("}", g.getRaw());
        assertEquals(0, printer.getNesting());
    }
    
    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Array indenter is not inline
        printer.writeStartArray(g);
        
        assertEquals("[", g.getRaw());
        assertEquals(1, printer.getNesting());
    }
    
    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Array indenter is not inline
        printer.beforeArrayValues(g);
        
        assertEquals("", g.getRaw());
    }
    
    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Array indenter is not inline
        printer.writeArrayValueSeparator(g);
        
        assertEquals(DEFAULT_SEPARATORS.getArrayValueSeparator(), g.getRaw());
        assertEquals("", g.getRaw());
    }
    
    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        
        // Array indenter is not inline
        printer.writeEndArray(g, 1);
        
        assertEquals(" ", g.getRaw());
        assertEquals("]", g.getRaw());
        assertEquals(0, printer.getNesting());
        
        // Array indenter is inline
        printer.writeEndArray(g, 1);
        
        assertEquals("", g.getRaw());
        assertEquals("]", g.getRaw());
        assertEquals(0, printer.getNesting());
    }
}