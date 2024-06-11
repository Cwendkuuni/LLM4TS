package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * NumericEntityUnescaperTest class.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        // Test cases for decimal entity
        CharSequence input = "&#123;&#125;";
        Writer out = new StringWriter();
        int length = unescaper.translate(input, 0, out);
        assertEquals(6, length);
        assertEquals("{\\u007B\\u007D}", out.toString());

        input = "&amp;#123;&#125;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(6, length);
        assertEquals("{\\u007B\\u007D}", out.toString());

        input = "&amp;#x7B;&#x7D;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(6, length);
        assertEquals("{\\u007B\\u007D}", out.toString());

        input = "&amp;#123;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(3, length);
        assertEquals("{\\u007B}", out.toString());

        input = "&amp;#x7B;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(2, length);
        assertEquals("{\\u007B}", out.toString());

        // Test cases for hex entity with semicolon
        input = "&amp;#x{7B}7D;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(6, length);
        assertEquals("{\\u007B\\u007D}", out.toString());

        input = "&amp;#x{7B}7D";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for hex entity without semicolon
        input = "&amp;#x{7B}7D";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "&amp;#x{7B}";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for invalid decimal entity
        input = "&amp;#1234567890ABCDEF;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#1234567890ABCDEF";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#1234567890ABCDE";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for invalid hex entity
        input = "&amp;#xABCDE;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#xABCDE";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#xAB";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for invalid entity with semicolon
        input = "&amp;#1234567890ABCDEF;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#1234567890ABCDEF";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for invalid entity without semicolon
        input = "&amp;#1234567890ABCDEF";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "&amp;#1234567890ABCDE";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test cases for empty input
        input = "";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(0, length);
        assertEquals("", out.toString());

        input = "&amp;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(1, length);
        assertEquals("&amp;", out.toString());

        // Test cases for input with other entities
        input = "&amp;#123;&lt;foo&gt;&amp;#125;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(9, length);
        assertEquals("{\\u007B}&lt;foo&gt;{\\u007D}", out.toString());

        input = "&amp;#x7B;&lt;foo&gt;&#x7D;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(9, length);
        assertEquals("{\\u007B}&lt;foo&gt;{\\u007D}", out.toString());

        input = "&amp;#123;&lt;foo&gt;&amp;#125;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(9, length);
        assertEquals("{\\u007B}&lt;foo&gt;{\\u007D}", out.toString());

        input = "&amp;#x7B;&lt;foo&gt;&#x7D;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(9, length);
        assertEquals("{\\u007B}&lt;foo&gt;{\\u007D}", out.toString());

        // Test cases for input with other entities and invalid entities
        input = "&amp;#123;&lt;foo&gt;&amp;#xAB;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#123;&lt;foo&gt;&amp;#xABCDE";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#123;&lt;foo&gt;&amp;#1234567890ABCDE";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for input with other entities and invalid entities with semicolon
        input = "&amp;#123;&lt;foo&gt;&amp;#xAB;";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "&amp;#123;&lt;foo&gt;&amp;#xABCDE;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        input = "&amp;#123;&lt;foo&gt;&amp;#1234567890ABCDE;";
        out = new StringWriter();
        length = unescaper.translate(input, 0, out);
        assertEquals(-1, length);

        // Test cases for input with other entities and invalid entities without semicolon
        input = "&amp;#123;&lt;foo&gt;&amp;#xAB;";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "&amp;#123;&lt;foo&gt;&amp;#xABCDE";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "&amp;#123;&lt;foo&gt;&amp;#1234567890ABCDE";
        out = new StringWriter();
        try {
            length = unescaper.translate(input, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * StringWriter class used to capture the output.
     */
    private static class StringWriter extends Writer {
        private StringBuilder sb = new StringBuilder();

        @Override
        public void write(int c) throws IOException {
            sb.appendCodePoint(c);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
        }

        @Override
        public void write(String s, int off, int len) throws IOException {
            sb.append(s, off, len);
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}