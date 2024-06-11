import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_normalDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, outputBuilder);
        } catch (Exception e) {
            fail("An exception should not have been thrown.");
        }
        assertEquals("A", outputBuilder.toString());
    }

    @Test
    public void testTranslate_hexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, outputBuilder);
        } catch (Exception e) {
            fail("An exception should not have been thrown.");
        }
        assertEquals("A", outputBuilder.toString());
    }

    @Test
    public void testTranslate_invalidEntityFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            unescaper.translate("&#", 0, outputBuilder);
        } catch (Exception e) {
            fail("An exception should not have been thrown.");
        }
        assertEquals("", outputBuilder.toString());
    }

    @Test
    public void testTranslate_invalidEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            unescaper.translate("&#abc;", 0, outputBuilder);
        } catch (Exception e) {
            fail("An exception should not have been thrown.");
        }
        assertEquals("", outputBuilder.toString());
    }

    @Test
    public void testTranslate_noEntityMatch() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            unescaper.translate("abc", 0, outputBuilder);
        } catch (Exception e) {
            fail("An exception should not have been thrown.");
        }
        assertEquals(0, outputBuilder.length());
    }

}