import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeRootValueSeparator(generator);

        assertEquals(" ", generator.getRawOutput());
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeStartObject(generator);

        assertEquals("{", generator.getRawOutput());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.beforeObjectEntries(generator);

        assertEquals(0, printer._nesting);
        assertEquals(0, printer._nesting);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeObjectFieldValueSeparator(generator);

        assertEquals(" : ", generator.getRawOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeObjectEntrySeparator(generator);

        assertEquals(", ", generator.getRawOutput());
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeEndObject(generator, 1);

        assertEquals(" }", generator.getRawOutput());
        assertEquals(0, printer._nesting);
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeStartArray(generator);

        assertEquals("[", generator.getRawOutput());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.beforeArrayValues(generator);

        assertEquals(0, printer._nesting);
        assertEquals(0, printer._nesting);
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeArrayValueSeparator(generator);

        assertEquals(", ", generator.getRawOutput());
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();

        printer.writeEndArray(generator, 1);

        assertEquals(" ]", generator.getRawOutput());
        assertEquals(0, printer._nesting);
    }
}

class JsonGeneratorStub implements JsonGenerator {
    private String rawOutput = "";

    public String getRawOutput() {
        return rawOutput;
    }

    @Override
    public void writeRaw(String raw) throws IOException {
        rawOutput += raw;
    }

    // Implement other methods from JsonGenerator interface
}