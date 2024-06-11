package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new java.io.StringWriter();

        // Test simple numeric entities
        String input = "&amp; &lt; &gt; &apos; &quot; &amp;#128; &amp;#129; &amp;#x7F;";
        int expectedLength = input.length() + 6; // 2 entities replaced + 1 'x' in hex entity
        unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        assertEquals("& & < > ' \" ð Ð", output);
        assertEquals(expectedLength, output.length());

        // Test hex entities
        input = "&amp;#x61; &amp;#x43; &amp;#x27;";
        expectedLength = input.length() + 3; // 3 entities replaced
        unescaper.translate(new StringReader(input), 0, writer);
        output = writer.toString();
        assertEquals("ab '", output);
        assertEquals(expectedLength, output.length());

        // Test entities with leading and trailing whitespaces
        input = " &amp;#x61; &amp;#x43;&#xA0; &amp;#x27;&#xA0;";
        expectedLength = input.length() + 3; // 3 entities replaced
        unescaper.translate(new StringReader(input), 0, writer);
        output = writer.toString();
        assertEquals(" ab  '", output);
        assertEquals(expectedLength, output.length());

        // Test entities with different character sets
        input = "&amp;#199; &amp;#231; &amp;#352;";
        expectedLength = input.length() + 3; // 3 entities replaced
        unescaper.translate(new StringReader(input), 0, writer);
        output = writer.toString();
        assertEquals("ĳ ß Ŕ", output);
        assertEquals(expectedLength, output.length());

        // Test entities with different starting index
        String subInput = "&amp;#x61;";
        int index = 3;
        unescaper.translate(new StringReader(input), index, writer);
        output = writer.toString();
        assertEquals(" ab '", output);
        assertEquals(expectedLength - 3, output.length());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new java.io.StringWriter();
        StringReader reader = new StringReader("&amp;");

        unescaper.translate(reader, 0, writer);
    }

    @Test
    public void testInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new java.io.StringWriter();

        String input = "&amp;#INVALID;";
        String expectedOutput = "&amp;#INVALID;";
        unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new java.io.StringWriter();

        unescaper.translate(new StringReader(""), 0, writer);
        String output = writer.toString();
        assertEquals("", output);
    }

    @Test
    public void testNullInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new java.io.StringWriter();

        unescaper.translate(null, 0, writer);
        String output = writer.toString();
        assertEquals("", output);
    }
}

class StringReader extends CharSequence {
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
        return new StringReader(cs.subSequence(start, end).toString());
    }
}