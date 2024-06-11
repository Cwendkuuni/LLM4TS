import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_validInputWithSemiColon_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(4, result);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslate_validInputWithoutSemiColon_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_validInputWithoutSemiColon_ErrorIfNoSemiColonOption_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        int index = 0;
        
        try {
            translator.translate(input, index, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
            assertEquals("", out.toString());
        }
    }
    
    @Test
    public void testTranslate_validInputWithHexValue_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#x41;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslate_validInputWithInvalidHexValue_InvalidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#xZZ;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_validInputWithLargeEntityValue_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#123456;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(9, result);
        assertEquals("𝄠", out.toString());
    }
    
    @Test
    public void testTranslate_validInputWithInvalidEntityValue_ValidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#999999;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(7, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_invalidInput_InvalidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "ABC";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = translator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_invalidInput_IndexOutOfRange_InvalidOutput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        int index = 4; // Out of range index
        
        int result = translator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}