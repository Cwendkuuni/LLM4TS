import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate_typical() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate_minValue() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    @Test
    public void testSafeAdd_typical() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_overflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        FieldUtils.safeAdd(val1, val2);
    }

    // continue testing all methods...

}