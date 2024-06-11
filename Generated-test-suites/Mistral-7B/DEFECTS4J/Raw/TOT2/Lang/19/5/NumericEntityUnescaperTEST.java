import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import java.io.StringWriter;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        String input = "&amp;#160; &#x20AC; &#128572; &#12345; &#87654321;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "  € ♥ Ⅵ ⁵⁰⁰ⁱ";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_NullInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence cs = null;
        unescaper.translate(cs, 0, out);
    }

    @Test(expected = IOException.class)
    public void testTranslate_EmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence cs = "";
        unescaper.translate(cs, 0, out);
    }

    @Test(expected = IOException.class)
    public void testTranslate_NullWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence cs = "&amp;#160; &#x20AC; &#128572; &#12345; &#87654321;";
        Writer out = null;
        unescaper.translate(cs, 0, out);
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        String input = "&amp;#asdf;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        assertEquals(1, actual);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_ValidEntityWithSemicolon() throws IOException {
        String input = "&amp;#160; &#x20AC; &#128572; &#12345; &#87654321;&#xFFFF;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "  € ♥ Ⅵ ⁵⁰⁰ⁱ\uD800\uDFFF";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslate_ValidEntityWithoutSemicolon() throws IOException {
        String input = "&amp;#160; &#x20AC; &#128572; &#12345; &#87654321#xFFFF";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "  € ♥ Ⅵ ⁵⁰⁰ⁱ";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslate_OutOfBoundsCharacterCode() throws IOException {
        String input = "&amp;#65536;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslate_ValidCharacterCode() throws IOException {
        String input = "&amp;#48;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "0";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslate_HexadecimalEntity() throws IOException {
        String input = "&amp;#x41;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        String expected = "A";
        assertEquals(expected.length(), actual);
        assertEquals(expected, out.toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_InvalidHexadecimalSequence() throws IOException {
        String input = "&amp;#xabcd;";
        Writer out = new StringWriter();
        CharSequence cs = new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        };

        int actual = new NumericEntityUnescaper().translate(cs, 0, out);
        assertEquals(0, actual);
        assertEquals("", out.toString());
    }
}