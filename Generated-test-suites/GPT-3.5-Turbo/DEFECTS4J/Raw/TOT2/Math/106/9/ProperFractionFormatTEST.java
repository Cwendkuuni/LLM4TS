import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        String result = properFormat.format(new Fraction(3, 4), new StringBuffer(), null).toString();
        assertEquals("3 / 4", result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        Fraction result = properFormat.parse("2 1/3", new ParsePosition(0));
        assertEquals(new Fraction(7, 3), result);
    }

    @Test
    public void testWholeFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat(NumberFormat.getInstance());
        NumberFormat wholeFormat = properFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        NumberFormat newWholeFormat = NumberFormat.getInstance();
        properFormat.setWholeFormat(newWholeFormat);
        assertEquals(newWholeFormat, properFormat.getWholeFormat());
    }

}