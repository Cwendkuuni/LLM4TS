import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Number.class, NumberSerializer.instance);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    // Test case for serialize method with Integer
    @Test
    public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = 1;
        String expected = "1";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with Long
    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = 1L;
        String expected = "1";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with Double
    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = 1.0;
        String expected = "1.0";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with Float
    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = 1.0f;
        String expected = "1.0";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with Short
    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = (short) 1;
        String expected = "1";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with Byte
    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException, IOException {
        Number number = (byte) 1;
        String expected = "1";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with BigDecimal
    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        BigDecimal number = new BigDecimal("123.45");
        String expected = "123.45";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with BigInteger
    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        BigInteger number = BigInteger.valueOf(1234567890123456789L);
        String expected = "1234567890123456789";
        String actual = objectMapper.writeValueAsString(number);
        assertEquals(expected, actual);
    }

    // Test case for serialize method with non-supported Number type
    @Test(expected = JsonMappingException.class)
    public void testSerializeUnsupportedNumberType() throws JsonGenerationException, JsonMappingException, IOException {
        Object number = new Object() {
            public Number value() {
                return new BigInteger("123"); // not a valid BigInteger
            }
        };
        String actual = objectMapper.writeValueAsString(number);
        fail("Expected JsonMappingException but got: " + actual);
    }

    // Test case for getSchema method
    @Test
    public void testGetSchemaInteger() throws JsonMappingException {
        assertEquals("integer", objectMapper.readerForValueType(Number.class)
                .getSchema(objectMapper.getSerializerProvider(), NumberSerializer.class));
    }

    // Test case for getSchema method with BigDecimal
    @Test
    public void testGetSchemaBigDecimal() throws JsonMappingException {
        assertEquals("number", objectMapper.readerForValueType(Number.class)
                .getSchema(objectMapper.getSerializerProvider(), NumberSerializer.class));
    }

    // Test case for acceptJsonFormatVisitor method with Integer
    @Test
    public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
        // Not testing the implementation details, just checking that the method does not throw an exception
        objectMapper.readerForValueType(Number.class).acceptJsonFormatVisitor(null, Number.class);
    }

    // Test case for acceptJsonFormatVisitor method with BigDecimal
    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
        // Not testing the implementation details, just checking that the method does not throw an exception
        objectMapper.readerForValueType(Number.class).acceptJsonFormatVisitor(null, BigDecimal.class);
    }
}