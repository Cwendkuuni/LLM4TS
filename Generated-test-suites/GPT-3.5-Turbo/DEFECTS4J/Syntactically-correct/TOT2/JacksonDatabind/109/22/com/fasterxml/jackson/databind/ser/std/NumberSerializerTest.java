import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test for BigDecimal serialization
        BigDecimal bigDecimal = new BigDecimal("123.45");
        // Add assert statement for bigDecimal serialization

        // Test for BigInteger serialization
        BigInteger bigInteger = new BigInteger("123456789");
        // Add assert statement for bigInteger serialization

        // Test for Long serialization
        Long longValue = 123L;
        // Add assert statement for longValue serialization

        // Test for Double serialization
        Double doubleValue = 123.45;
        // Add assert statement for doubleValue serialization

        // Test for Float serialization
        Float floatValue = 123.45F;
        // Add assert statement for floatValue serialization

        // Test for Integer serialization
        Integer integerValue = 123;
        // Add assert statement for integerValue serialization

        // Test for Byte serialization
        Byte byteValue = 1;
        // Add assert statement for byteValue serialization

        // Test for Short serialization
        Short shortValue = 123;
        // Add assert statement for shortValue serialization

        // Test for fallback "untyped" number write method
        Number untypedValue = 123456;
        // Add assert statement for untypedValue serialization
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test for schema with integer type
        // Add assert statement for schema with integer type

        // Test for schema with number type
        // Add assert statement for schema with number type
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test for integer format visitor for BigIntegers
        // Add assert statement for integer format visitor for BigIntegers

        // Test for float format visitor for BigDecimals
        // Add assert statement for float format visitor for BigDecimals

        // Test for number format visitor for other cases
        // Add assert statement for number format visitor for other cases
    }

}