
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormatWithWhole() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append(" ");

        Assert.assertEquals(expectedResult.toString(), properFractionFormat.format(fraction, stringBuffer, fieldPosition).toString());
    }
    
    @Test
    public void testFormatWithoutWhole() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("2 / 3");

        Assert.assertEquals(expectedResult.toString(), properFractionFormat.format(fraction, stringBuffer, fieldPosition).toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = properFractionFormat.getWholeFormat();

        Assert.assertNotNull(numberFormat);
    }
    
    @Test
    public void testParseValidFractionWithWhole() {
        String fractionString = "2 3 / 4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(11, 4);

        Assert.assertEquals(expectedResult, properFractionFormat.parse(fractionString, parsePosition));
    }
    
    @Test
    public void testParseValidFractionWithoutWhole() {
        String fractionString = "2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(2, 3);

        Assert.assertEquals(expectedResult, properFractionFormat.parse(fractionString, parsePosition));
    }
    
    @Test
    public void testParseInvalidFraction() {
        String fractionString = "2 / -3";
        ParsePosition parsePosition = new ParsePosition(0);

        Assert.assertNull(properFractionFormat.parse(fractionString, parsePosition));
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);

        Assert.assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
    
}
