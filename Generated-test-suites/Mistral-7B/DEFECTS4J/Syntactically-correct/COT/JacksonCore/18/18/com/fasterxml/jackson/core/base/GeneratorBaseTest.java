package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.*;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GeneratorBaseTest {
    private ObjectMapper mapper;
    private ObjectWriter writer;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        writer = mapper.writer();
    }

    @After
    public void teardown() throws IOException {
        writer.close();
        mapper.close();
    }

    // Test for isEnabled method
    @Test
    public void testIsEnabled() throws IOException {
        JsonGenerator gen = new GeneratorBase(0, null) {
            @Override
            public void writeString(String s) throws IOException {
                // Do nothing
            }
        };

        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    // Test for getFeatureMask method
    @Test
    public void testGetFeatureMask() throws IOException {
        JsonGenerator gen = new GeneratorBase(0, null) {
            @Override
            public void writeString(String s) throws IOException {
                // Do nothing
            }
        };

        assertEquals(0, gen.getFeatureMask());

        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), gen.getFeatureMask());

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(0, gen.getFeatureMask());
    }

    // Test for writeFieldName method
    @Test
    public void testWriteFieldName() throws IOException {
        JsonGenerator gen = writer;

        gen.writeStartObject();
        gen.writeFieldName("test");
        gen.writeEndObject();

        String json = gen.getOutputStream().toString();
        assertEquals("{\"test\":null}", json);
    }

    // Test for writeString method with valid input
    @Test
    public void testWriteStringValid() throws IOException {
        JsonGenerator gen = writer;

        gen.writeString("test");

        String json = gen.getOutputStream().toString();
        assertEquals("test", json);
    }

    // Test for writeString method with null input
    @Test(expected = IOException.class)
    public void testWriteStringNull() throws IOException {
        JsonGenerator gen = writer;

        gen.writeString(null);
    }

    // Test for writeRawValue method with valid input
    @Test
    public void testWriteRawValueValid() throws IOException {
        JsonGenerator gen = writer;

        gen.writeRawValue("test");

        String json = gen.getOutputStream().toString();
        assertEquals("test", json);
    }

    // Test for writeRawValue method with null input
    @Test(expected = IOException.class)
    public void testWriteRawValueNull() throws IOException {
        JsonGenerator gen = writer;

        gen.writeRawValue(null);
    }

    // Test for writeRawValue method with valid character array input
    @Test
    public void testWriteRawValueCharArray() throws IOException {
        JsonGenerator gen = writer;

        char[] text = new char[]{'t', 'e', 's', 't'};
        gen.writeRawValue(text, 0, text.length);

        String json = gen.getOutputStream().toString();
        assertEquals(new String(text), json);
    }

    // Test for writeRawValue method with null character array input
    @Test(expected = IOException.class)
    public void testWriteRawValueNullCharArray() throws IOException {
        JsonGenerator gen = writer;

        char[] text = null;
        gen.writeRawValue(text, 0, 0);
    }

    // Test for writeRawValue method with big input
    @Test
    public void testWriteRawValueBigInput() throws IOException {
        JsonGenerator gen = writer;

        String text = "test";
        for (int i = 0; i < 1000; i++) {
            text += gen.getOutputContext().getTextFactory().writeString("test");
            gen.writeRawValue(text);
        }

        String json = gen.getOutputStream().toString();
        assertEquals(new String(new char[1001 * (1 + text.length())]), json);
    }

    // Test for writeRawValue method with large character array input
    @Test
    public void testWriteRawValueLargeCharArray() throws IOException {
        JsonGenerator gen = writer;

        char[] text = new char[10000];
        gen.writeRawValue(text, 0, text.length);

        String json = gen.getOutputStream().toString();
        assertEquals(new String(text), json);
    }
}