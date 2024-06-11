import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class DefaultPrettyPrinterTest {

    // Test case for writeRootValueSeparator method

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(g);
        Mockito.verify(g).writeRaw(" ");
    }

    // Test case for writeStartObject method

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(g);
        Mockito.verify(g).writeRaw("{");
        Mockito.verify(prettyPrinter._objectIndenter).writeIndentation(g, 0);
    }

    // Test case for beforeObjectEntries method

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.beforeObjectEntries(g);
        Mockito.verify(prettyPrinter._objectIndenter).writeIndentation(g, 0);
    }

    // Test case for writeObjectFieldValueSeparator method

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeObjectFieldValueSeparator(g);
        Mockito.verify(g).writeRaw(" : ");
    }

    // Test case for writeObjectEntrySeparator method

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeObjectEntrySeparator(g);
        Mockito.verify(g).writeRaw(",");
        Mockito.verify(prettyPrinter._objectIndenter).writeIndentation(g, 0);
    }

    // Test case for writeEndObject method

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeEndObject(g, 0);
        Mockito.verify(prettyPrinter._objectIndenter).writeIndentation(g, 0);
        Mockito.verify(g).writeRaw(" ");
        Mockito.verify(g).writeRaw("}");
    }

    // Test case for writeStartArray method

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartArray(g);
        Mockito.verify(g).writeRaw("[");
        Mockito.verify(prettyPrinter._arrayIndenter).writeIndentation(g, 0);
    }

    // Test case for beforeArrayValues method

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.beforeArrayValues(g);
        Mockito.verify(prettyPrinter._arrayIndenter).writeIndentation(g, 0);
    }

    // Test case for writeArrayValueSeparator method

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeArrayValueSeparator(g);
        Mockito.verify(g).writeRaw(",");
        Mockito.verify(prettyPrinter._arrayIndenter).writeIndentation(g, 0);
    }

    // Test case for writeEndArray method

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeEndArray(g, 0);
        Mockito.verify(prettyPrinter._arrayIndenter).writeIndentation(g, 0);
        Mockito.verify(g).writeRaw(" ");
        Mockito.verify(g).writeRaw("]");
    }

}