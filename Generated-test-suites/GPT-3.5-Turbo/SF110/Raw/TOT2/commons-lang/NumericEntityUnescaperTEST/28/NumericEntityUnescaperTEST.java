import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateWithHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("A", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new StringWriter(output));
    }

    @Test
    public void testTranslateNumberFormatException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#invalid;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateHighValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65536;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("\uFFFD\uFFFD", output.toString()); // Unicode replacement character
    }
}