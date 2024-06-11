import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.text.*;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        // Test case 1
        Fraction fraction1 = new Fraction(3, 4);
        StringBuffer buffer1 = new StringBuffer();
        FieldPosition pos1 = new FieldPosition(0);
        assertEquals("3 / 4", format.format(fraction1, buffer1, pos1).toString());

        // Test case 2
        Fraction fraction2 = new Fraction(-2, 5);
        StringBuffer buffer2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0);
        assertEquals("-2 / 5", format.format(fraction2, buffer2, pos2).toString());

        // Test case 3
        Fraction fraction3 = new Fraction(5, 1);
        StringBuffer buffer3 = new StringBuffer();
        FieldPosition pos3 = new FieldPosition(0);
        assertEquals("5", format.format(fraction3, buffer3, pos3).toString());
    }

    @Test
    public void testParse() {
        // Test case 1
        String source1 = "3 / 4";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction fraction1 = format.parse(source1, pos1);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(4, fraction1.getDenominator());

        // Test case 2
        String source2 = "-2 / 5";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction fraction2 = format.parse(source2, pos2);
        assertEquals(-2, fraction2.getNumerator());
        assertEquals(5, fraction2.getDenominator());

        // Test case 3
        String source3 = "5";
        ParsePosition pos3 = new ParsePosition(0);
        Fraction fraction3 = format.parse(source3, pos3);
        assertEquals(5, fraction3.getNumerator());
        assertEquals(1, fraction3.getDenominator());
    }
}