import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertNotNull(unescaper);
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#65;", 0, new StringWriter(output));
            assertEquals(4, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}