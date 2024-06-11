import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int translated = unescaper.translate("&#65;", 0, result);
        assertEquals(5, translated);
        assertEquals("A", result.toString());
    }

    @Test
    public void testTranslate_hexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int translated = unescaper.translate("&#x48;", 0, result);
        assertEquals(6, translated);
        assertEquals("H", result.toString());
    }

    @Test
    public void testTranslate_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int translated = unescaper.translate("&#88", 0, result);
        assertEquals(3, translated);
        assertEquals("X", result.toString());
    }

    @Test
    public void testTranslate_invalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int translated = unescaper.translate("&#xG;", 0, result);
        assertEquals(0, translated);
        assertEquals("", result.toString());
    }

    @Test
    public void testTranslate_largeCharacterCode() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int translated = unescaper.translate("&#65536;", 0, result);
        assertEquals(7, translated);
        assertEquals("\uD800\uDC00", result.toString());
    }

}