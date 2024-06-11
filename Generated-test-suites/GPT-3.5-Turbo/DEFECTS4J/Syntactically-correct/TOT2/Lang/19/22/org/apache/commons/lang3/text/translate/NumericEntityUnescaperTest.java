import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate("&#65;", 0, output);
        
        assertEquals(5, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateWithHexadecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate("&#x41;", 0, output);
        
        assertEquals(6, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate("&#invalid;", 0, output);
        
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateWithHighValueNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate("&#65536;", 0, output);
        
        assertEquals(6, result);
        assertEquals("\uD800\uDC00", output.toString());
    }

    @Test
    public void testTranslateWithNoSemicolonNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate("&#65", 0, output);
        
        assertEquals(4, result);
        assertEquals("A", output.toString());
    }
}