import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test formatting positive fraction
        Fraction fraction1 = new Fraction(3, 4);
        assertEquals("3/4", format.format(fraction1));

        // Test formatting negative fraction
        Fraction fraction2 = new Fraction(-5, 6);
        assertEquals("-5/6", format.format(fraction2));

        // Test formatting whole number
        Fraction fraction3 = new Fraction(4);
        assertEquals("4", format.format(fraction3));

        // Test formatting zero fraction
        Fraction fraction4 = new Fraction(0);
        assertEquals("0", format.format(fraction4));

        // Test formatting 1/1 fraction
        Fraction fraction5 = new Fraction(1, 1);
        assertEquals("1", format.format(fraction5));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test parsing positive fraction
        Fraction fraction1 = format.parse("3/4");
        assertEquals(3, fraction1.getNumerator());
        assertEquals(4, fraction1.getDenominator());

        // Test parsing negative fraction
        Fraction fraction2 = format.parse("-5/6");
        assertEquals(-5, fraction2.getNumerator());
        assertEquals(6, fraction2.getDenominator());

        // Test parsing whole number
        Fraction fraction3 = format.parse("4");
        assertEquals(4, fraction3.getNumerator());
        assertEquals(1, fraction3.getDenominator());

        // Test parsing zero fraction
        Fraction fraction4 = format.parse("0");
        assertEquals(0, fraction4.getNumerator());
        assertEquals(1, fraction4.getDenominator());
    }
}