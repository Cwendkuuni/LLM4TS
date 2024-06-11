import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithNoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper);
    }

    @Test
    public void testIsSetWhenOptionIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testIsSetWhenOptionIsNotSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslateWithHexEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int result = unescaper.translate("&#x61;", 0, out);
        assertEquals(6, result);
        assertEquals("a", out.toString());
    }

    @Test
    public void testTranslateWithDecimalEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int result = unescaper.translate("&#97;", 0, out);
        assertEquals(5, result);
        assertEquals("a", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        unescaper.translate("&#97", 0, out);
    }

    @Test
    public void testTranslateWithNonNumericEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int result = unescaper.translate("&#apple;", 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslateWithLargeDecimalEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int result = unescaper.translate("&#100000;", 0, out);
        assertEquals(9, result);
        assertEquals("𐄀", out.toString());
    }

}