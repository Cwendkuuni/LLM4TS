import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testInstanceCreation() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of BigDecimal
        // Add test logic here
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of BigInteger
        // Add test logic here
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of Long
        // Add test logic here
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of Double
        // Add test logic here
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of Float
        // Add test logic here
    }

    @Test
    public void testSerializeIntByteShort() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test serialization of Integer, Byte, and Short
        // Add test logic here
    }

    @Test
    public void testSerializeFallback() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test fallback serialization
        // Add test logic here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test get schema method
        // Add test logic here
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Test accept JSON format visitor method
        // Add test logic here
    }

}