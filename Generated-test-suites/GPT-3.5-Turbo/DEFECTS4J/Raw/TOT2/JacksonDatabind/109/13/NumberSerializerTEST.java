import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializerInstance() {
        assertSame(NumberSerializer.instance, new NumberSerializer(Number.class));
    }

    @Test
    public void testSerializeBigDecimal() {
        // Test serialization of BigDecimal
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeBigInteger() {
        // Test serialization of BigInteger
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeLong() {
        // Test serialization of Long
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeDouble() {
        // Test serialization of Double
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeFloat() {
        // Test serialization of Float
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeInteger() {
        // Test serialization of Integer
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeByte() {
        // Test serialization of Byte
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeShort() {
        // Test serialization of Short
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testSerializeFallback() {
        // Test serialization of fallback method
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Add test here
    }
}