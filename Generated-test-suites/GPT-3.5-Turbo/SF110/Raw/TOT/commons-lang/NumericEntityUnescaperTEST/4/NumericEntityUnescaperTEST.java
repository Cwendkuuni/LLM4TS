import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Typical use case
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());

        // Edge case - index at the start of input
        writer = new StringWriter();
        unescaper.translate("&#97;", 0, writer);
        assertEquals("a", writer.toString());

        // Edge case - index at the end of input
        writer = new StringWriter();
        unescaper.translate("This is a test &#115", 17, writer);
        assertEquals("", writer.toString());

        // Error scenario - invalid input starting with '&'
        writer = new StringWriter();
        unescaper.translate("&notvalid", 0, writer);
        assertEquals("", writer.toString());

        // Error scenario - invalid input with missing semi-colon
        writer = new StringWriter();
        try {
            unescaper.translate("&#123", 0, writer);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.semiColonOptional);

        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }
}