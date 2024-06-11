import org.junit.Test;

public class FractionTest {
    
    @Test
    public void testConstructor1() {
        Fraction fraction = new Fraction(2.5);
        
        assert fraction.getNumerator() == 5;
        assert fraction.getDenominator() == 2;
    }
    
    @Test
    public void testConstructor2() {
        Fraction fraction = new Fraction(2.5, 0.01, 100);
        
        assert fraction.getNumerator() == 5;
        assert fraction.getDenominator() == 2;
    }
    
    @Test
    public void testConstructor3() {
        Fraction fraction = new Fraction(2.5, 100);
        
        assert fraction.getNumerator() == 5;
        assert fraction.getDenominator() == 2;
    }
    
    @Test
    public void testConstructor4() {
        Fraction fraction = new Fraction(2.5, 0.01, 100, 50);
        
        assert fraction.getNumerator() == 5;
        assert fraction.getDenominator() == 2;
    }
    
    @Test
    public void testConstructor5() {
        Fraction fraction = new Fraction(3);
        
        assert fraction.getNumerator() == 3;
        assert fraction.getDenominator() == 1;
    }
    
    @Test
    public void testConstructor6() {
        Fraction fraction = new Fraction(4, 5);
        
        assert fraction.getNumerator() == 4;
        assert fraction.getDenominator() == 5;
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominatorConstructor() {
        Fraction fraction = new Fraction(2, 0);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testOverflowConstructor() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, -1);
    }
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction absFraction = fraction.abs();
        
        assert absFraction.getNumerator() == 2;
        assert absFraction.getDenominator() == 3;
        
        Fraction fraction2 = new Fraction(2, 3);
        Fraction absFraction2 = fraction2.abs();
        
        assert absFraction2.getNumerator() == 2;
        assert absFraction2.getDenominator() == 3;
    }
    
    // ...
    
}