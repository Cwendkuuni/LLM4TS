
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    private final CharSequenceTranslator translator = new NumericEntityUnescaper();

    private void assertEqualsIgnoreWhitespaces(String expected, String actual) {
        expected = expected.replaceAll("\\s", "");
        actual = actual.replaceAll("\\s", "");
        assertEquals(expected, actual);
    }

    private void assertTranslation(String expected, CharSequence input) throws IOException {
        Writer writer = new CharArrayWriter();
        int consumed = translator.translate(input, 0, writer);
        assertEquals(input.length(), consumed);
        assertEqualsIgnoreWhitespaces(expected, writer.toString());
    }

    @Test
    public void testTranslate() throws IOException {
        // Test case: &#[xX]?\d+;?
        assertTranslation("A", "&#65;");
        assertTranslation("A", "&#x41;");
        assertTranslation("\n", "&#10;");
        assertTranslation("\n", "&#x0A;");
        assertTranslation("\n", "&#00010;");
        assertTranslation("\n", "&#000010;");
        assertTranslation("\n", "&#0000010;");
        assertTranslation("\n", "&#x000A;");
        assertTranslation("\n", "&#x0000A;");
        assertTranslation("\n", "&#x0000A;");
        assertTranslation("\n", "&#x00000A;");
        assertTranslation("\n", "&#x000000A;");
        assertTranslation("newline", "&#110;&#101;&#119;&#108;&#105;&#110;&#101;");
        assertTranslation("newline", "&#x6E;&#x65;&#x77;&#x6C;&#x69;&#x6E;&#x65;");
        assertTranslation("tab\t", "tab&#x0009;");
        assertTranslation("tab\t", "tab&#9;");
        assertTranslation("tab\t", "tab&#x9;");
        assertTranslation("tab\t", "tab&#0009;");
        assertTranslation("\u221a", "&radic;");
        assertTranslation("R", "&#82m;");
    }

    @Test
    public void testTranslateInvalid() throws IOException {
        Writer writer = new CharArrayWriter();
        int consumed = translator.translate("&#", 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());

        writer = new CharArrayWriter();
        consumed = translator.translate("&#;", 0, writer);
        assertEquals(2, consumed);
        assertEquals(";&#", writer.toString());

        writer = new CharArrayWriter();
        consumed = translator.translate("&#x;", 0, writer);
        assertEquals(3, consumed);
        assertEquals("&#x;", writer.toString());

        writer = new CharArrayWriter();
        consumed = translator.translate("&#A;", 0, writer);
        assertEquals(3, consumed);
        assertEquals("&#A;", writer.toString());

        writer = new CharArrayWriter();
        consumed = translator.translate("&;", 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());

        writer = new CharArrayWriter();
        consumed = translator.translate("", 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }
}
