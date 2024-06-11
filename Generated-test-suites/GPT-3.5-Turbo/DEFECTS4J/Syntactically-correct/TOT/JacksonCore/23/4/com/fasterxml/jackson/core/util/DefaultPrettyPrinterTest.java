import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter printer;

    @Before
    public void setUp() {
        printer = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() {
        // Test when rootSeparator is null
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeRootValueSeparator(generatorMock);
        assertEquals("", generatorMock.getOutput());

        // Test when rootSeparator is not null
        printer = new DefaultPrettyPrinter("\n");
        generatorMock = new JsonGeneratorMock();
        printer.writeRootValueSeparator(generatorMock);
        assertEquals("\n", generatorMock.getOutput());
    }

    @Test
    public void testWriteStartObject() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeStartObject(generatorMock);
        assertEquals("{", generatorMock.getOutput());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeObjectEntries() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.beforeObjectEntries(generatorMock);
        assertEquals("", generatorMock.getOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeObjectFieldValueSeparator(generatorMock);
        assertEquals(": ", generatorMock.getOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeObjectEntrySeparator(generatorMock);
        assertEquals(", ", generatorMock.getOutput());
    }

    @Test
    public void testWriteEndObject() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeEndObject(generatorMock, 0);
        assertEquals(" }", generatorMock.getOutput());
        assertEquals(0, printer._nesting);

        printer._nesting = 1;
        generatorMock = new JsonGeneratorMock();
        printer.writeEndObject(generatorMock, 1);
        assertEquals(" ", generatorMock.getOutput());
        assertEquals(0, printer._nesting);
    }

    @Test
    public void testWriteStartArray() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeStartArray(generatorMock);
        assertEquals("[", generatorMock.getOutput());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeArrayValues() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.beforeArrayValues(generatorMock);
        assertEquals("", generatorMock.getOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeArrayValueSeparator(generatorMock);
        assertEquals(", ", generatorMock.getOutput());
    }

    @Test
    public void testWriteEndArray() {
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        printer.writeEndArray(generatorMock, 0);
        assertEquals(" ]", generatorMock.getOutput());
        assertEquals(0, printer._nesting);

        printer._nesting = 1;
        generatorMock = new JsonGeneratorMock();
        printer.writeEndArray(generatorMock, 1);
        assertEquals(" ", generatorMock.getOutput());
        assertEquals(0, printer._nesting);
    }

    private class JsonGeneratorMock extends JsonGenerator {

        private StringBuilder output;

        public JsonGeneratorMock() {
            output = new StringBuilder();
        }

        public String getOutput() {
            return output.toString();
        }

        @Override
        public void writeRaw(String text) throws IOException {
            output.append(text);
        }

        // Other overridden methods
    }
}