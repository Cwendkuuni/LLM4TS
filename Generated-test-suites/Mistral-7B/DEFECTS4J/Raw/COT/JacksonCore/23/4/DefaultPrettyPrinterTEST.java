import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.NopIndenter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinter.getRootSeparator());
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("customSeparator"));
        assertNotNull(prettyPrinter);
        assertEquals("customSeparator", prettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertTrue(output.isEmpty() || output.startsWith(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR.asString()));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartObject(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("{", output);
    }

    @Test
    public void testWriteStartObjectWithIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        Indenter indenter = new FixedSpaceIndenter();
        prettyPrinter.indentObjectsWith(indenter);
        prettyPrinter.writeStartObject(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("{\n", output);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectFieldValueSeparator("fieldName", "value");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals(": ", output);
    }

    @Test
    public void testWriteObjectEntrySeparatorWithSpaces() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeObjectFieldValueSeparator("fieldName", "value");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals(" :\n", output);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectFieldValueSeparator("fieldName", "value");
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("value", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteObjectFieldValueSeparatorWithNullFieldName() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectFieldValueSeparator(null, "value");
        jsonGenerator.close();
        fail("Expected IllegalArgumentException but got none");
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithNullValue() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeObjectFieldValueSeparator("fieldName", null);
        jsonGenerator.close();
        String output = writer.toString();
        assertTrue(output.isEmpty());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("{\n}\n", output);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartArray(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("[", output);
    }

    @Test
    public void testWriteStartArrayWithIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        Indenter indenter = new FixedSpaceIndenter();
        prettyPrinter.indentArraysWith(indenter);
        prettyPrinter.writeStartArray(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("[{\n", output);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals(",\n", output);
    }

    @Test
    public void testWriteArrayValueSeparatorWithSpaces() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals(",\n", output);
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        jsonGenerator.close();
        String output = writer.toString();
        assertEquals("[{\n}]\n", output);
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, prettyPrinterWithSpaces);
        assertTrue(prettyPrinterWithSpaces.isSpacesInObjectEntries());
    }

    @Test
    public void testWithSpacesInObjectEntriesTwice() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
        DefaultPrettyPrinter prettyPrinterWithSpacesAgain = prettyPrinterWithSpaces.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, prettyPrinterWithSpaces);
        assertNotSame(prettyPrinterWithSpaces, prettyPrinterWithSpacesAgain);
        assertTrue(prettyPrinterWithSpacesAgain.isSpacesInObjectEntries());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(objectIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithObjectIndenter);
        assertEquals(objectIndenter, prettyPrinterWithObjectIndenter.getObjectIndenter());
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(arrayIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithArrayIndenter);
        assertEquals(arrayIndenter, prettyPrinterWithArrayIndenter.getArrayIndenter());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String rootSeparator = "customSeparator";
        DefaultPrettyPrinter prettyPrinterWithCustomRootSeparator = prettyPrinter.withRootSeparator(rootSeparator);
        assertNotSame(prettyPrinter, prettyPrinterWithCustomRootSeparator);
        assertEquals(rootSeparator, prettyPrinterWithCustomRootSeparator.getRootSeparator().asString());
    }

    @Test
    public void testWithSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators();
        DefaultPrettyPrinter prettyPrinterWithCustomSeparators = prettyPrinter.withSeparators(separators);
        assertNotSame(prettyPrinter, prettyPrinterWithCustomSeparators);
        assertEquals(separators, prettyPrinterWithCustomSeparators.getSeparators());
    }
}