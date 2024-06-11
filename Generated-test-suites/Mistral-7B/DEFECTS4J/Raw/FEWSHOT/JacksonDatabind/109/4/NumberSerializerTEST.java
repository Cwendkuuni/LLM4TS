package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    /**
     * Static instance that is only to be used for {@link java.lang.Number}.
     */
    static final NumberSerializer NUMBER_SERIALIZER = NumberSerializer.instance;

    /**
     * Test serialization and deserialization of various types of {@link Number}.
     *
     * @throws IOException if any I/O error occurs
     * @throws JsonProcessingException if any JSON processing error occurs
     */
    @Test
    public void testSerializationAndDeserialization() throws IOException, JsonProcessingException {
        // Test serialization and deserialization of various types of Number
        for (Number number : new Number[]{new Integer(1), new Long(2), new Float(3.0f), new Double(4.0), new BigInteger("5"), new BigDecimal("6.5")}) {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = objectWriter.writeValueAsString(number);
            ObjectMapper objectMapper = new ObjectMapper();
            Object deserialized = objectMapper.readValue(json, Number.class);
            assertEquals(number, deserialized);
        }

        // Test serialization and deserialization of null Number
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(null);
        ObjectMapper objectMapper = new ObjectMapper();
        Object deserialized = objectMapper.readValue(json, Number.class);
        assertNull(deserialized);
    }

    /**
     * Test contextual serialization of {@link BigDecimal} as a {@link String}.
     *
     * @throws IOException if any I/O error occurs
     * @throws JsonProcessingException if any JSON processing error occurs
     */
    @Test
    public void testContextualSerializationOfBigDecimalAsString() throws IOException, JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().createGenerator();
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.serialize(new BigDecimal("123.45"), generator, new SerializerProvider());
        assertEquals("123.45", generator.getOutputContent().toString().trim());
    }

    /**
     * Test schema generation for {@link NumberSerializer}.
     */
    @Test
    public void testGetSchema() {
        JsonNode schema = NumberSerializer.instance.getSchema(new SerializerProvider(), Number.class);
        assertEquals("number", schema.asText());
    }

    /**
     * Test handling of {@link JsonFormat} annotation.
     */
    @Test
    public void testHandlingOfJsonFormatAnnotation() {
        class TestObject {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            Number number;
        }

        TestObject testObject = new TestObject();
        testObject.number = new BigDecimal("123.45");

        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig().getDefaultSerializerProvider().findValueSerializer(TestObject.class, "number");
        visitor.acceptJsonFormatVisitor(TestObject.class, new TypeReference<JavaType>() {}.getType(TestObject.class, "number"));

        assertEquals(JsonParser.NumberType.STRING, visitor.asNumberFormat().getNumberType());
    }
}