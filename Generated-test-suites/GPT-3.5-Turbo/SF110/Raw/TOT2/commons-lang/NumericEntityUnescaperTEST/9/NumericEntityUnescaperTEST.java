import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertNotNull(numericEntityUnescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(numericEntityUnescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#65;", 0, new StringWriter(output));
            assertEquals(6, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateHexValue() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#x41;", 0, new StringWriter(output));
            assertEquals(7, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        numericEntityUnescaper.translate("&#65", 0, new StringWriter());
    }

    @Test
    public void testTranslateNumberFormatException() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#INVALID;", 0, new StringWriter(output));
            assertEquals(0, result);
            assertTrue(output.toString().isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}