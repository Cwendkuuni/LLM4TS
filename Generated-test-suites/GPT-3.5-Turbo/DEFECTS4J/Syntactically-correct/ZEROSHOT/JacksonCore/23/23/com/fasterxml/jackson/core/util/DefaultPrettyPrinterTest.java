import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeRootValueSeparator(g);
        Mockito.verify(g).writeRaw(Mockito.any(SerializableString.class));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeStartObject(g);
        Mockito.verify(g).writeRaw(Mockito.anyChar());
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.beforeObjectEntries(g);
        Mockito.verify(g).writeRaw(Mockito.any(Indenter.class), Mockito.anyInt());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeObjectFieldValueSeparator(g);
        Mockito.verify(g).writeRaw(Mockito.any(SerializableString.class));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeObjectEntrySeparator(g);
        Mockito.verify(g).writeRaw(Mockito.any(SerializableString.class));
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeEndObject(g, 0);
        Mockito.verify(g).writeRaw(Mockito.anyChar());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeStartArray(g);
        Mockito.verify(g).writeRaw(Mockito.anyChar());
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.beforeArrayValues(g);
        Mockito.verify(g).writeRaw(Mockito.any(Indenter.class), Mockito.anyInt());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeArrayValueSeparator(g);
        Mockito.verify(g).writeRaw(Mockito.any(SerializableString.class));
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeEndArray(g, 0);
        Mockito.verify(g).writeRaw(Mockito.anyChar());
    }
}