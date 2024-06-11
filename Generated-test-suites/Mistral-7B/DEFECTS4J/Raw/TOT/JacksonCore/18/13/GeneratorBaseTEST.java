package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultPrettyPrinter());
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        generator.writeBoolean(true);
        generator.flush();
        String json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("true", json);

        generator.writeBoolean(false);
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("false", json);
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        generator.writeNull();
        generator.flush();
        String json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("null", json);
    }

    @Test
    public void testWriteNumber() throws JsonProcessingException {
        generator.writeNumber(123);
        generator.flush();
        String json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("123", json);

        generator.writeNumber(123L);
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("123", json);

        generator.writeNumber(123.0);
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("123.0", json);

        generator.writeNumber(123.0f);
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("123.0", json);

        generator.writeNumber(new BigDecimal("123.45"));
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("123.45", json);
    }

    @Test
    public void testWriteString() throws JsonProcessingException {
        generator.writeString("Hello World!");
        generator.flush();
        String json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("\"Hello World!\"", json);
    }

    @Test
    public void testWriteRawValue() throws JsonProcessingException {
        generator.writeRawValue("Hello World!");
        generator.flush();
        String json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("\"Hello World!\"", json);

        generator.writeRawValue("\\/");
        generator.flush();
        json = new String(generator.getOutputBuffer().toByteArray());
        Assert.assertEquals("\\/", json);
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(new java.util.HashMap<>());
        generator.writeString(json);
        generator.flush();

        json = objectMapper.writeValueAsString(new java.util.HashMap<>(){{
            put("key", "value");
        }});
        generator.writeString(json);
        generator.flush();

        json = objectMapper.writeValueAsString(new int[10]);
        generator.writeString(json);
        generator.flush();
    }

    @Test
    public void testWriteTree() throws JsonProcessingException {
        TreeNode tree = new TreeNode(){};
        generator.writeTree(tree);
        generator.flush();
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidValue() throws JsonProcessingException {
        generator.writeNumber(Long.MAX_VALUE + 1);
        generator.flush();
    }
}