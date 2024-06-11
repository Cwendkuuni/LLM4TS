package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorDoubleEpsilonMaxIterations() {
        try {
            Fraction fraction = new Fraction(1.5, 0.01, 1000);
            assertEquals(new Fraction(3, 2), fraction);
        } catch (FractionConversionException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testConstructorIntInt() {
        Fraction fraction = new Fraction(3, 6);
        assertEquals(new Fraction(1, 2), fraction);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        assertEquals(new Fraction(2, 3), fraction.abs());
    }

    // Other test cases for each public method should be added here
    
}