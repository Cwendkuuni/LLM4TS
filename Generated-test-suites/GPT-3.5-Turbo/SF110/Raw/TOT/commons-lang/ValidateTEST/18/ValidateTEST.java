import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ValidateTest {
    
    @Test
    public void testIsTrueWithLongValue() {
        boolean expression = true;
        String message = "The expression is false for value %d";
        long value = 100;
        try {
            Validate.isTrue(expression, message, value);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        expression = false;
        try {
            Validate.isTrue(expression, message, value);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals(String.format(message, value), e.getMessage());
        }
    }
    
    @Test
    public void testIsTrueWithDoubleValue() {
        boolean expression = true;
        String message = "The expression is false for value %f";
        double value = 5.5;
        try {
            Validate.isTrue(expression, message, value);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        expression = false;
        try {
            Validate.isTrue(expression, message, value);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals(String.format(message, value), e.getMessage());
        }
    }
    
    @Test
    public void testIsTrueWithObjectValues() {
        boolean expression = true;
        String message = "The expression is false for values %s";
        Object[] values = {1, "A", true};
        try {
            Validate.isTrue(expression, message, values);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        expression = false;
        try {
            Validate.isTrue(expression, message, values);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals(String.format(message, values), e.getMessage());
        }
    }
    
    @Test
    public void testIsTrueWithoutMessage() {
        boolean expression = true;
        try {
            Validate.isTrue(expression);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        expression = false;
        try {
            Validate.isTrue(expression);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }
    
    // Continue with the test methods for other Validate class methods
    
}