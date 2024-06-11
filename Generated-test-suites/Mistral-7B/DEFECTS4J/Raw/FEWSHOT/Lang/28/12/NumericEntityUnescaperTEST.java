package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;123;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("123", writer.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslateWithHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;#x1f600;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("\uD83C\uDF00", writer.toString());
        assertEquals(5, length);
    }

    @Test
    public void testTranslateWithInvalidDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;#abcd;";
        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringReader(input), 0, writer);
        });
    }

    @Test
    public void testTranslateWithInvalidHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;#xzab;";
        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringReader(input), 0, writer);
        });
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateWithNoEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "some text &amp; some more text";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("some text & some more text", writer.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateWithMalformedEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringBuilder();

        String input = "&amp;#x1f600a;";
        assertThrows(IOException.class, () -> {
            unescaper.translate(new StringReader(input), 0, writer);
        });
    }
}

class StringReader implements CharSequence, Reader {

    private final String cs;

    public StringReader(String cs) {
        this.cs = cs;
    }

    @Override
    public int length() {
        return cs.length();
    }

    @Override
    public char charAt(int index) {
        return cs.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return cs.subSequence(start, end);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        System.arraycopy(cs.substring(off, off + len).toCharArray(), 0, cbuf, 0, len);
        return len;
    }

    @Override
    public void close() throws IOException {
    }
}