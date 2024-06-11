import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_Success() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_Success_Hex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_Error_NoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        try {
            unescaper.translate(input, index, writer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
            assertEquals("", writer.toString());
        }
    }

    @Test
    public void testTranslate_Error_InvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#xyz;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_Success_GreaterThan65535() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65536;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(8, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_Success_SemiColonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(3, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_Success_SemiColonNotPresent() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(3, result);
        assertEquals("A", writer.toString());
    }
}