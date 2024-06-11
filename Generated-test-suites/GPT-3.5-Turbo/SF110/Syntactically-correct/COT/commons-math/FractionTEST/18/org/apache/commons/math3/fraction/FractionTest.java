import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testAbs() {
        Fraction f1 = new Fraction(3, 5);
        Fraction f2 = new Fraction(-3, 5);
        assertEquals(f1, f2.abs());
    }
    
    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 3);
        assertEquals(1, f1.compareTo(f2));
    }
    
    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.0001);
    }
    
    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(4, 6);
        assertEquals(f1, f2);
    }
    
    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(3, 4);
        assertEquals(0.75f, f.floatValue(), 0.0001);
    }
    
    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(2, 5);
        assertEquals(5, f.getDenominator());
    }
    
    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 7);
        assertEquals(3, f.getNumerator());
    }
    
    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(4, 6);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
    
    @Test
    public void testIntValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1, f.intValue());
    }
    
    @Test
    public void testLongValue() {
        Fraction f = new Fraction(5, 2);
        assertEquals(2, f.longValue());
    }
    
    @Test
    public void testNegate() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(-3, 4);
        assertEquals(f2, f1.negate());
    }
    
    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(3, 5);
        Fraction reciprocal = new Fraction(5, 3);
        assertEquals(reciprocal, f.reciprocal());
    }
    
    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction sum = new Fraction(5, 4);
        assertEquals(sum, f1.add(f2));
    }
    
    @Test
    public void testAddInt() {
        Fraction f = new Fraction(2, 3);
        Fraction sum = new Fraction(8, 3);
        assertEquals(sum, f.add(2));
    }
    
    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        Fraction diff = new Fraction(1, 4);
        assertEquals(diff, f1.subtract(f2));
    }
    
    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(2, 3);
        Fraction diff = new Fraction(1, 3);
        assertEquals(diff, f.subtract(1));
    }
    
    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction product = new Fraction(1, 2);
        assertEquals(product, f1.multiply(f2));
    }
    
    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(2, 3);
        Fraction product = new Fraction(4, 3);
        assertEquals(product, f.multiply(2));
    }
    
    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 3);
        Fraction quotient = new Fraction(9, 8);
        assertEquals(quotient, f1.divide(f2));
    }
    
    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(2, 3);
        Fraction quotient = new Fraction(2, 9);
        assertEquals(quotient, f.divide(4));
    }
    
    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 3);
        assertEquals(33.3333, f.percentageValue(), 0.0001);
    }
    
    @Test
    public void testGetReducedFraction() {
        Fraction f1 = new Fraction(6, 8);
        Fraction f2 = new Fraction(3, 4);
        assertEquals(f2, Fraction.getReducedFraction(f1.getNumerator(), f1.getDenominator()));
    }
    
    @Test
    public void testToString() {
        Fraction f = new Fraction(2, 3);
        assertEquals("2 / 3", f.toString());
    }
}