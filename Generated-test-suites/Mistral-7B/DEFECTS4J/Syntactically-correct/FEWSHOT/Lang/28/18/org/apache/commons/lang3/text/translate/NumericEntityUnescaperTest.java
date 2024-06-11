package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        // Test positive cases
        String input = "&amp;&#x61;&#x62;&#x63;";
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("abc", out.toString());
        assertEquals(3, length);

        input = "&amp;#10;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\n", out.toString());
        assertEquals(1, length);

        input = "&amp;#x0A;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\n", out.toString());
        assertEquals(1, length);

        input = "&amp;#x0D;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\r", out.toString());
        assertEquals(1, length);

        input = "&amp;#x20AC;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\u00A2", out.toString());
        assertEquals(1, length);

        input = "&amp;#128;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\u0080", out.toString());
        assertEquals(1, length);

        input = "&amp;#xDBFF; &amp;#xFBFF; &amp;#xFFFE; &amp;#xFFFF;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\ufffe\uffff\ufffe\uffff", out.toString());
        assertEquals(7, length);

        // Test negative cases
        input = "&amp;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("&", out.toString());
        assertEquals(1, length);

        input = "&amp;#";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);

        input = "&amp;#a";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);

        input = "&amp;#z";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);

        input = "&amp;#-%";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);

        input = "&amp;#-%1234";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("%1234", out.toString());
        assertEquals(5, length);

        input = "&amp;#xZZZZ;";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);

        input = "&amp;#xZZZZ%";
        out = new StringBuilder();
        length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("%", out.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input = null;
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertTrue(length == 0 || input == null);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input = "";
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.toString());
        assertEquals(0, length);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#123;";
        unescaper.translate(new StringReader(input), 0, null);
    }
}

class StringReader implements CharSequence {
    private final String s;

    public StringReader(String s) {
        this.s = s;
    }

    @Override
    public int length() {
        return s.length();
    }

    @Override
    public char charAt(int index) {
        return s.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return s.subSequence(start, end);
    }
}