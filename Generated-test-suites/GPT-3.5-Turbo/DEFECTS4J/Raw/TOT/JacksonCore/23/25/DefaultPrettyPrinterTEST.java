import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeRootValueSeparator(generator);
        
        assertEquals("The root value separator should be empty string", "", generator.getOutput());
    }
    
    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeStartObject(generator);
        
        assertEquals("The output should be equal to '{'", "{", generator.getOutput());
        assertEquals("The nesting level should be increased by 1", 1, printer._nesting);
    }
    
    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.beforeObjectEntries(generator);
        
        assertEquals("The output should be empty string", "", generator.getOutput());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator_whenSpacesInObjectEntriesEnabled() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeObjectFieldValueSeparator(generator);
        
        assertEquals("The output should be equal to ' : '", " : ", generator.getOutput());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator_whenSpacesInObjectEntriesDisabled() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter().withoutSpacesInObjectEntries();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeObjectFieldValueSeparator(generator);
        
        assertEquals("The output should be equal to ':'", ":", generator.getOutput());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.writeObjectEntrySeparator(generator);
        
        assertEquals("The output should be equal to ', '", ", ", generator.getOutput());
    }
    
    @Test
    public void testWriteEndObject_withNonZeroNrOfEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 2;
        
        printer.writeEndObject(generator, 3);
        
        assertEquals("The output should be equal to '  }'", "  }", generator.getOutput());
        assertEquals("The nesting level should be decreased by 1", 1, printer._nesting);
    }
    
    @Test
    public void testWriteEndObject_withZeroNrOfEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.writeEndObject(generator, 0);
        
        assertEquals("The output should be equal to ' }'", " }", generator.getOutput());
        assertEquals("The nesting level should not be changed", 1, printer._nesting);
    }
    
    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeStartArray(generator);
        
        assertEquals("The output should be equal to '['", "[", generator.getOutput());
        assertEquals("The nesting level should be increased by 1", 1, printer._nesting);
    }
    
    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.beforeArrayValues(generator);
        
        assertEquals("The output should be empty string", "", generator.getOutput());
    }
    
    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.writeArrayValueSeparator(generator);
        
        assertEquals("The output should be equal to ', '", ", ", generator.getOutput());
    }
    
    @Test
    public void testWriteEndArray_withNonZeroNrOfValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 2;
        
        printer.writeEndArray(generator, 3);
        
        assertEquals("The output should be equal to '  ]'", "  ]", generator.getOutput());
        assertEquals("The nesting level should be decreased by 1", 1, printer._nesting);
    }
    
    @Test
    public void testWriteEndArray_withZeroNrOfValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        printer._nesting = 1;
        
        printer.writeEndArray(generator, 0);
        
        assertEquals("The output should be equal to ' ]'", " ]", generator.getOutput());
        assertEquals("The nesting level should not be changed", 1, printer._nesting);
    }
}