
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslateWithDecimalNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        char[] input = ("&#65;").toCharArray();
        int result = unescaper.translate(input, 0, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslateWithHexNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        char[] input = ("&#x41;").toCharArray();
        int result = unescaper.translate(input, 0, out);
        assertEquals(7, result);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslateWithNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        Writer out = new StringWriter();
        char[] input = ("&#65").toCharArray();
        int result = unescaper.translate(input, 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();
        char[] input = ("&#65").toCharArray();
        unescaper.translate(input, 0, out);
    }
    
    @Test
    public void testTranslateWithInvalidNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        char[] input = ("&invalid;").toCharArray();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWithHighValueEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        char[] input = ("&#65536;").toCharArray();
        int result = unescaper.translate(input, 0, out);
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", out.toString());
    }
}
