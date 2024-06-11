import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        assertEquals("", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        assertEquals(" }", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.beforeArrayValues(jsonGenerator);
        assertEquals("", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getRawOutput());
    }
    
    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        MockJsonGenerator jsonGenerator = new MockJsonGenerator();
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        assertEquals(" ]", jsonGenerator.getRawOutput());
    }
}

class MockJsonGenerator implements JsonGenerator {
    
    private String rawOutput;
    
    public MockJsonGenerator() {
        rawOutput = "";
    }
    
    public String getRawOutput() {
        return rawOutput;
    }
    
    @Override
    public void writeRaw(String raw) {
        rawOutput += raw;
    }
    
    @Override
    public void writeRawValue(String raw) {
        throw new UnsupportedOperationException();
    }
    
    // Other methods from JsonGenerator
}