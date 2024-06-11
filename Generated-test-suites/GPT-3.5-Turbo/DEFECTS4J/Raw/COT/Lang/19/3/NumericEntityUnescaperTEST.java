import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basic() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());

        translator.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_noSemicolon() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#65", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_multipleChars() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#66;&#67;", 0, writer);
        assertEquals("BC", writer.toString());
    }

    @Test
    public void testTranslate_hexadecimal() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_invalidNumberFormat() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#invalid;", 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_highCodePoint() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        translator.translate("&#65536;", 0, writer);
        assertEquals("\uD800\uDC00", writer.toString());
    }

}