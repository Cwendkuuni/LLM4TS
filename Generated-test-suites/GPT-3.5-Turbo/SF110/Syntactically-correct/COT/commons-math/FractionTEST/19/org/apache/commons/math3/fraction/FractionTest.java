import org.junit.Before;
import org.junit.Test;

public class FractionTest {

    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(3, 4);
    }

    @Test
    public void testAbs() {
        Fraction result = fraction.abs();
        assert result.getNumerator() == 3;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testCompareTo() {
        Fraction fraction2 = new Fraction(2, 5);
        int result = fraction.compareTo(fraction2);
        assert result > 0;
    }

    @Test
    public void testDoubleValue() {
        double result = fraction.doubleValue();
        assert result == 0.75;
    }

    @Test
    public void testEquals() {
        Fraction fraction2 = new Fraction(3, 4);
        boolean result = fraction.equals(fraction2);
        assert result == true;
    }

    @Test
    public void testFloatValue() {
        float result = fraction.floatValue();
        assert result == 0.75f;
    }

    @Test
    public void testGetDenominator() {
        int result = fraction.getDenominator();
        assert result == 4;
    }

    @Test
    public void testGetNumerator() {
        int result = fraction.getNumerator();
        assert result == 3;
    }

    @Test
    public void testHashCode() {
        int result = fraction.hashCode();
        assert result == -1440579579;
    }

    @Test
    public void testIntValue() {
        int result = fraction.intValue();
        assert result == 0;
    }

    @Test
    public void testLongValue() {
        long result = fraction.longValue();
        assert result == 0L;
    }

    @Test
    public void testNegate() {
        Fraction result = fraction.negate();
        assert result.getNumerator() == -3;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testReciprocal() {
        Fraction result = fraction.reciprocal();
        assert result.getNumerator() == 4;
        assert result.getDenominator() == 3;
    }

    @Test
    public void testAdd() {
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction.add(fraction2);
        assert result.getNumerator() == 5;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testAddInt() {
        Fraction result = fraction.add(2);
        assert result.getNumerator() == 11;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testSubtract() {
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction.subtract(fraction2);
        assert result.getNumerator() == 1;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testSubtractInt() {
        Fraction result = fraction.subtract(2);
        assert result.getNumerator() == -5;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testMultiplyFraction() {
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction.multiply(fraction2);
        assert result.getNumerator() == 3;
        assert result.getDenominator() == 8;
    }

    @Test
    public void testMultiplyInt() {
        Fraction result = fraction.multiply(2);
        assert result.getNumerator() == 6;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testDivideFraction() {
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction.divide(fraction2);
        assert result.getNumerator() == 6;
        assert result.getDenominator() == 4;
    }

    @Test
    public void testDivideInt() {
        Fraction result = fraction.divide(2);
        assert result.getNumerator() == 3;
        assert result.getDenominator() == 8;
    }

    @Test
    public void testPercentageValue() {
        double result = fraction.percentageValue();
        assert result == 75.0;
    }

    @Test
    public void testGetReducedFraction() {
        Fraction result = Fraction.getReducedFraction(6, 4);
        assert result.getNumerator() == 3;
        assert result.getDenominator() == 2;
    }

    @Test
    public void testToString() {
        String result = fraction.toString();
        assert result.equals("3 / 4");
    }

    @Test
    public void testGetField() {
        FractionField result = fraction.getField();
        assert result == FractionField.getInstance();
    }
}