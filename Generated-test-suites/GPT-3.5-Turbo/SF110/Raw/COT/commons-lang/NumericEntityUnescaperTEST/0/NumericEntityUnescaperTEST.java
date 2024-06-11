// JUnit test cases for NumericEntityUnescaper class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateBasic() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int index = unescaper.translate("&#65;", 0, out);
        assertEquals(6, index);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int index = unescaper.translate("&#x41;", 0, out);
        assertEquals(7, index);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int index = unescaper.translate("&A;", 0, out);
        assertEquals(0, index);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateInvalidHexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate("&#xg;", 0, out);
        });
    }

    @Test
    public void testTranslateWithoutSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        int index = unescaper.translate("&#65", 0, out);
        assertEquals(6, index);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateSemiColonRequiredError() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> {
            unescaper.translate("&#65", 0, out);
        });
    }

}