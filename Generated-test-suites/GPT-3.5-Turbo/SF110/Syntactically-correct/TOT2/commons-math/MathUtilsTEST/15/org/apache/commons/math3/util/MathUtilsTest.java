package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(123456, MathUtils.hash(123.456));
    }

    @Test
    public void testHash_doubleArray() {
        double[] array = {1.0, 2.0, 3.0};
        assertEquals(-123456, MathUtils.hash(array));
    }
    
    // Other test cases for remaining methods...

}