package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.Validate;

public class ValidateTest {

    @Test
    public void testIsTrue_long() {
        try {
            Validate.isTrue(10 > 5, "The value %d is not greater than 5", 10);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testIsNull() {
        try {
            Validate.notNull(null);
        } catch (NullPointerException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testNotEmpty_array() {
        Integer[] array = {1, 2, 3};
        try {
            Validate.notEmpty(array);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception thrown");
        }
    }

    // Other test methods for remaining public methods in Validate class

}