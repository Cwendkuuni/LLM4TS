import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_invalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(0, unescaper.translate("abc", 0, out)); // Check if no entity is found
    }

    @Test
    public void testTranslate_validNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(6, unescaper.translate("&#65;", 0, out)); // Check valid numeric entity translation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_missingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        unescaper.translate("&#65", 0, out); // Check if exception is thrown for missing semi-colon
    }

    @Test
    public void testTranslate_hexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate("&#x41;", 0, out)); // Check valid hex numeric entity translation
    }
}