import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private static ProperFractionFormat format;

    @BeforeClass
    public static void setUp() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        assertEquals("1 / 2", format.format(1, 2));
        assertEquals("3 / 4", format.format(3, 4));
        assertEquals("5 / 6", format.format(5, 6));
    }

    @Test
    public void testGetWholeFormat() {
        assertNotNull(format.getWholeFormat());
    }

    @Test
    public void testParse() {
        assertEquals(format.parse("1 / 2"), new Fraction(1, 2));
        assertEquals(format.parse("3 / 4"), new Fraction(3, 4));
        assertEquals(format.parse("5 / 6"), new Fraction(5, 6));
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat newFormat = NumberFormat.getInstance();
        format.setWholeFormat(newFormat);
        assertEquals(newFormat, format.getWholeFormat());
    }

}