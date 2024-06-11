import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {
  
    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(4, result);
        assertEquals("A", out.toString());
    }
  
    @Test
    public void testTranslate_withValidHexInput_shouldReturnTranslatedCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x41;";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }
  
    @Test
    public void testTranslate_withMissingSemicolon_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
  
    @Test
    public void testTranslate_withInvalidInput_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#invalid;";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
  
    @Test
    public void testTranslate_withValidInputGreaterThanFFFF_shouldReturnTranslatedCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65536;";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(8, result);
        assertEquals("\uD800\uDC00", out.toString());
    }
  
    @Test
    public void testTranslate_withValidHexInputGreaterThanFFFF_shouldReturnTranslatedCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x10000;";
        Writer out = new StringWriter();
        
        int result = unescaper.translate(input, 0, out);
        
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", out.toString());
    }
}