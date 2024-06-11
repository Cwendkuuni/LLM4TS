import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_decCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#65;", 0, output);
        assertEquals(5, result);
        assertEquals("A", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#97;", 0, output);
        assertEquals(5, result);
        assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_hexCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, output);
        assertEquals(7, result);
        assertEquals("A", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#x61;", 0, output);
        assertEquals(7, result);
        assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_optionalSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#65", 0, output);
        assertEquals(4, result);
        assertEquals("A", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#97", 0, output);
        assertEquals(4, result);
        assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_entityValueGreaterThanFFFF() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, output);
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#x10000;", 0, output);
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", output.toString());
    }

    @Test
    public void testTranslate_invalidNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#A;", 0, output);
        assertEquals(4, result);
        assertEquals("", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#10A;", 0, output);
        assertEquals(5, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_invalidHexNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#xA;", 0, output);
        assertEquals(4, result);
        assertEquals("", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#x1G;", 0, output);
        assertEquals(5, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_noStringValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#;", 0, output);
        assertEquals(3, result);
        assertEquals("", output.toString());

        output = new StringWriter();
        result = unescaper.translate("&#x;", 0, output);
        assertEquals(4, result);
        assertEquals("", output.toString());
    }

}