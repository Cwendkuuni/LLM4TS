import static org.junit.Assert.*;

import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() {
        // Test serialization with BigDecimal
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        // Test BigDecimal value serialization
        BigDecimal input = new BigDecimal("123.45");
        // Assert serialization result
        assertEquals("123.45", serializer.serialize(input));
    }

    @Test
    public void testSerialize_BigInteger() {
        // Test serialization with BigInteger
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        // Test BigInteger value serialization
        BigInteger input = new BigInteger("12345");
        // Assert serialization result
        assertEquals("12345", serializer.serialize(input));
    }

    @Test
    public void testSerialize_Long() {
        // Test serialization with Long
        NumberSerializer serializer = new NumberSerializer(Long.class);
        // Test Long value serialization
        Long input = 123456789L;
        // Assert serialization result
        assertEquals("123456789", serializer.serialize(input));
    }

    @Test
    public void testSerialize_Double() {
        // Test serialization with Double
        NumberSerializer serializer = new NumberSerializer(Double.class);
        // Test Double value serialization
        Double input = 123.45;
        // Assert serialization result
        assertEquals("123.45", serializer.serialize(input));
    }

    @Test
    public void testSerialize_Float() {
        // Test serialization with Float
        NumberSerializer serializer = new NumberSerializer(Float.class);
        // Test Float value serialization
        Float input = 123.45f;
        // Assert serialization result
        assertEquals("123.45", serializer.serialize(input));
    }

    @Test
    public void testSerialize_Integer() {
        // Test serialization with Integer
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        // Test Integer value serialization
        Integer input = 12345;
        // Assert serialization result
        assertEquals("12345", serializer.serialize(input));
    }

    // Add more test cases for other methods if required

}