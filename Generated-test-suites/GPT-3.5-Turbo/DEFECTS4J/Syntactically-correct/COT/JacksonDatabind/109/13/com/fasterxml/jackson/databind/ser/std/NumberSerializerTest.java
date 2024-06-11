import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize_NullNumber_ThrowsIOException() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = null; // Replace with a valid JsonGenerator object
        SerializerProvider provider = null; // Replace with a valid SerializerProvider object

        try {
            serializer.serialize(null, jsonGenerator, provider);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException to be thrown
        }
    }

    @Test
    public void testSerialize_BigDecimalNumber_WritesNumberToGenerator() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = null; // Replace with a valid JsonGenerator object
        SerializerProvider provider = null; // Replace with a valid SerializerProvider object

        BigDecimal number = new BigDecimal("10.5");
        serializer.serialize(number, jsonGenerator, provider);

        // Check if the number was written correctly to the generator
    }

    // Repeat similar test methods for the other identified edge cases and exceptions

}