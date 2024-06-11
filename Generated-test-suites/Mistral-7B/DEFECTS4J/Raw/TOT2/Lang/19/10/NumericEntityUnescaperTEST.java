
package org.junit.tests;

import org.apache.commons.lang3.text.translate.*;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160; &#x20AC; &#65279; &#xFFFF; &#x1F600;";
        StringWriter writer = new StringWriter();
        unescaper.translate(new StringReader(input), 0, writer);
        String expectedOutput = "  € ␠ Å 🆒";
        assertEquals(expectedOutput, writer.toString());
    }

    // Test edge cases and exceptions
    @Test
    public void testInvalidEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&abc; &%#; &foo;";
        unescaper.translate(new StringReader(input), 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "";
        unescaper.translate(new StringReader(input), 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testNoEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "abcdefg";
        unescaper.translate(new StringReader(input), 0, writer);
        assertEquals("abcdefg", writer.toString());
    }

    @Test
    public void testAlreadyTranslatedEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&nbsp; &nbsp;";
        unescaper.translate(new StringReader(input), 0, writer);
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testInvalidValues() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&-1; &65536; &-10000; &100000;";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new StringReader(input), 0, writer));
    }

    @Test
    public void testMissingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&foo#123";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new StringReader(input), 0, writer));
    }

    @Test
    public void testInvalidBase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#xz123;";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new StringReader(input), 0, writer));
    }
}
