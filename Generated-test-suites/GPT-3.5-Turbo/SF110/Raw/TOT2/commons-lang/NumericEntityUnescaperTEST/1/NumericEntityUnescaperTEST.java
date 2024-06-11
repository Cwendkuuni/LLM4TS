import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, new StringWriter(output));
        assertEquals(5, output.length());
        assertEquals(6, result);
    }

    @Test
    public void testTranslateWithHexadecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x41;", 0, new StringWriter(output));
        assertEquals(1, output.length());
        assertEquals(6, result);
    }

    @Test
    public void testTranslateWithInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#invalid;", 0, new StringWriter(output));
        assertEquals(0, output.length());
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        unescaper.translate("&#65", 0, new StringWriter());
    }

    @Test
    public void testTranslateWithOutOfRangeValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65536;", 0, new StringWriter(output));
        assertEquals(1, output.length());
        assertEquals(7, result);
    }
}