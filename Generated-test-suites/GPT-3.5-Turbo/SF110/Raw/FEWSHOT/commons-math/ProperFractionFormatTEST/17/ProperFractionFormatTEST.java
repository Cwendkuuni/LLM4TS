import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(3, 4);
        StringBuffer buffer1 = new StringBuffer();
        format.format(fraction1, buffer1, null);
        assertEquals("0 3 / 4", buffer1.toString());

        Fraction fraction2 = new Fraction(5, 2);
        StringBuffer buffer2 = new StringBuffer();
        format.format(fraction2, buffer2, null);
        assertEquals("2 1 / 2", buffer2.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition position = new ParsePosition(0);

        Fraction fraction1 = format.parse("0 3 / 4", position);
        assertEquals(new Fraction(3, 4), fraction1);

        Fraction fraction2 = format.parse("2 1 / 2", position);
        assertEquals(new Fraction(5, 2), fraction2);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}