
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexadecimalInput_returnsExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidDecimalInput_returnsExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidDecimalInputAndNoSemicolon_returnsExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withMissingSemicolon_throwsException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_withInvalidHexadecimalInput_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#xG;", 0, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_withInvalidDecimalInput_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#10A;", 0, writer);
    }
}
