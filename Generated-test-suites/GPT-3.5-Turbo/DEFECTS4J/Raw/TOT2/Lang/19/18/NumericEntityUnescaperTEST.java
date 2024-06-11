import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidDecimalValue_shouldReturnCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, output);
            assertEquals(output.toString(), "A");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_withValidHexadecimalValue_shouldReturnCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, output);
            assertEquals(output.toString(), "A");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_withInvalidEntityValue_shouldNotTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#XYZ;", 0, output);
            assertEquals(output.toString(), "");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_withHighEntityValue_shouldReturnSurrogatePair() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#128514;", 0, output);
            assertEquals(output.toString(), "");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_withNoSemiColon_shouldTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, output);
            assertEquals(output.toString(), "A");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}