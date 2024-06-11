import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.DefaultPrettyPrinter;

import org.junit.Test;

import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeRootValueSeparator(g);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartObject(g);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeObjectEntries(g);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectFieldValueSeparator(g);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectEntrySeparator(g);
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndObject(g, 0);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartArray(g);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeArrayValues(g);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeArrayValueSeparator(g);
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = null;
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndArray(g, 0);
    }
}