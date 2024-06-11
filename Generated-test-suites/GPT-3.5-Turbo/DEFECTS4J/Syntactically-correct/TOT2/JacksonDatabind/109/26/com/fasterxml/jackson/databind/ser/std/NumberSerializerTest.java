import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        // Test serialization of BigDecimal
        // Edge case: big decimal with scale
        BigDecimal input = new BigDecimal("123.456");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(input, generator, null);
        generator.close();
        assertEquals("\"123.456\"", writer.toString());
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        // Test serialization of BigInteger
        BigInteger input = new BigInteger("12345");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(input, generator, null);
        generator.close();
        assertEquals("12345", writer.toString());
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        // Test serialization of Long
        // Edge case: Long value
        Long input = 1234567890L;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(input, generator, null);
        generator.close();
        assertEquals("1234567890", writer.toString());
    }

    // Add more test cases as needed for other methods

}