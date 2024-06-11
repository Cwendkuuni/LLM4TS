
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        
        String result1 = formatter.format(new Fraction(1, 2));
        String expected1 = "1/2";
        assertEquals(expected1, result1);
        
        String result2 = formatter.format(new Fraction(3, 2));
        String expected2 = "1 1/2";
        assertEquals(expected2, result2);
        
        String result3 = formatter.format(new Fraction(-3, 2));
        String expected3 = "-1 1/2";
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        
        Fraction result1 = formatter.parse("1/2");
        Fraction expected1 = new Fraction(1, 2);
        assertEquals(expected1, result1);
        
        Fraction result2 = formatter.parse("1 1/2");
        Fraction expected2 = new Fraction(3, 2);
        assertEquals(expected2, result2);
        
        Fraction result3 = formatter.parse("-1 1/2");
        Fraction expected3 = new Fraction(-3, 2);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        
        Fraction result = formatter.parse("1/2/3");
        assertNull(result);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getInstance();
        formatter.setWholeFormat(format);
        
        NumberFormat result = formatter.getWholeFormat();
        assertEquals(format, result);
    }
}
