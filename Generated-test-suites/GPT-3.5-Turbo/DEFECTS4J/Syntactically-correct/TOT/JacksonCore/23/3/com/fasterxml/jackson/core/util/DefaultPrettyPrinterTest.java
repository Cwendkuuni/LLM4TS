import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator_withRootSeparator_outputSeparator() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeRootValueSeparator(generator);
        assertEquals(" ", writer.toString());
    }

    @Test
    public void testWriteRootValueSeparator_withoutRootSeparator_outputNothing() throws IOException{
        prettyPrinter = prettyPrinter.withRootSeparator(null);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeRootValueSeparator(generator);
        assertEquals("", writer.toString());
    }

    @Test
    public void testWriteStartObject_outputOpeningBracket() throws IOException{
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartObject(generator);
        assertEquals("{", writer.toString());
    }

    @Test
    public void testBeforeObjectEntries_withNestedLevelThree_outputIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withObjectIndenter(new FixedSpaceIndenter());
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.beforeObjectEntries(generator);
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator_withSpacesInObjectEntries_outputColonWithSpaces() throws IOException{
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertEquals(" :", writer.toString());
    }

    @Test
    public void testWriteObjectEntrySeparator_withObjectIndenterNotInline_outputSeparatorWithIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withObjectIndenter(new FixedSpaceIndenter());
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectEntrySeparator(generator);
        assertEquals("," + System.lineSeparator() + "   ", writer.toString());
    }

    @Test
    public void testWriteObjectEntrySeparator_withObjectIndenterInline_outputSeparatorWithoutIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withObjectIndenter(DefaultPrettyPrinter.NopIndenter.instance);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectEntrySeparator(generator);
        assertEquals(",", writer.toString());
    }

    @Test
    public void testWriteEndObject_withZeroNrOfEntries_outputSpaceClosingBracket() throws IOException{
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeEndObject(generator, 0);
        assertEquals(" }", writer.toString());
    }

    @Test
    public void testWriteStartArray_outputOpeningBracket() throws IOException{
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartArray(generator);
        assertEquals("[", writer.toString());
    }

    @Test
    public void testBeforeArrayValues_withNestedLevelTwo_outputIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withArrayIndenter(new FixedSpaceIndenter());
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.beforeArrayValues(generator);
        assertEquals("  ", writer.toString());
    }

    @Test
    public void testWriteArrayValueSeparator_withArrayIndenterNotInline_outputSeparatorWithIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withArrayIndenter(new FixedSpaceIndenter());
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeArrayValueSeparator(generator);
        assertEquals("," + System.lineSeparator() + "  ", writer.toString());
    }

    @Test
    public void testWriteArrayValueSeparator_withArrayIndenterInline_outputSeparatorWithoutIndentation() throws IOException{
        prettyPrinter = prettyPrinter.withArrayIndenter(DefaultPrettyPrinter.NopIndenter.instance);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeArrayValueSeparator(generator);
        assertEquals(",", writer.toString());
    }

    @Test
    public void testWriteEndArray_withZeroNrOfValues_outputSpaceClosingBracket() throws IOException{
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeEndArray(generator, 0);
        assertEquals(" ]", writer.toString());
    }
}