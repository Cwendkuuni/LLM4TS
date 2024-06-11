
import org.apache.commons.lang3.text.translate.*;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NoTranslation() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter writer = new StringWriter();
        int count = unescaper.translate("This is a test", 0, writer);

        assertEquals(0, count);
        assertEquals("This is a test", writer.toString());
    }

    @Test
    public void testTranslate_NormalEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter writer = new StringWriter();
        int count = unescaper.translate("&#65;", 0, writer);

        assertEquals(6, count);
        assertEquals("A", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NoSemicolon_ThrowException() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
    }

    @Test
    public void testTranslate_HexEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter writer = new StringWriter();
        int count = unescaper.translate("&#x41;", 0, writer);

        assertEquals(8, count);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_LargeEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter writer = new StringWriter();
        int count = unescaper.translate("&#65536;", 0, writer);

        assertEquals(14, count);
        assertEquals("\uD800\uDC00", writer.toString());
    }
}
