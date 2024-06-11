package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        String input = "&amp; &lt; &gt; &apos; &quot; &#32; &#128; &#xFFFF; &#x1F602;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        int length = translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp; &lt; &gt; &apos; &quot; \u0020 \u0080 \uFFFF \uD835\uDE02", output.toString());

        input = "&amp; &lt; &gt; &apos; &quot; &#x32; &#x128; &#xFFFF; &#x1F602;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp; &lt; &gt; &apos; &quot; \u0020 \u0080 \uFFFF \uD835\uDE02", output.toString());

        input = "&amp; &lt; &gt; &apos; &quot; &#x32; &amp;#x128; &amp;#xFFFF; &amp;#x1F602;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp; &lt; &gt; &apos; &quot; \u0020 \u0080 \uFFFF \uD835\uDE02", output.toString());

        input = "&amp; &lt; &gt; &apos; &quot; &#x32; &amp;#x128;  &amp;#xFFFF; &amp;#x1F602;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp; &lt; &gt; &apos; &quot; \u0020 \u0080 \uFFFF \uD835\uDE02", output.toString());

        input = "&amp; &lt; &gt; &apos; &quot;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals(input, output.toString());

        input = "";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertNull(output.toString());

        input = "&";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertNull(output.toString());

        input = "&amp;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp;", output.toString());

        input = "&amp;&amp;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp;&amp;", output.toString());

        input = "&amp;&#x26;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp;&#x26;", output.toString());

        input = "&amp;&#xA0;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertEquals("&amp;\u00A0", output.toString());

        input = "&amp;&#xFFFF;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertArrayEquals(new char[]{'&', '\uFFFE'}, output.toString().toCharArray());

        input = "&amp;&#xFFFD;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertArrayEquals(new char[]{'&', '\uFFFE'}, output.toString().toCharArray());

        input = "&amp;#x1F602;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertArrayEquals(new char[]{'&', '\uD835', '\uDE02'}, output.toString().toCharArray());

        input = "&amp;#x1F603;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertNull(output.toString());

        input = "&amp;#x1F604;";
        output = new StringWriter();
        translator.translate(new StringReader(input), 0, output);
        assertEquals(input.length(), length);
        assertNull(output.toString());
    }
}

class StringReader implements CharSequence, Readable {
    private final String cs;
    private int pos;

    public StringReader(String cs) {
        this.cs = cs;
    }

    public int read(CharBuffer out) throws IOException {
        return read(out, cs.length());
    }

    public int read(CharBuffer out, int off, int length) throws IOException {
        if (off < 0 || length < 0 || off + length > cs.length()) {
            throw new IndexOutOfBoundsException();
        }

        int n = cs.length() - pos;
        int l = Math.min(n, length);
        out.put(cs.subSequence(pos, pos + l).toString());
        pos += l;
        return l;
    }

    public long position() throws IOException {
        return pos;
    }

    public int length() {
        return cs.length();
    }

    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end > cs.length() || start > end) {
            throw new IndexOutOfBoundsException();
        }
        return cs.subSequence(start, end);
    }

    public char charAt(int index) {
        if (index < 0 || index >= cs.length()) {
            throw new IndexOutOfBoundsException();
        }
        return cs.charAt(index);
    }

    public int length() {
        return cs.length();
    }
}