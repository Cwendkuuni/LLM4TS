package com.fasterxml.jackson.core.base; // replace with your package name if necessary

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    private GeneratorBase generator;

    @Before
    public void setUp() {
        int features = JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getCodec();
        generator = new GeneratorBase(features, codec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };
    }

    @Test
    public void testVersion() throws IOException {
        Version version = generator.version();
        assertEquals("2.12.2", version.toString());
    }

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() throws IOException {
        assertFalse(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        generator.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() throws IOException {
        assertFalse(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
        generator.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test(expected = IllegalStateException.class)
    public void testClose_AlreadyClosed() throws IOException {
        generator.close();
        generator.close();
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("hello");
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        generator.writeRawValue("world");
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        generator.writeRawValue(new com.fasterxml.jackson.core.json.SerializableString("jackson"));
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_InvalidType() throws IOException {
        generator.writeRawValue(3);
    }

    @Test
    public void testWriteFieldName() throws IOException {
        generator.writeFieldName("name");
    }

    @Test
    public void testWriteObject_Null() throws IOException {
        generator.writeNull();
    }

    @Test
    public void testWriteObject_String() throws IOException {
        generator.writeObject("Jackson");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteObject_NonSerializableObject() throws IOException {
        generator.writeObject(new Object());
    }

    @Test
    public void testWriteObject_BigDecimal() throws IOException {
        BigDecimal bd = new BigDecimal("1234567.89");
        generator.writeObject(bd);
    }

    @Test
    public void testFlush() throws IOException {
        generator.flush();
    }

    @Test
    public void testClose() throws IOException {
        generator.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        generator.close();
        assertTrue(generator.isClosed());
    }
}