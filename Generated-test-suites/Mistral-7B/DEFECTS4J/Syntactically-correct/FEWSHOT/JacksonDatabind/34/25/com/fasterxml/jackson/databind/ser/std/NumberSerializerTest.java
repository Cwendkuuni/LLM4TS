package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.databind.ser.std.TestUtils.createNumberSerializer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Test;

/**
 * Test cases for NumberSerializer class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializeSmallNumbers() throws Exception {
        NumberSerializer serializer = createNumberSerializer(Number.class);
        JsonGenerator gen = TestUtils.createJsonGenerator();

        serializer.serialize(1, gen, new SerializerProvider().builder().build());
        serializer.serialize(1.0, gen, new SerializerProvider().builder().build());
        serializer.serialize(1.0f, gen, new SerializerProvider().builder().build());
        serializer.serialize(1L, gen, new SerializerProvider().builder().build());
        serializer.serialize(-1, gen, new SerializerProvider().builder().build());
        serializer.serialize(-1.0, gen, new SerializerProvider().builder().build());
        serializer.serialize(-1.0f, gen, new SerializerProvider().builder().build());
        serializer.serialize(-1L, gen, new SerializerProvider().builder().build());

        TestUtils.assertJsonEquals("1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("1.0", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("1.0", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer())); // float serializes to double
        TestUtils.assertJsonEquals("1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("-1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("-1.0", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("-1.0", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer())); // float serializes to double
        TestUtils.assertJsonEquals("-1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));

        gen.close();
    }

    @Test
    public void testSerializeBigNumbers() throws Exception {
        NumberSerializer serializer = createNumberSerializer(BigInteger.class);
        JsonGenerator gen = TestUtils.createJsonGenerator();

        // Test with positive big integer
        BigInteger bigInt = new BigInteger("1234567890123456789012345678901234567890");
        serializer.serialize(bigInt, gen, new SerializerProvider().builder().build());

        TestUtils.assertJsonEquals("1234567890123456789012345678901234567890", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        gen.close();

        // Test with negative big integer
        BigInteger negativeBigInt = new BigInteger("-1234567890123456789012345678901234567890");
        serializer.serialize(negativeBigInt, gen, new SerializerProvider().builder().build());

        TestUtils.assertJsonEquals("-1234567890123456789012345678901234567890", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        gen.close();
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = createNumberSerializer(BigDecimal.class);
        JsonGenerator gen = TestUtils.createJsonGenerator();

        BigDecimal bigDecimal = new BigDecimal("123.456.789,012.345.678.9012.345.678.9012.345");
        serializer.serialize(bigDecimal, gen, new SerializerProvider().builder().build());

        TestUtils.assertJsonEquals("123.456.789,012.345.678.9012.345.678.9012.345", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        gen.close();
    }

    @Test
    public void testGetSchema() throws Exception {
        NumberSerializer serializer = createNumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(new SerializerProvider().builder().build(), Number.class);

        assertEquals("number", schema.asText());
        assertEquals(true, schema.get("hasMinimum") == null);
        assertEquals(true, schema.get("hasMaximum") == null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer serializer = createNumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonFormatVisitor<>(new ObjectMapper().getFactory());
        serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        assertNotNull(visitor.getNumberFormat());
        assertNotNull(visitor.getNumberFormat().getDecimalFormatSymbols());
        assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitor.getNumberFormat().getNumberType());
    }

    @Test
    public void testSerializeOtherNumberTypes() throws Exception {
        NumberSerializer serializer = createNumberSerializer(Number.class);
        JsonGenerator gen = TestUtils.createJsonGenerator();

        serializer.serialize((byte) 1, gen, new SerializerProvider().builder().build());
        serializer.serialize((short) 1, gen, new SerializerProvider().builder().build());
        serializer.serialize((byte) 0, gen, new SerializerProvider().builder().build());

        TestUtils.assertJsonEquals("1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("1", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        TestUtils.assertJsonEquals("0", gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));

        gen.close();

        serializer.serialize(null, gen, new SerializerProvider().builder().build());
        assertNull(gen.getOutputStrategy().getEncodedBase64Value(gen.getOutputBuffer()));
        gen.close();
    }
}