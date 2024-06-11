import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        // Test case 1: formatting a fraction with a non-zero whole number
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer expected = new StringBuffer();
        expected.append("2 1 / 2");
        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), result.toString());
        
        // Test case 2: formatting a fraction with a negative whole number
        fraction = new Fraction(-5, 2);
        expected.setLength(0);
        expected.append("-2 1 / 2");
        result = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testParse() {
        // Test case 1: parsing a valid fraction with a non-zero whole number
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(5, 2);
        Fraction result = format.parse(source, pos);
        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
        
        // Test case 2: parsing a valid fraction with a negative whole number
        source = "-2 1 / 2";
        pos = new ParsePosition(0);
        expected = new Fraction(-5, 2);
        result = format.parse(source, pos);
        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    // More test cases for other public methods...
    
}