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
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, new java.io.StringWriter(output));
        } catch (Exception e) {
            fail("Exception thrown during translation");
        }
        assertEquals("A", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNoSemiColonException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new java.io.StringWriter(output));
    }

    @Test
    public void testTranslateWithInvalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#INVALID;", 0, new java.io.StringWriter(output));
        assertEquals(0, result);
    }

    @Test
    public void testTranslateWithHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#x41;", 0, new java.io.StringWriter(output));
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateWithInvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#xINVALID;", 0, new java.io.StringWriter(output));
        assertEquals(0, result);
    }
}