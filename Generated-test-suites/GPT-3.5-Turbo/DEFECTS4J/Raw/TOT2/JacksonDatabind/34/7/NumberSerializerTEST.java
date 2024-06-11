import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize_withBigDecimal_shouldWriteNumber() {
        // Test serializing a BigDecimal
        // Setup
        BigDecimal value = new BigDecimal("123.456");
        
        // Execution
        // Call serialize method
        
        // Assertion
        // Check if the output is correct
    }

    @Test
    public void testSerialize_withBigInteger_shouldWriteNumber() {
        // Test serializing a BigInteger
        // Setup
        BigInteger value = BigInteger.valueOf(123);
        
        // Execution
        // Call serialize method
        
        // Assertion
        // Check if the output is correct
    }

    @Test
    public void testSerialize_withInteger_shouldWriteNumber() {
        // Test serializing an Integer
        // Setup
        Integer value = 123;
        
        // Execution
        // Call serialize method
        
        // Assertion
        // Check if the output is correct
    }

    // Add more test cases for other public methods as well

    @Test
    public void testGetSchema_withIsIntTrue_shouldReturnIntegerSchema() {
        // Test getSchema method with _isInt as true
        // Setup
        
        // Execution
        // Call getSchema method
        
        // Assertion
        // Check if the returned schema is of type 'integer'
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIsIntTrue_shouldVisitIntFormat() {
        // Test acceptJsonFormatVisitor method with _isInt as true
        // Setup
        
        // Execution
        // Call acceptJsonFormatVisitor method
        
        // Assertion
        // Check if the visitor visited int format for typeHint
    }

}