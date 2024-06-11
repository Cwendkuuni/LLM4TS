import org.junit.Test;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-5, 2);
        Fraction result = f.abs();
        assert result.equals(new Fraction(5, 2));
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(5, 8);
        assert f1.compareTo(f2) > 0;
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.doubleValue();
        assert result == 0.5;
    }

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(6, 8);
        assert f1.equals(f2);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        float result = f.floatValue();
        assert result == 0.5;
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(3, 4);
        int result = f.getDenominator();
        assert result == 4;
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 4);
        int result = f.getNumerator();
        assert result == 3;
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(6, 8);
        assert f1.hashCode() == f2.hashCode();
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(3, 4);
        int result = f.intValue();
        assert result == 0;
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(3, 4);
        long result = f.longValue();
        assert result == 0;
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.negate();
        assert result.equals(new Fraction(-3, 4));
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.reciprocal();
        assert result.equals(new Fraction(4, 3));
    }

    @Test
    public void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.add(f2);
        assert result.equals(new Fraction(3, 4));
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(1);
        assert result.equals(new Fraction(3, 2));
    }

    @Test
    public void testSubtract() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        Fraction result = f1.subtract(f2);
        assert result.equals(new Fraction(1, 4));
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.subtract(1);
        assert result.equals(new Fraction(-1, 4));
    }

    @Test
    public void testMultiply() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 5);
        Fraction result = f1.multiply(f2);
        assert result.equals(new Fraction(3, 10));
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.multiply(2);
        assert result.equals(new Fraction(3, 2));
    }

    @Test
    public void testDivide() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(2, 5);
        Fraction result = f1.divide(f2);
        assert result.equals(new Fraction(15, 8));
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.divide(2);
        assert result.equals(new Fraction(3, 8));
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        double result = f.percentageValue();
        assert result == 50.0;
    }

    @Test
    public void testGetReducedFraction() {
        Fraction result = Fraction.getReducedFraction(4, 8);
        assert result.equals(new Fraction(1, 2));
    }

    @Test
    public void testToString() {
        Fraction f = new Fraction(3, 4);
        String result = f.toString();
        assert result.equals("3 / 4");
    }
}