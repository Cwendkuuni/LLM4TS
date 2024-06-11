import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper();
        assertNotNull(entityUnescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper();
        assertFalse(entityUnescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslateBasic() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = entityUnescaper.translate("&#65;", 0, new StringWriter(output));
        assertEquals(5, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateHexadecimal() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = entityUnescaper.translate("&#x41;", 0, new StringWriter(output));
        assertEquals(6, result);
        assertEquals("A", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        entityUnescaper.translate("&#65", 0, new StringWriter(output));
    }

    @Test
    public void testTranslateInvalidNumber() {
        NumericEntityUnescaper entityUnescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = entityUnescaper.translate("&#invalid;", 0, new StringWriter(output));
        assertEquals(0, result);
    }

}