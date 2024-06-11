import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        assertEquals("-7 / 2", properFractionFormat.format(new ProperFraction(-7, 2)));
        assertEquals("0 / 1", properFractionFormat.format(new ProperFraction(0, 1)));
        assertEquals("3 / 4", properFractionFormat.format(new ProperFraction(3, 4)));
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);

        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        assertEquals(new ProperFraction(-7, 2), properFractionFormat.parse("-7 / 2", pos));
        assertEquals(new ProperFraction(0, 1), properFractionFormat.parse("0 / 1", pos));
        assertEquals(new ProperFraction(3, 4), properFractionFormat.parse("3 / 4", pos));
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(wholeFormat);

        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
}