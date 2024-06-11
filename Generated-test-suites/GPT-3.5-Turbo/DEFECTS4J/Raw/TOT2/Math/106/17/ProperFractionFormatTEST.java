import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Assert;
import org.junit.Test;

import java.text.FieldPosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 3);
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buf, pos);
        Assert.assertEquals("1 2/3", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertNull(result);
    }

    @Test
    public void testParseInvalidExpression() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertNull(result);
    }
    
    // Additional test cases for edge cases and exception handling scenarios should be added here
    
}