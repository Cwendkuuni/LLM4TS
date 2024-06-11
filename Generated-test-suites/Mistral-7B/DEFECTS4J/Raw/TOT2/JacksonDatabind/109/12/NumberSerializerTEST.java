import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    // Test for serialize method with Integer input
    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = 123;

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with BigInteger input
    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = new BigInteger("123456789012345678901234567890");

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123456789012345678901234567890";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with BigDecimal input
    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = new BigDecimal("123.456");

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123.456";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with Long input
    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = 1234567890123456789L;

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "1234567890123456789";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with Double input
    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = 123.456;

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123.456";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with Float input
    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = 123.45f;

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123.45";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with custom Number input
    @Test
    public void testSerializeCustomNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(MyCustomNumber.class);
        JsonGenerator jsonGenerator = null; // Initialize JsonGenerator later
        Number number = new MyCustomNumber(123);

        // Call serialize method
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());

        // Verify the result
        String expectedResult = "123";
        assertEquals(expectedResult, jsonGenerator.getOutputBuffer().toString());
    }

    // Test for serialize method with null input
    @Test(expected = JsonProcessingException.class)
    public void testSerializeNull() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Call serialize method with null input
        numberSerializer.serialize(null, new JsonGenerator(), new SerializerProvider());
    }
}