package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-10, FieldUtils.safeNegate(10));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeNegate(-Integer.MAX_VALUE));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));
        assertEquals(0, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    // Add more test cases for the remaining methods...

}