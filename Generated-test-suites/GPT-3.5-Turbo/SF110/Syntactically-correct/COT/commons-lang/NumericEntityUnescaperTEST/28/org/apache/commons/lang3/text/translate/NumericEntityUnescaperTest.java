
import org.junit.Test;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int length = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, length);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_semiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        int length = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, length);
        assertEquals("A", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_errorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
    }
    
    @Test
    public void testTranslate_highValueEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int length = unescaper.translate("&#65536;", 0, writer);
        assertEquals(9, length);
        assertEquals("\uD800\uDC00", writer.toString());
    }
    
    @Test
    public void testTranslate_hexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int length = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, length);
        assertEquals("A", writer.toString());
    }
}
