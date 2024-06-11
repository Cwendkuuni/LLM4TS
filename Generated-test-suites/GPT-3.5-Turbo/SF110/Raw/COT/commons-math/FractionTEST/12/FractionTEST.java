import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testAbs() {
        // Test positive numerator
        Fraction f1 = new Fraction(3, 4);
        assertEquals(new Fraction(3, 4), f1.abs());

        // Test negative numerator
        Fraction f2 = new Fraction(-3, 4);
        assertEquals(new Fraction(3, 4), f2.abs());

        // Test zero numerator
        Fraction f3 = new Fraction(0, 4);
        assertEquals(new Fraction(0, 4), f3.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(1, 2);

        // Test equal fractions
        assertEquals(0, f1.compareTo(f2));

        // Test greater fraction
        assertEquals(1, f1.compareTo(f3));

        // Test smaller fraction
        assertEquals(-1, f3.compareTo(f1));
    }

    // ... more test cases for other methods ...

}