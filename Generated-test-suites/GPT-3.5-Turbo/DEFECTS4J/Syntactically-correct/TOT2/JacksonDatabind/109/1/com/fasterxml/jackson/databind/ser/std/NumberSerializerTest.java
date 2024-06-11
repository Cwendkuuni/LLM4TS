import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        BigDecimal value = new BigDecimal("123.45");
        // Test serialization of BigDecimal
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_BigInteger() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        BigInteger value = BigInteger.valueOf(12345);
        // Test serialization of BigInteger
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Long() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Long value = 123L;
        // Test serialization of Long
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Double() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Double value = 123.45;
        // Test serialization of Double
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Float() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Float value = 123.45f;
        // Test serialization of Float
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Integer() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Integer value = 123;
        // Test serialization of Integer
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Byte() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Byte value = 123;
        // Test serialization of Byte
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_Short() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Short value = 123;
        // Test serialization of Short
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testSerialize_UnknownNumberType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Test serialization of an unknown type of number
        // Check if the output JSON matches the expected value
        // Write the test case here
    }

    @Test
    public void testGetSchema_Integer() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Test getting the schema for an integer number
        // Check if the returned schema node is of type "integer"
        // Write the test case here
    }

    @Test
    public void testGetSchema_Number() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Test getting the schema for a general number
        // Check if the returned schema node is of type "number"
        // Write the test case here
    }

    @Test
    public void testAcceptJsonFormatVisitor_Integer() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Test accepting a Json format visitor for an integer number
        // Check if the visitor expects the format of a BigInteger
        // Write the test case here
    }

    @Test
    public void testAcceptJsonFormatVisitor_Number() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // Test accepting a Json format visitor for a general number
        // Check if the visitor expects a generic number format
        // Write the test case here
    }

}