package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    @Test
    public void testWriteObject() throws IOException, JsonProcessingException {
        JsonGenerator generator = new MinimalJsonGenerator(); // Use a specific subclass for testing
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(new String("Hello World"));
        generator.writeObject(json);
        Assert.assertEquals("\"Hello World\"", generator.getCurrentValue().asText());

        generator.writeObject(new Integer(42));
        Assert.assertEquals(42, generator.getCurrentValue().asInt());

        generator.writeObject(new BigDecimal("3.14159265359"));
        Assert.assertEquals(new BigDecimal("3.14159265359"), generator.getCurrentValue());

        generator.writeObject(new Boolean(true));
        Assert.assertEquals(true, generator.getCurrentValue().asBoolean());

        generator.writeObject(null);
        Assert.assertEquals(null, generator.getCurrentValue());
    }

    @Test
    public void testWriteString() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeString("Hello World");
        Assert.assertEquals("Hello World", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeRawValue("Hello World");
        Assert.assertEquals("Hello World", generator.getCurrentValue().asText());

        generator.writeRawValue("{\"name\":\"John\",\"age\":30}");
        Assert.assertEquals("{\"name\":\"John\",\"age\":30}", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteRawValue_encoding() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        // Test encoding "Hello World" as UTF-8 bytes
        byte[] utf8Bytes = "Hello World".getBytes("UTF-8");
        generator.writeRawValue(new String(utf8Bytes));
        Assert.assertEquals(new String(utf8Bytes), generator.getCurrentValue().asText());

        // Test encoding "Hello World" as surrogate pairs
        int surr1 = 0xD800;
        int surr2 = 0xDC01;
        int expected = _decodeSurrogate(surr1, surr2) + 0x00DC;
        generator.writeRawValue(new String(new char[]{
                (char) surr1,
                (char) surr2
        }));
        Assert.assertEquals(String.valueOf((char) expected), generator.getCurrentValue().asText());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldName() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeFieldName("\"Hello World\"");
    }

    @Test
    public void testWriteNumber() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeNumber(1);
        Assert.assertEquals(1, generator.getCurrentValue().asInt());

        generator.writeNumber(0xff_ff_ff_ffL);
        Assert.assertEquals(Long.MAX_VALUE, generator.getCurrentValue().asLong());

        generator.writeNumber(Double.POSITIVE_INFINITY);
        Assert.assertEquals(Double.POSITIVE_INFINITY, generator.getCurrentValue().asDouble());

        generator.writeNumber(new BigDecimal("3.14159265359"));
        Assert.assertEquals("3.14159265359", generator.getCurrentValue().asText());

        generator.writeNumber(new BigDecimal("1.23E-308"));
        Assert.assertEquals("1.23E-308", generator.getCurrentValue().asText());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBoolean() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeBoolean(true);
        generator.writeBoolean(false);

        generator.writeBoolean("true");
        generator.writeBoolean("false");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNull() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeNull();
    }

    @Test
    public void testWriteBinary() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "Hello World".getBytes();
        generator.writeBinary(Base64.Standard, outputStream, data.length);
        generator
            .writeRawValue(Base64.getEncoder().encodeToString(data))
            .flush();

        Assert.assertEquals("SmltYWdlJw0K", generator.getCurrentValue().asText());
        Assert.assertEquals("Hello World", new String(Base64.getDecoder().decode(generator.getCurrentValue().asText()), "UTF-8"));
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBinary_unsupported() throws IOException {
        JsonGenerator generator = new MinimalJsonGenerator();

        generator.writeBinary(Base64.Standard, null, 0);
    }
}