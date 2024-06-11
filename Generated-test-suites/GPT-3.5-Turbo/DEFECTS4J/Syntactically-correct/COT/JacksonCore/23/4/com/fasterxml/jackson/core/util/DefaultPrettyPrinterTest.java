
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeRootValueSeparator(generator);
        generator.flush();
        assertEquals(" ", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteStartObject() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeStartObject(generator);
        generator.flush();
        assertEquals("{", generator.getOutputTarget().toString());
    }

    @Test
    public void testBeforeObjectEntries() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.beforeObjectEntries(generator);
        generator.flush();
        assertEquals("", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeObjectFieldValueSeparator(generator);
        generator.flush();
        assertEquals(" :", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeObjectEntrySeparator(generator);
        generator.flush();
        assertEquals(",", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteEndObject() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeEndObject(generator, 1);
        generator.flush();
        assertEquals(" }", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteStartArray() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeStartArray(generator);
        generator.flush();
        assertEquals("[", generator.getOutputTarget().toString());
    }

    @Test
    public void testBeforeArrayValues() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.beforeArrayValues(generator);
        generator.flush();
        assertEquals("", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeArrayValueSeparator(generator);
        generator.flush();
        assertEquals(", ", generator.getOutputTarget().toString());
    }

    @Test
    public void testWriteEndArray() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        printer.writeEndArray(generator, 1);
        generator.flush();
        assertEquals(" ]", generator.getOutputTarget().toString());
    }
}
