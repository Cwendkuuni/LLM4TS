import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.writer.StringWriter;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateBasic() {
        CharSequence input = "&apos; test &apos;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals("&apos; test '&'".getBytes().length, length);
        assertEquals("&apos; test '&apos;".getBytes()[0], out.toString().charAt(0));
        assertEquals("&apos; test '&apos;'.", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidInput() {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(null, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequence input = "&apos; test &apos;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(input, 0, null);
    }

    @Test
    public void testTranslateInvalidSequenceStart() {
        CharSequence input = "& test &apos;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("& test &apos;", out.toString());
    }

    @Test
    public void testTranslateInvalidSequenceEnd() {
        CharSequence input = "&apos; test &";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("&apos; test &", out.toString());
    }

    @Test
    public void testTranslateLargeValue() {
        CharSequence input = "&#[10FFFF]";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals(4, length);
        assertEquals("\uD800\uDBFF\uD800\uDFFF", out.toString());
    }

    @Test
    public void testTranslateNoSemicolon() {
        CharSequence input = "&apos; test &apos;&#1234";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals(5, length);
        assertEquals("&apos; test &apos;&#1234", out.toString());
    }

    @Test
    public void testTranslateHex() {
        CharSequence input = "&#x1F60A";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals(1, length);
        assertEquals("\uD83D\uDE0A", out.toString());
    }

    @Test
    public void testTranslateOctal() {
        CharSequence input = "&#0123";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals(1, length);
        assertEquals('\u007B', out.toString().charAt(0));
    }

    @Test
    public void testTranslateEmptyString() {
        Writer out = new StringWriter();
        CharSequence input = "";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        assertEquals(0, length);
        assertEquals("", out.toString());
    }
}