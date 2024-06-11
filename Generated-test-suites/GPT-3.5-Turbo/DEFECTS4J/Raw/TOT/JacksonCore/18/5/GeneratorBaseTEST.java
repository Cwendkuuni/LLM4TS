import com.fasterxml.jackson.core.json.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GeneratorBaseTest {

    private StringWriter writer;
    private JsonGenerator generator;

    @Before
    public void setUp() {
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
    }

    @Test
    public void testWriteFieldName() throws IOException {
        generator.writeFieldName("key");
        generator.writeStartObject();
        generator.writeStringField("name", "John");
        generator.writeEndObject();
        generator.close();

        assertEquals("{\"key\":{\"name\":\"John\"}}", writer.toString());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("Hello World");
        generator.close();

        assertEquals("\"Hello World\"", writer.toString());
    }

    @Test
    public void testWriteRaw() throws IOException {
        generator.writeRaw("Some raw content");
        generator.close();

        assertEquals("Some raw content", writer.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generator.writeStartObject();
        generator.writeRawValue("{\"name\":\"John\"}");
        generator.writeEndObject();
        generator.close();

        assertEquals("{\"name\":\"John\"}", writer.toString());
    }

    @Test
    public void testWriteBinary() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);
        generator.writeNumber(3.14);
        generator.writeNumber(new BigDecimal("1000.123"));
        generator.close();

        assertEquals("1233.141000.123", writer.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        generator.writeBoolean(false);
        generator.close();

        assertEquals("truefalse", writer.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        generator.close();

        assertEquals("null", writer.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteBinaryWithInputStream() throws IOException {
        generator.writeBinary(null);
    }

    @Test
    public void testFlush() throws IOException {
        generator.writeString("Hello World");
        generator.flush();

        assertEquals("\"Hello World\"", writer.toString());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(generator.isClosed());

        generator.close();

        assertTrue(generator.isClosed());
    }
}