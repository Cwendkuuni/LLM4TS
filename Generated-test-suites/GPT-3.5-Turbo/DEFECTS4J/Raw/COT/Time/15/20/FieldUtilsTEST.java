
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldUtilsTest {
    // Test cases for each method
    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(5);
        assertEquals(-5, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateWithMinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    // ... (Rest of the test cases)
    
}

//