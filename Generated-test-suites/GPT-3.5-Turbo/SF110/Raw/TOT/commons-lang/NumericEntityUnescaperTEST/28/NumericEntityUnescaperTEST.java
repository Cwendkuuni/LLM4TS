import org.junit.Test;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
  
    @Test
    public void translate_withValidNumericEntity_shouldReturnTranslatedOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        String input = "&amp;&#x41;&#65;";

        int result = unescaper.translate(input, 0, writer);

        assertEquals(12, result);
        assertEquals("&AA", writer.toString());
    }
    
    @Test
    public void translate_withInvalidNumericEntity_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        String input = "&amp;&#999999;";

        int result = unescaper.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void translate_withMissingSemicolonForRequiredOption_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        
        String input = "&amp;&#65";

        int result = unescaper.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void translate_withMissingSemicolonForErrorOption_shouldThrowIllegalArgumentException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        
        String input = "&amp;&#65";

        unescaper.translate(input, 0, writer);
    }
    
    @Test
    public void translate_withValidHexadecimalNumericEntity_shouldReturnTranslatedOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        String input = "&#x41;";

        int result = unescaper.translate(input, 0, writer);

        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void translate_withInvalidHexadecimalNumericEntity_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        String input = "&#xGG;";

        int result = unescaper.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}