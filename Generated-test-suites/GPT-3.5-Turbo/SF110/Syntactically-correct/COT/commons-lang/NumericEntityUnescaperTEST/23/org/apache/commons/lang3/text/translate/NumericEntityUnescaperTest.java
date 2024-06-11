import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_NoSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int index = 0;
        unescaper.translate(input, index, writer);
    }
    
    @Test
    public void testTranslate_Hexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_InvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#invalid;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}