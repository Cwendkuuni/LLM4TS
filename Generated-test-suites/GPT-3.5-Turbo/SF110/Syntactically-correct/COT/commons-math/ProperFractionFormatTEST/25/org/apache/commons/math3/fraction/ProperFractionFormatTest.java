import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        // Additional assertions specific to the default constructor
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        assertNotNull(fractionFormat);
        // Additional assertions specific to the constructor with format
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, 
                                                                      denominatorFormat);
        assertNotNull(fractionFormat);
        // Additional assertions specific to the constructor with formats
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer output = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, output, pos);
        assertEquals("3 / 4", result.toString());
        // Additional assertions specific to the format method
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
        // Additional assertions specific to the getWholeFormat method
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNotNull(fraction);
        // Additional assertions specific to the parse method
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        // Additional assertions specific to the setWholeFormat method
    }

}