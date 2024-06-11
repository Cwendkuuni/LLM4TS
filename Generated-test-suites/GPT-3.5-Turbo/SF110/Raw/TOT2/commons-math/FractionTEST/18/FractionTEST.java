import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(3, 7);
        
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    // Test case for add method
    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 4);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.add(f2);

        assertEquals(11, result.getNumerator());
        assertEquals(12, result.getDenominator());
    }

    // Test case for subtract method
    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(5, 8);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.subtract(f2);

        assertEquals(1, result.getNumerator());
        assertEquals(8, result.getDenominator());
    }

    // Test case for multiply method
    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(2, 5);
        Fraction f2 = new Fraction(3, 7);
        Fraction result = f1.multiply(f2);

        assertEquals(6, result.getNumerator());
        assertEquals(35, result.getDenominator());
    }

    // Test case for divide method
    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(7, 9);
        Fraction f2 = new Fraction(4, 5);
        Fraction result = f1.divide(f2);

        assertEquals(35, result.getNumerator());
        assertEquals(36, result.getDenominator());
    }

    // Test case for abs method
    @Test
    public void testAbs() {
        Fraction f1 = new Fraction(-5, 8);
        Fraction result = f1.abs();

        assertEquals(5, result.getNumerator());
        assertEquals(8, result.getDenominator());
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(3, 5);
        Fraction f2 = new Fraction(6, 10);

        assertEquals(f1.hashCode(), f2.hashCode());
    }

    // Test case for reciprocal method
    @Test
    public void testReciprocal() {
        Fraction f1 = new Fraction(3, 2);
        Fraction result = f1.reciprocal();

        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    // Test case for compareTo method
    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);

        assertTrue(f1.compareTo(f2) < 0);
    }

    // Test case for doubleValue method
    @Test
    public void testDoubleValue() {
        Fraction f1 = new Fraction(5, 4);

        assertEquals(1.25, f1.doubleValue(), 0.001);
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Fraction f1 = new Fraction(3, 7);

        assertEquals("3 / 7", f1.toString());
    }

    // Test case for error scenario - divide by zero
    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction f1 = new Fraction(4, 0);
        Fraction result = f1.reciprocal();
    }
}