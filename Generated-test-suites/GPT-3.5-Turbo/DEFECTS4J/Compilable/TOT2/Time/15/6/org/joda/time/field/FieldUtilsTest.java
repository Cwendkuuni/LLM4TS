package org.joda.time.field;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException not thrown");
        } catch (ArithmeticException ex) {
            // expected exception
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-10, FieldUtils.safeAdd(-7, -3));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException not thrown");
        } catch (ArithmeticException ex) {
            // expected exception
        }
    }

    // Additional test cases for remaining methods can be added here

}