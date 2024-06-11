import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_normalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(4, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_hexadecimalCode() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x61;", 0, writer);
        assertEquals(5, result);
        assertEquals('a', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_invalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#1234", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_codepointAboveFFFF() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#100000;", 0, writer);
        assertEquals(11, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_optionalSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#97", 0, writer);
        assertEquals(4, result);
        assertEquals('a', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_noMatch() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("abc", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}