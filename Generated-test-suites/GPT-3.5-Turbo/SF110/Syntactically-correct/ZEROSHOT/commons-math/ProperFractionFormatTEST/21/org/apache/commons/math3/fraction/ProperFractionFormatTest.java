import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("2 / 3", format.format(fraction));

        fraction = new Fraction(5, 4);
        assertEquals("1 1 / 4", format.format(fraction));

        fraction = new Fraction(-3, 2);
        assertEquals("-1 1 / 2", format.format(fraction));

        fraction = new Fraction(0, 7);
        assertEquals("0 / 1", format.format(fraction));
    }

    @Test
    public void testParse() throws ParseException {
        Fraction fraction = format.parse("2 / 3");
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        fraction = format.parse("1 1 / 4");
        assertEquals(5, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());

        fraction = format.parse("-1 1 / 2");
        assertEquals(-3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        fraction = format.parse("0 / 1");
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

}