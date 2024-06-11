import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeRootValueSeparator(generatorMock);

        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR.toString(), generatorMock.getOutput());
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeStartObject(generatorMock);

        assertEquals("{", generatorMock.getOutput());
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.beforeObjectEntries(generatorMock);

        assertEquals("", generatorMock.getOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeObjectFieldValueSeparator(generatorMock);

        String expectedOutput = " " + DefaultPrettyPrinter.DEFAULT_SEPARATORS.getObjectFieldValueSeparator() + " ";
        assertEquals(expectedOutput, generatorMock.getOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeObjectEntrySeparator(generatorMock);

        String expectedOutput = DefaultPrettyPrinter.DEFAULT_SEPARATORS.getObjectEntrySeparator();
        assertEquals(expectedOutput, generatorMock.getOutput());
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeEndObject(generatorMock, 1);

        assertEquals(" }", generatorMock.getOutput());
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeStartArray(generatorMock);

        assertEquals("[", generatorMock.getOutput());
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.beforeArrayValues(generatorMock);

        assertEquals("", generatorMock.getOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeArrayValueSeparator(generatorMock);

        String expectedOutput = DefaultPrettyPrinter.DEFAULT_SEPARATORS.getArrayValueSeparator();
        assertEquals(expectedOutput, generatorMock.getOutput());
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();

        prettyPrinter.writeEndArray(generatorMock, 1);

        assertEquals(" ]", generatorMock.getOutput());
    }

    class JsonGeneratorMock extends JsonGenerator {
        private StringBuilder output = new StringBuilder();

        public String getOutput() {
            return output.toString();
        }

        @Override
        public void writeRaw(String text) {
            output.append(text);
        }

        // Implement other methods of JsonGenerator if necessary
    }
}