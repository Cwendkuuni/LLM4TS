import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        assertEquals("1 / 2", properFormat.format(new Fraction(1, 2), buffer, position).toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        assertNotNull(properFormat.getWholeFormat());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getInstance();
        properFormat.setWholeFormat(format);
        assertEquals(format, properFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        ParsePosition position = new ParsePosition(0);
        assertNotNull(properFormat.parse("1 / 3", position));
    }

}