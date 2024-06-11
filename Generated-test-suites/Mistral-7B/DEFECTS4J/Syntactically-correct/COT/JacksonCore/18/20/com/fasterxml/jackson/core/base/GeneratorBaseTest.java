package com.fasterxml.jackson.core.base; // add the package name if necessary

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    private GeneratorBase jsonGenerator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        JsonWriteContext context = JsonWriteContext.builder().build();
        jsonGenerator = new GeneratorBase(0, new BasicObjectMapper.StandardSerializer(), context);
    }

    @Test
    public void testVersion() {
        Version version = jsonGenerator.version();
        // You can add expected version number here for validation
        assertTrue(version.getClass().getSimpleName().startsWith("Version"));
    }

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        jsonGenerator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        jsonGenerator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() throws IOException {
        jsonGenerator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        jsonGenerator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testIsEnabled_WRITE_NULL_AS_NULL_OBJECT() throws IOException {
        jsonGenerator.enable(Feature.WRITE_NULL_AS_NULL_OBJECT);
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NULL_AS_NULL_OBJECT));
        jsonGenerator.disable(Feature.WRITE_NULL_AS_NULL_OBJECT);
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NULL_AS_NULL_OBJECT));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() throws IOException {
        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        jsonGenerator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteString_Null() throws IOException {
        jsonGenerator.writeString(null);
        fail("Expected JsonProcessingException but was not thrown.");
    }

    @Test
    public void testWriteString_EmptyString() throws IOException {
        jsonGenerator.writeString("");
        assertEquals("\"\"", outputStream.toString());
    }

    @Test
    public void testWriteString_SingleCharacter() throws IOException {
        jsonGenerator.writeString("a");
        assertEquals("\"a\"", outputStream.toString());
    }

    @Test
    public void testWriteString_MultipleCharacters() throws IOException {
        jsonGenerator.writeString("abc");
        assertEquals("\"abc\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_EmptyString() throws IOException {
        jsonGenerator.writeRawValue("");
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_SingleCharacter() throws IOException {
        jsonGenerator.writeRawValue("a");
        assertEquals("\"a\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_MultipleCharacters() throws IOException {
        jsonGenerator.writeRawValue("abc");
        assertEquals("\"abc\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_RawString() throws IOException {
        jsonGenerator.writeRawValue("raw string");
        assertEquals("\"raw string\"", outputStream.toString());
    }

    @Test
    public void testWriteNumber_Int() throws IOException {
        jsonGenerator.writeNumber(123);
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testWriteNumber_Long() throws IOException {
        jsonGenerator.writeNumber(1234567890L);
        assertEquals("1234567890", outputStream.toString());
    }

    @Test
    public void testWriteNumber_Float() throws IOException {
        jsonGenerator.writeNumber(123.45f);
        assertEquals("123.45", outputStream.toString());
    }

    @Test
    public void testWriteNumber_Double() throws IOException {
        jsonGenerator.writeNumber(123.456789);
        assertEquals("123.456789", outputStream.toString());
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        BigDecimal decimal = new BigDecimal("123.456789");
        jsonGenerator.writeNumber(decimal);
        assertEquals(decimal.toString(), outputStream.toString());
    }

    @Test
    public void testWriteBoolean_True() throws IOException {
        jsonGenerator.writeBoolean(true);
        assertEquals("true", outputStream.toString());
    }

    @Test
    public void testWriteBoolean_False() throws IOException {
        jsonGenerator.writeBoolean(false);
        assertEquals("false", outputStream.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        jsonGenerator.writeNull();
        assertEquals("null", outputStream.toString());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteObject_Null() throws IOException {
        jsonGenerator.writeObject(null);
        fail("Expected JsonGenerationException but was not thrown.");
    }

    @Test
    public void testWriteObject_Number() throws IOException {
        jsonGenerator.writeObject(new Integer(123));
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testWriteObject_String() throws IOException {
        jsonGenerator.writeObject("abc");
        assertEquals("\"abc\"", outputStream.toString());
    }

    @Test
    public void testWriteObject_Array() throws IOException {
        Object[] array = new Object[]{1, "a", new Integer(2)};
        jsonGenerator.writeObject(array);
        assertEquals("[1,\"a\",2]", outputStream.toString());
    }

    @Test
    public void testWriteObject_CustomObject() throws IOException {
        // Provide a custom object to test its serialization
        class TestObject {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObject = new TestObject("Test Object");
        jsonGenerator.writeObject(testObject);
        assertEquals("{\"name\":\"Test Object\"}", outputStream.toString());
    }

    @Test
    public void testFlush() throws IOException {
        jsonGenerator.writeString("Test");
        jsonGenerator.flush();
        assertEquals("\"Test\"", outputStream.toString());
    }

    @Test
    public void testClose() throws IOException {
        jsonGenerator.writeString("Test");
        jsonGenerator.close();
        assertEquals("\"Test\"", outputStream.toString());
        assertFalse(jsonGenerator.isOpen());
    }
}