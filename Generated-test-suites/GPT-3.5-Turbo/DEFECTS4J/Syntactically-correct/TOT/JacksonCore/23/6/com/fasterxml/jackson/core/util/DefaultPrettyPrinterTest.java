import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();

        printer.writeRootValueSeparator(generator);

        assertEquals("", generator.getRawString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();

        printer.writeStartObject(generator);

        assertEquals("{", generator.getRawString());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();
        printer._nesting = 1;

        printer.beforeObjectEntries(generator);

        assertEquals(1, generator.getWriteIndentationCount());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();

        printer.writeObjectFieldValueSeparator(generator);

        assertEquals(" : ", generator.getRawString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();
        printer._nesting = 1;

        printer.writeObjectEntrySeparator(generator);

        assertEquals(", ", generator.getRawString());
        assertEquals(1, generator.getWriteIndentationCount());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();
        printer._nesting = 1;

        printer.writeEndObject(generator, 2);

        assertEquals(" }", generator.getRawString());
        assertEquals(1, printer._nesting);
        assertEquals(1, generator.getWriteIndentationCount());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();

        printer.writeStartArray(generator);

        assertEquals("[", generator.getRawString());
        assertEquals(1, printer._nesting);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();
        printer._nesting = 1;

        printer.beforeArrayValues(generator);

        assertEquals(1, generator.getWriteIndentationCount());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();

        printer.writeArrayValueSeparator(generator);

        assertEquals(", ", generator.getRawString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DummyJsonGenerator generator = new DummyJsonGenerator();
        printer._nesting = 1;

        printer.writeEndArray(generator, 3);

        assertEquals(" ]", generator.getRawString());
        assertEquals(1, printer._nesting);
        assertEquals(1, generator.getWriteIndentationCount());
    }

}