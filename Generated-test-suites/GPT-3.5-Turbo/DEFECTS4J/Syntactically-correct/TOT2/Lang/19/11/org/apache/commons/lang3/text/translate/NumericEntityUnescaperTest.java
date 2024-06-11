import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_withDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, sb);
            assertEquals("A", sb.toString());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testTranslate_withHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, sb);
            assertEquals("A", sb.toString());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    // Add more test cases for edge cases and error scenarios as needed
    
}