import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat(new SimpleDateFormat("yyyy-MM-dd", Locale.US));
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expectedResult = new StringBuffer();
        expected.append("3 / 4");

        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);

        Assert.assertEquals(expectedResult.toString(), result.toString());
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedResult = new Fraction(3, 4);

        Fraction result = properFractionFormat.parse(source, pos);

        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testParseWithInvalidFormat() {
        String source = "3/4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(source, pos);

        Assert.assertNull(result);
    }
    
    @Test
    public void testParseWithInvalidNumber() {
        String source = "3 / -4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(source, pos);

        Assert.assertNull(result);
    }
    
    @Test
    public void testParseWithEmptyDenominator() {
        String source = "3 /";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(source, pos);

        Assert.assertNull(result);
    }
    
    @Test
    public void testParseWithNoDenominator() {
        String source = "3 ";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(source, pos);

        Assert.assertEquals(new Fraction(3, 1), result);
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        properFractionFormat.setWholeFormat(format);

        Assert.assertEquals(format, properFractionFormat.getWholeFormat());
    }
}