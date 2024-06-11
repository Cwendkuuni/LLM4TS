import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import java.text.NumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        String expected = "3 / 4";
        
        assertEquals(expected, format.format(fraction, buffer, position).toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "5 / 2";
        ParsePosition position = new ParsePosition(0);
        Fraction expected = new Fraction(2, 1);
        
        assertEquals(expected, format.parse(input, position));
    }
    
    @Test
    public void testWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        format.setWholeFormat(numberFormat);
        
        assertEquals(numberFormat, format.getWholeFormat());
    }
    
    @Test
    public void testConvenienceConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        
        assertEquals(wholeFormat, format.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        format.setWholeFormat(null);
    }
}