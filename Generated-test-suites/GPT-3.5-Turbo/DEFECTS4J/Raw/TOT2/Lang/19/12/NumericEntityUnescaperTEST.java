import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate() {
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#65;", 0, output);
            assertEquals(4, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateWithHexValue() {
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#x65;", 0, output);
            assertEquals(5, result);
            assertEquals("e", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateWithoutSemicolon() {
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#65", 0, output);
            assertEquals(4, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateInvalidInput() {
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("invalid", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}