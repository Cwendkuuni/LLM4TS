import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_invalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("", 0, new StringWriter(sb));
        } catch (IOException e) {
            fail("Exception occurred.");
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&&#65;", 0, new StringWriter(sb));
        } catch (IOException e) {
            fail("Exception occurred.");
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testTranslate_hexadecimalInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, new StringWriter(sb));
        } catch (IOException e) {
            fail("Exception occurred.");
        }
        assertEquals("A", sb.toString());
    }

    @Test
    public void testTranslate_noSemicolonInNonHexadecimalInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, new StringWriter(sb));
        } catch (IOException e) {
            fail("Exception occurred.");
        }
        assertEquals("A", sb.toString());
    }
}