import org.junit.*;
import org.apache.commons.math3.fraction.*;
import java.text.*;
import java.text.ParseException;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor(){
        ProperFractionFormat format = new ProperFractionFormat();
        Assert.assertNotNull(format);
    }

    @Test
    public void testConstructorWithNumberFormat(){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat);
        Assert.assertNotNull(format);
    }

    @Test
    public void testConstructorWithWholeFormat(){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
        Assert.assertNotNull(format);
    }

    @Test
    public void testFormat(){
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer stringBuffer = new StringBuffer();
        format.format(fraction, stringBuffer, new FieldPosition(0));
        String result = stringBuffer.toString();
        Assert.assertEquals("3 / 4", result);
    }

    @Test
    public void testGetWholeFormat(){
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        Assert.assertNotNull(wholeFormat);
    }

    @Test
    public void testParseValidFraction(){
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 3 / 4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = format.parse(source, parsePosition);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testParseInvalidFraction(){
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 3 /";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            format.parse(source, parsePosition);
            Assert.fail("Expected ParseException to be thrown");
        } catch (ParseException e){
            // Expected exception
        }
    }

    @Test
    public void testSetWholeFormat(){
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        format.setWholeFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, format.getWholeFormat());
    }
}