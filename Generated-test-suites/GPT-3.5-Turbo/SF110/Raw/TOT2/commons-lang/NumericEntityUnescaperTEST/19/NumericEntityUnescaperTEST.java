import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&amp;&#x41;&#65;", 0, out);
        assertEquals(13, result);
        assertEquals("&AE", out.toString());
    }
    
    @Test
    public void testTranslateWithNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        try {
            unescaper.translate("&amp#x41", 0, out);
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithInvalidInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&amp;&#xG;", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
}