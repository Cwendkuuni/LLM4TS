import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.456");
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123.456", generator.toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("123456789");
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123456789", generator.toString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123", generator.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 123L;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123", generator.toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 123.456;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123.456", generator.toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 123.456f;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("123.456", generator.toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 127;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("127", generator.toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 32767;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(value, generator, provider);
        // Assert the serialized output matches the expected value
        assertEquals("32767", generator.toString());
    }

    @Test
    public void testSerializeUnsupportedType() throws IOException {
        // Try to serialize a type other than the supported ones
        try {
            Boolean value = true;
            JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
            SerializerProvider provider = objectMapper.getSerializerProvider();
            numberSerializer.serialize(value, generator, provider);
            // If an exception is not thrown, fail the test
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert that the expected IOException is thrown
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        JsonNode schema = numberSerializer.getSchema(provider, null);
        // Assert that the returned schema is of the correct type
        assertTrue(schema.isValueNode());
        assertEquals("number", schema.textValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Test for _isInt = true
        JsonFormatVisitorWrapper visitor1 = objectMapper.getSerializerProvider()
                .getVisitor(objectMapper.getSerializationConfig(), Integer.class);
        numberSerializer.acceptJsonFormatVisitor(visitor1, null);
        // Assert that the correct format visitor is expected
        assertTrue(visitor1 instanceof JsonIntegerFormatVisitor);

        // Test for _isInt = false
        JsonFormatVisitorWrapper visitor2 = objectMapper.getSerializerProvider()
                .getVisitor(objectMapper.getSerializationConfig(), BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor2, null);
        // Assert that the correct format visitor is expected
        assertTrue(visitor2 instanceof JsonFloatFormatVisitor);
    }

}