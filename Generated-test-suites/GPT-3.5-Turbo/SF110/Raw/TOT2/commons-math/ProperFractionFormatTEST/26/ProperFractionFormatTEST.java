
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testProperFractionFormatConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testParse_NegativeNum() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "-3/4";
        Fraction result = properFractionFormat.parse(input, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
    }
    
    // Add more test cases here
    
}
