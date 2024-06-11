import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeRootValueSeparator(g);
        // check the functionality of writeRootValueSeparator method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartObject(g);
        // check the functionality of writeStartObject method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeObjectEntries(g);
        // check the functionality of beforeObjectEntries method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectFieldValueSeparator(g);
        // check the functionality of writeObjectFieldValueSeparator method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectEntrySeparator(g);
        // check the functionality of writeObjectEntrySeparator method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        int nrOfEntries = 0; // specify the number of entries
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndObject(g, nrOfEntries);
        // check the functionality of writeEndObject method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartArray(g);
        // check the functionality of writeStartArray method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeArrayValues(g);
        // check the functionality of beforeArrayValues method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeArrayValueSeparator(g);
        // check the functionality of writeArrayValueSeparator method
        // assert statements
        assertNotNull(g);
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = null; // create JsonGenerator object
        int nrOfValues = 0; // specify the number of values
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndArray(g, nrOfValues);
        // check the functionality of writeEndArray method
        // assert statements
        assertNotNull(g);
    }
}