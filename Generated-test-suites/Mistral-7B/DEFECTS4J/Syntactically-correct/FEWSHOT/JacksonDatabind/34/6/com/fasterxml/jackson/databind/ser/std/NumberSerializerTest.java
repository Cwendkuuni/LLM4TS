package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {
    private static final ObjectMapper mapper = JsonMapperFactory.getMapper();

    @Test
    public void testSerialize() throws IOException {
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator gen = provider.getGenerator();

        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of various Number types
        for (Number number : new Number[]{null, new BigDecimal("123.45"), new BigInteger("2345678901234567890"),
                123, 45, -123, -45, 123L, 45L, 123.0F, 123.45D, new Byte("127"), new Short("123")}) {
            serializer.serialize(number, gen, provider);
            gen.flush();
            TextNode actual = (TextNode) gen.getOutputPrinter().getValueAsTree();
            TextNode expected = JsonNodeFactory.instance.textNode(number.toString());
            assertEquals(expected, actual);
        }

        // test serialization of BigDecimal with scale > 0
        BigDecimal bd = new BigDecimal("123.456");
        serializer.serialize(bd, gen, provider);
        gen.flush();
        TextNode actualBd = (TextNode) gen.getOutputPrinter().getValueAsTree();
        TextNode expectedBd = JsonNodeFactory.instance.textNode(bd.toPlainString());
        assertEquals(expectedBd, actualBd);

        // test serialization of BigInteger
        BigInteger bi = new BigInteger("12345678901234567890");
        serializer.serialize(bi, gen, provider);
        gen.flush();
        TextNode actualBi = (TextNode) gen.getOutputPrinter().getValueAsTree();
        TextNode expectedBi = JsonNodeFactory.instance.textNode(bi.toString());
        assertEquals(expectedBi, actualBi);

        // test serialization of Integer and Long
        for (Number number : new Number[]{123, 123L}) {
            serializer.serialize(number, gen, provider);
            gen.flush();
            TextNode actual = (TextNode) gen.getOutputPrinter().getValueAsTree();
            TextNode expected = JsonNodeFactory.instance.textNode(number.toString());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testDeserialize() throws IOException, JsonProcessingException {
        JsonParser parser = createParser("[]");
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of various Number types
        for (Number number : new Number[]{null, new BigDecimal("123.45"), new BigInteger("2345678901234567890"),
                123, 45, -123, -45, 123L, 45L, 123.0F, 123.45D, new Byte("127"), new Short("123")}) {
            JsonNode node = JsonNodeFactory.instance.textNode(number.toString());
            Number deserialized = mapper.readValue(node.toString(), Number.class);
            assertEquals(number, deserialized);
        }

        // test deserialization of BigDecimal with scale > 0
        BigDecimal bd = new BigDecimal("123.456");
        JsonNode nodeBd = JsonNodeFactory.instance.textNode(bd.toPlainString());
        Number deserializedBd = mapper.readValue(nodeBd.toString(), Number.class);
        assertEquals(bd, deserializedBd);

        // test deserialization of BigInteger
        BigInteger bi = new BigInteger("12345678901234567890");
        JsonNode nodeBi = JsonNodeFactory.instance.textNode(bi.toString());
        Number deserializedBi = mapper.readValue(nodeBi.toString(), Number.class);
        assertEquals(bi, deserializedBi);

        // test deserialization of Integer and Long
        for (Number number : new Number[]{123, 123L}) {
            JsonNode node = JsonNodeFactory.instance.textNode(number.toString());
            Number deserialized = mapper.readValue(node.toString(), Number.class);
            assertEquals(number, deserialized);
        }
    }
}