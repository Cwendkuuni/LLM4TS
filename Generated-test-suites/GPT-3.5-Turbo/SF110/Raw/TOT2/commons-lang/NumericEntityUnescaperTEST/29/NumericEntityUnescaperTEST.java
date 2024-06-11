import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertNotNull(unescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        int result = unescaper.translate("&#65;", 0, new StringWriter());
        assertEquals(6, result);
    }

    @Test
    public void testTranslateWithInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        int result = unescaper.translate("&invalidEntity;", 0, new StringWriter());
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        unescaper.translate("&#65", 0, new StringWriter());
    }

    @Test
    public void testTranslateWithHexadecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        int result = unescaper.translate("&#x41;", 0, new StringWriter());
        assertEquals(7, result);
    }

    @Test
    public void testTranslateWithOutOfRangeValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        int result = unescaper.translate("&#65536;", 0, new StringWriter());
        assertEquals(0, result);
    }
}