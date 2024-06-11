import org.junit.Test;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("1 / 2", format.format(new Fraction(1, 2)));
    }

    @Test
    public void testFormat_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("1 1 / 2", format.format(new Fraction(3, 2)));
    }

    @Test
    public void testFormat_NegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("-1 1 / 2", format.format(new Fraction(-3, 2)));
    }

    @Test
    public void testFormat_WholeNumberAndNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("-2 1 / 2", format.format(new Fraction(-5, 2)));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(new Fraction(1, 2), format.parse("1 / 2"));
    }

    @Test
    public void testParse_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(new Fraction(3, 2), format.parse("1 1 / 2"));
    }

    @Test
    public void testParse_NegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(new Fraction(-3, 2), format.parse("-1 1 / 2"));
    }

    @Test
    public void testParse_WholeNumberAndNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(new Fraction(-5, 2), format.parse("-2 1 / 2"));
    }

    @Test
    public void testParse_InvalidFormat_NoFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("1"));
    }

    @Test
    public void testParse_InvalidFormat_InvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("a 1 / 2"));
    }

    @Test
    public void testParse_InvalidFormat_NoSpace() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("11/2"));
    }
}