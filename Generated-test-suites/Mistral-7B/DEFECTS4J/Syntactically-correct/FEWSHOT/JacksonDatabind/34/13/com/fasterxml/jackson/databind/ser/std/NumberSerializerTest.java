package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.assertEquals;
import static com.fasterxml.jackson.core.util.TestUtils.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
public class NumberSerializerTest {

    private static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Test
    public void testInstance() {
        assertTrue(NumberSerializer.class.isInstance(NumberSerializer.instance));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        serializer.serialize(null, gen, new SerializerProvider());
        assertNull(gen.getOutputBuffer().toString());

        serializer.serialize(new BigDecimal("123.45"), gen, new SerializerProvider());
        assertEquals("123.45", gen.getOutputBuffer().toString());

        serializer.serialize(new BigInteger("12345"), gen, new SerializerProvider());
        assertEquals("12345", gen.getOutputBuffer().toString());

        serializer.serialize(new Integer(123), gen, new SerializerProvider());
        assertEquals("123", gen.getOutputBuffer().toString());

        serializer.serialize(new Long(12345), gen, new SerializerProvider());
        assertEquals("12345", gen.getOutputBuffer().toString());

        serializer.serialize(new Double(123.45), gen, new SerializerProvider());
        assertEquals("123.45", gen.getOutputBuffer().toString());

        serializer.serialize(new Float(123.45f), gen, new SerializerProvider());
        assertEquals("123.45", gen.getOutputBuffer().toString());

        serializer.serialize(new Byte((byte) 123), gen, new SerializerProvider());
        assertEquals("123", gen.getOutputBuffer().toString());

        serializer.serialize(new Short((short) 123), gen, new SerializerProvider());
        assertEquals("123", gen.getOutputBuffer().toString());

        serializer.serialize(new BigDecimal("1.23E-5"), gen, new SerializerProvider());
        assertEquals("1.23E-5", gen.getOutputBuffer().toString());

        serializer.serialize(new BigInteger("123456789012345678901234567890"), gen, new SerializerProvider());
        assertEquals("123456789012345678901234567890", gen.getOutputBuffer().toString());

        gen.close();
    }

    @Test
    public void testGetSchema() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new TextNode("{ \"number\": { \"type\": \"number\", \"description\": \"Number type\" } }").toString());
        ObjectCodec codec = parser.getCodec();
        ObjectReader reader = codec.readerFor(JsonNode.class);

        JsonNode schema = NumberSerializer.instance.getSchema(new SerializerProvider(), Number.class);
        JsonNode expectedSchema = reader.readValue(new TextNode("{\"number\": {\"type\": \"number\", \"description\": \"Number type\"}}").textValue());

        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonFormatVisitor(new ObjectMapper(), new SerializerProvider());
        NumberSerializer serializer = NumberSerializer.instance;

        // Test for int type
        serializer.acceptJsonFormatVisitor(visitor, Integer.TYPE);
        assertTrue(visitor.isAccepted(JsonParser.NumberType.INT));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.LONG));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.FLOAT));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.BIG_INTEGER));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.BIG_DECIMAL));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.TEXT));

        // Test for non-int type
        visitor = new JsonSerializer.StdJsonFormatVisitor(new ObjectMapper(), new SerializerProvider());
        serializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);
        assertTrue(visitor.isAccepted(JsonParser.NumberType.BIG_DECIMAL));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.INT));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.LONG));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.FLOAT));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.BIG_INTEGER));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.TEXT));
    }
}