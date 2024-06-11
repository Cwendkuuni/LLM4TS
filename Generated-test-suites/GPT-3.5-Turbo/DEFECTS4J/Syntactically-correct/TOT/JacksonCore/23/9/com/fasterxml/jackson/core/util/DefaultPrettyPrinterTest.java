
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testCreateInstance() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter newInstance = pp.createInstance();
        assertNotNull(newInstance);
    }

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeRootValueSeparator(g);
        assertEquals(g.getRawOutput(), "");
        pp.withRootSeparator("test");
        pp.writeRootValueSeparator(g);
        assertEquals(g.getRawOutput(), "test");
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeStartObject(g);
        assertEquals(g.getRawOutput(), "{");
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.beforeObjectEntries(g);
        assertEquals(g.getRawOutput(), "");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeObjectFieldValueSeparator(g);
        assertEquals(g.getRawOutput(), ": ");
        pp.withoutSpacesInObjectEntries();
        pp.writeObjectFieldValueSeparator(g);
        assertEquals(g.getRawOutput(), ":");
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeObjectEntrySeparator(g);
        assertEquals(g.getRawOutput(), ", ");
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeEndObject(g, 0);
        assertEquals(g.getRawOutput(), " }");
        g.reset();
        pp.writeEndObject(g, 1);
        assertEquals(g.getRawOutput(), " } ");
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeStartArray(g);
        assertEquals(g.getRawOutput(), "[");
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.beforeArrayValues(g);
        assertEquals(g.getRawOutput(), "");
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeArrayValueSeparator(g);
        assertEquals(g.getRawOutput(), ", ");
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGeneratorStub g = new JsonGeneratorStub();
        pp.writeEndArray(g, 0);
        assertEquals(g.getRawOutput(), " ]");
        g.reset();
        pp.writeEndArray(g, 1);
        assertEquals(g.getRawOutput(), " ] ");
    }

    private static class JsonGeneratorStub implements JsonGenerator {

        private String rawOutput = "";

        public String getRawOutput() {
            return rawOutput;
        }

        public void reset() {
            rawOutput = "";
        }

        @Override
        public void writeRaw(String value) {
            rawOutput += value;
        }

        // other overridden methods

    }

}

