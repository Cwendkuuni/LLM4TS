package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidNumericEntities() {
        CharSequence input = "&amp;&#x61;&#x6C;&#x65;&#x72;&#x65; &&#x2122; &#xA9; &#x2212; &#x2213; &#x2215; &#x2248; &#x2249; &#x2260; &#x2261;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                System.out.print((char) c);
            }
        };

        int index = 0;
        for (int length = input.length(); index < length; index++) {
            int actual = new NumericEntityUnescaper().translate(input, index, output);
            assertEquals("Expected 1 or more characters to be written, but only " + actual + " were.", length - index + actual, NumericEntityUnescaper.translate(input, index, output));
        }
        assertEquals("Expected output to be 'ampar&eacute; &trade; &copy; &ndash; &mdash; &divide; &times; &divideeq; &gt; &gt; &lt; &gt; &lt; &gt; &le; &ge; &sub; &sup'", "ampar&eacute; &trade; &copy; &ndash; &mdash; &divide; &times; &divideeq; &gt; &gt; &lt; &gt; &lt; &gt; &le; &ge; &sub; &sup", new String(new char[input.length()]).toCharArray());
    }

    @Test
    public void testTranslate_InvalidCharacterAtStart() {
        CharSequence input = "&";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 0, output));
    }

    @Test
    public void testTranslate_InvalidCharacterAtEnd() {
        CharSequence input = "&amp;#x123;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, input.length(), output));
    }

    @Test
    public void testTranslate_InvalidCharacterInMiddle() {
        CharSequence input = "&amp;&#x123a;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 6, output));
    }

    @Test
    public void testTranslate_InvalidHex() {
        CharSequence input = "&amp;&#x12z;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 0, output));
    }

    @Test
    public void testTranslate_InvalidDecimal() {
        CharSequence input = "&amp;&#xyz;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 0, output));
    }

    @Test
    public void testTranslate_InvalidHexRangeStart() {
        CharSequence input = "&amp;&#xff0;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 0, output));
    }

    @Test
    public void testTranslate_InvalidHexRangeEnd() {
        CharSequence input = "&amp;&#x10f00;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertThrows("Expected an IllegalArgumentException", IllegalArgumentException.class, () -> new NumericEntityUnescaper().translate(input, 0, output));
    }

    @Test
    public void testTranslate_EmptyEntity() {
        CharSequence input = "&amp;";
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                // not used in this test
            }
        };
        assertNull("Expected no characters to be written for an empty entity.", NumericEntityUnescaper.translate(input, 0, output));
    }
}