
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class DefaultPrettyPrinterTest {
    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setup() {
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeRootValueSeparator(g);
        String expected = " ";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeStartObject(g);
        String expected = "{";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.beforeObjectEntries(g);
        String expected = "";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(g);
        String expected = " :";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeObjectEntrySeparator(g);
        String expected = ",";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = new JsonGenerator();
        int nrOfEntries = 1;
        defaultPrettyPrinter.writeEndObject(g, nrOfEntries);
        String expected = "}";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeStartArray(g);
        String expected = "[";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.beforeArrayValues(g);
        String expected = " ";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(g);
        String expected = ",";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = new JsonGenerator();
        int nrOfValues = 2;
        defaultPrettyPrinter.writeEndArray(g, nrOfValues);
        String expected = "]";
        String actual = g.getRaw();
        Assert.assertEquals(expected, actual);
    }
}
