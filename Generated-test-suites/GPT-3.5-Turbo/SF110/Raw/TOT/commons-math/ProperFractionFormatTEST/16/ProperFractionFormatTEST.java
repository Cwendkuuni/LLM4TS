import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_TypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expected = new StringBuffer("3 / 4");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 1);
        StringBuffer expected = new StringBuffer("4");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_NegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-2, 7);
        StringBuffer expected = new StringBuffer("-2 / 7");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_ZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 0);
        StringBuffer expected = new StringBuffer("Infinity");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_PositiveInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(10, 0);
        StringBuffer expected = new StringBuffer("Infinity");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_NegativeInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-20, 0);
        StringBuffer expected = new StringBuffer("-Infinity");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testFormat_NotANumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 0);
        StringBuffer expected = new StringBuffer("NaN");
        
        assertEquals(expected.toString(), format.format(fraction, new StringBuffer(), null).toString());
    }
    
    @Test
    public void testParse_TypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 / 5";
        Fraction expected = new Fraction(2, 5);
        
        assertEquals(expected, format.parse(source));
    }
    
    @Test
    public void testParse_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "4";
        Fraction expected = new Fraction(4, 1);
        
        assertEquals(expected, format.parse(source));
    }
    
    @Test
    public void testParse_NegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 / 8";
        Fraction expected = new Fraction(-3, 8);
        
        assertEquals(expected, format.parse(source));
    }
    
    @Test
    public void testParse_InvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 ^ 1";
        Fraction expected = null;
        
        assertEquals(expected, format.parse(source));
    }
    
    @Test
    public void testParse_InvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-2 / 0";
        Fraction expected = null;
        
        assertEquals(expected, format.parse(source));
    }
    
}