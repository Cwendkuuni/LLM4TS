import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test basic functionality
        assertEquals(4, unescaper.translate("&#65;", 0, new StringWriter()));
        
        // Test empty input
        assertEquals(0, unescaper.translate("", 0, new StringWriter()));
        
        // Test invalid input format
        assertEquals(0, unescaper.translate("&ABC;", 0, new StringWriter()));
        
        // Test hex input
        assertEquals(6, unescaper.translate("&#x1F603;", 0, new StringWriter()));
        
        // Test invalid hex input
        assertEquals(0, unescaper.translate("&#x1FG;", 0, new StringWriter()));
    }
}