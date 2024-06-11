package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabledFeatures() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
         // Test with default features
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
         // Test with features enabled
         int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
         GeneratorBase generatorWithFeatures = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec(), null, features);
         assertTrue(generatorWithFeatures.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
         assertTrue(generatorWithFeatures.isEnabled(Feature.ESCAPE_NON_ASCII));
         assertTrue(generatorWithFeatures.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
         // Test with features disabled
         int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask() ^ Feature.STRICT_DUPLICATE_DETECTION.getMask();
         GeneratorBase generatorWithFeaturesDisabled = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec(), null, featuresDisabled);
         assertFalse(generatorWithFeaturesDisabled.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
         assertFalse(generatorWithFeaturesDisabled.isEnabled(Feature.ESCAPE_NON_ASCII));
         assertFalse(generatorWithFeaturesDisabled.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetGetFeatures() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
         // Test setting and getting features
         assertEquals(Feature.DEFAULT_FEATURE_MASK, generator.getFeatureMask());
         generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
         assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
         generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
         assertEquals(Feature.DEFAULT_FEATURE_MASK, generator.getFeatureMask());
         generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
         assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
         generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
         assertEquals(Feature.DEFAULT_FEATURE_MASK, generator.getFeatureMask());
    }

    @Test
    public void testWriteString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        String[] testCases = new String[]{"", "ABC", "DEF", "123", "456", "789", "012", "345", "678", "901", "ABCDEF", "1234567890", "012345678901234567890123456789012345678901234567890"};
        for (String testCase : testCases) {
            generator.writeString(testCase);
            assertEquals(testCase, generator.getCurrentValue());
            generator.flush();
            JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
            assertEquals(testCase, parser.getValueAsString());
            generator.close();
        }
    }

    @Test
    public void testWriteRawValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        String[] testCases = new String[]{"", "ABC", "DEF", "123", "456", "789", "012", "345", "678", "901", "ABCDEF", "1234567890", "0123456789012345678901234567890123456789012345678901234567890"};
        for (String testCase : testCases) {
            generator.writeRawValue(testCase);
            assertEquals(testCase, generator.getCurrentValue());
            generator.flush();
            JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
            assertEquals(testCase, parser.getValueAsString());
            generator.close();
        }
    }

    @Test
    public void testWriteNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        generator.writeNull();
        assertNull(generator.getCurrentValue());
        generator.flush();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertNull(parser.getValueAsString());
        generator.close();
    }

    @Test
    public void testWriteBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(Boolean.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        generator.writeBoolean(true);
        assertEquals("true", generator.getCurrentValue());
        generator.flush();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals("true", parser.getValueAsString());
        generator.writeBoolean(false);
        assertEquals("false", generator.getCurrentValue());
        generator.flush();
        parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals("false", parser.getValueAsString());
        generator.close();
    }

    @Test
    public void testWriteNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(Long.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        generator.writeNumber(123);
        assertEquals("123", generator.getCurrentValue());
        generator.flush();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals("123", parser.getValueAsString());
        generator.close();
        
        generator = mapper.getPolymorphicSerializer(Double.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        generator.writeNumber(123.45);
        assertEquals("123.45", generator.getCurrentValue());
        generator.flush();
        parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals("123.45", parser.getValueAsString());
        generator.close();

        generator = mapper.getPolymorphicSerializer(BigDecimal.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        generator.writeNumber(new BigDecimal("123.45"));
        assertEquals("123.45", generator.getCurrentValue());
        generator.flush();
        parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals("123.45", parser.getValueAsString());
        generator.close();
    }

    @Test
    public void testWriteObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        String testValue = "test";
        generator.writeObject(testValue);
        assertEquals(testValue, generator.getCurrentValue());
        generator.flush();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals(testValue, parser.getValueAsString());
        generator.close();
    }

    @Test
    public void testWriteTree() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getPolymorphicSerializer(TreeNode.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonWriter.StandardEncodings.UTF8, mapper.getObjectCodec());
        TreeNode testValue = mapper.getValueFactory().findValueInternal("test");
        generator.writeTree(testValue);
        assertEquals(testValue, generator.getCurrentValue());
        generator.flush();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.getOutputBuffer().toString()));
        assertEquals(mapper.valueToTree(testValue), parser.readValueAsTree());
        generator.close();
    }
}