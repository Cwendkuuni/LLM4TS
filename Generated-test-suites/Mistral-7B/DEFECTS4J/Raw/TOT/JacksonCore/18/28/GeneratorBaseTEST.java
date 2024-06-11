package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    @Test
    public void testWriteString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        generator.setCurrentValue("Hello World!");
        String result = generator.writeString(new String("Hello World!"));
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testWriteRawValueString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        generator.writeRawValue("Hello World!");
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testWriteNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        generator.writeNull();
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals("null", result);
    }

    @Test
    public void testWriteNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        generator.writeNumber(123);
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals("123", result);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        generator.writeBoolean(true);
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals("true", result);
    }

    @Test
    public void testWriteBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        BigDecimal number = new BigDecimal("3.14159265358979323846264338327950288416");
        generator.writeNumber(number);
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals("\"3.14159265358979323846264338327950288416\"", result);
    }

    @Test
    public void testWriteObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        String input = "{\"name\":\"John\",\"age\":30}";
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString("John");
        generator.writeFieldName("age");
        generator.writeNumber(30);
        generator.writeEndObject();
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals(input, result);
    }

    @Test
    public void testWriteRawValueStringWithOffsetAndLength() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().getGeneratorFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        String input = "Hello World!";
        generator.writeRawValue(input, 0, input.length());
        String result = generator.getOutputContext().getOutputAsString();
        Assert.assertEquals(input, result);
    }

    @Test
    public void testWriteBinary() throws IOException {
        // This test case is not applicable as the method is not implemented in the test subject
        Assert.fail("Method writeBinary() is not implemented");
    }

    @Test
    public void testEnableWriteNumbersAsStrings() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        Assert.assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisableWriteNumbersAsStrings() {
        GeneratorBase generator = new GeneratorBase(0, null);
        Assert.assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBers_AS_STRINGS));
    }

    @Test
    public void testEnableAndDisableWriteNumbersAsStrings() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        Assert.assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        Assert.assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnableOrDisableWriteNumbersAsStringsWithPreviousState() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        boolean previousState = generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        boolean newState = generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        Assert.assertFalse(newState);
        Assert.assertEquals(previousState, generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }
}