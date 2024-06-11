import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int charsTranslated = unescaper.translate("&#65;", 0, output);
            assertEquals("A", output.toString());
            assertEquals(5, charsTranslated);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_validHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int charsTranslated = unescaper.translate("&#x41;", 0, output);
            assertEquals("A", output.toString());
            assertEquals(6, charsTranslated);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_invalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int charsTranslated = unescaper.translate("&#", 0, output);
            assertEquals(0, charsTranslated);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_highValueEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int charsTranslated = unescaper.translate("&#128514;", 0, output);
            assertEquals("😂", output.toString());
            assertEquals(9, charsTranslated);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}