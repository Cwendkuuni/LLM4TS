package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Unit tests for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    @Test
    public void testGeneratorBase() throws IOException {
        // Create test instance, with default settings
        GeneratorBase generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        assertEquals(GeneratorBase.class, generator.getClass());
        assertEquals(GeneratorBase.DERIVED_FEATURES_MASK, generator.getFeatureMask());
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(0, generator.getHighestNonEscapedChar());
        assertNull(generator.getPrettyPrinter());
        assertFalse(generator.isClosed());

        // Test feature enable/disable
        JsonGenerator gen = generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(127, gen.getHighestNonEscapedChar());
        gen = gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(0, gen.getHighestNonEscapedChar());

        // Test feature override
        JsonGenerator gen2 = generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(),
                Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gen2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen2.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(127, gen2.getHighestNonEscapedChar());
        gen2 = gen2.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(gen2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(0, gen2.getHighestNonEscapedChar());

        // Test use of default pretty printer
        JsonGenerator gen3 = generator.useDefaultPrettyPrinter();
        assertTrue(gen3.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(127, gen3.getHighestNonEscapedChar());
        assertEquals(DefaultPrettyPrinter.class, gen3.getPrettyPrinter().getClass());

        // Test setting custom pretty printer
        PrettyPrinter ppr = new DefaultPrettyPrinter();
        ObjectCodec oc = new SimpleObjectMapper().getFactory().getCodec();
        JsonGenerator gen4 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, oc, new JsonWriteContext(ppr));
        assertEquals(GeneratorBase.class, gen4.getClass());
        assertEquals(GeneratorBase.DERIVED_FEATURES_MASK, gen4.getFeatureMask());
        assertTrue(gen4.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(127, gen4.getHighestNonEscapedChar());
        assertEquals(ppr, gen4.getPrettyPrinter());

        // Test writing various types
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        JsonGenerator gen5 = generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS).enable(Feature.ESCAPE_NON_ASCII);
        gen5.writeStartObject();
        gen5.writeString("test");
        gen5.writeNumber(42);
        gen5.writeBoolean(true);
        gen5.writeNull();
        gen5.writeFieldName("key");
        gen5.writeValueAsString("value");
        gen5.writeEndObject();
        String json = bos.toString();
        assertFalse(gen5.isClosed());
        assertEquals("\"test\":42,\"key\":\"value\"", json);

        // Test writing BigDecimal
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        JsonGenerator gen6 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null)
                .enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gen6.writeNumber(new BigDecimal("123456789.123456"));
        String json2 = bos2.toString();
        assertEquals("123456789.123456", json2);

        // Test writing POJO
        JsonNode root = new ObjectMapper().readTree("{'a':1, 'b':'2'}");
        JsonGenerator gen7 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        gen7.writeTree(root);
        String jsonTree = gen7.getOutputContext().treeAsStrings().get(0);
        assertEquals("{\"a\":1,\"b\":\"2\"}", jsonTree);

        // Test setting codec and writing POJO with it
        ObjectCodec oc2 = new SimpleObjectMapper().getFactory().getCodec();
        JsonGenerator gen8 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), oc2);
        ObjectNode obj = new ObjectNode(oc2.getFactory());
        obj.put("a", 1);
        obj.put("b", "2");
        gen8.writeTree(obj);
        String jsonWithCodec = gen8.getOutputContext().treeAsStrings().get(0);
        assertEquals("{\"a\":1,\"b\":\"2\"}", jsonWithCodec);

        // Test writing binary data
        ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
        byte[] binaryData = { 0x01, 0x02, 0x03, 0x04 };
        JsonGenerator gen9 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen9.writeBinary(JsonGenerator.Base64Variants.STANDARD, new ByteArrayInputStream(binaryData), binaryData.length);
        String jsonBinary = gen9.getOutputContext().treeAsStrings().get(0);
        assertEquals("bWltcGxlZXM=".getBytes().length + ": " + new String(binaryData), jsonBinary);

        // Test closing generator
        gen5.close();
        assertTrue(gen5.isClosed());

        // Test writing after closing
        ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
        JsonGenerator gen10 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen10.close();
        try {
            gen10.writeTree(new ObjectMapper().readTree("{'a':1, 'b':'2'}"));
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}

class SimpleObjectMapper {
    ObjectMapper objectMapper;

    SimpleObjectMapper() {
        try {
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}