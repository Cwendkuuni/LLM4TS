package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.junit.Test;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(NumberSerializer.instance);
    }

    /**
     * Test createContextual method.
     */
    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = mapper.getSerializerProvider().findValueSerializer(BigDecimal.class);
        assertSame(ToStringSerializer.instance, serializer);
    }

    /**
     * Test serialize method with BigDecimal.
     */
    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal number = new BigDecimal("123.45");
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with BigInteger.
     */
    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger number = new BigInteger("12345678901234567890");
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Long.
     */
    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long number = 123L;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Double.
     */
    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double number = 123.45;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Float.
     */
    @Test
    public void testSerializeFloat() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float number = 123.45f;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Integer.
     */
    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer number = 123;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Byte.
     */
    @Test
    public void testSerializeByte() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte number = (byte) 123;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with Short.
     */
    @Test
    public void testSerializeShort() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short number = (short) 123;
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialize method with String.
     */
    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(String.class);
        String number = "123";
        String json = mapper.writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(new BigInteger(number), deserializedNumber);
    }

    /**
     * Test getSchema method.
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        String schema = new NumberSerializer(Number.class).getSchema(mapper.getSerializerProvider(), Number.class.getType()).toString();
        assertEquals("number", schema);
    }

    /**
     * Test acceptJsonFormatVisitor method with BigDecimal.
     */
    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormat.Value format = new JsonFormat.Value();
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_DECIMAL;

        try {
            serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().findValueSerializerProvider(), mapper.getTypeFactory().constructType(JsonFormat.Value.class));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        assertEquals(format.getShape(), JsonFormat.Value.Shape.STRING);
        assertEquals(format.getNumberType(), numberType);
    }

    /**
     * Test acceptJsonFormatVisitor method with other types.
     */
    @Test
    public void testAcceptJsonFormatVisitorOtherTypes() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormat.Value format = new JsonFormat.Value();

        try {
            serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().findValueSerializerProvider(), mapper.getTypeFactory().constructType(JsonFormat.Value.class));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().findValueSerializerProvider(), mapper.getTypeFactory().constructType(Integer.class));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().findValueSerializerProvider(), mapper.getTypeFactory().constructType(JsonFormat.Value.class));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }
}