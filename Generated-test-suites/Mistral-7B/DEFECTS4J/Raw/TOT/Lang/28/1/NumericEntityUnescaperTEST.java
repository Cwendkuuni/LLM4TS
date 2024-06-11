package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.Testutils;
import org.apache.commons.text.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    private static final CharSequenceTranslator UNESCAPER = new NumericEntityUnescaper();

    /**
     * Test translating a simple entity like &amp;.
     */
    @Test
    public void testSimpleEntity() {
        Writer writer = new StringWriter();
        CharSequence input = "&amp;";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 1, actual: " + length, 1, length);
        assertEquals("Expected: &, actual: " + writer.toString(), "&", writer.toString());
    }

    /**
     * Test translating a simple hex entity like &#x41;.
     */
    @Test
    public void testSimpleHexEntity() {
        Writer writer = new StringWriter();
        CharSequence input = "&#x41;";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 1, actual: " + length, 1, length);
        assertEquals("Expected: A, actual: " + writer.toString(), "A", writer.toString());
    }

    /**
     * Test translating a simple decimal entity like &#101;.
     */
    @Test
    public void testSimpleDecimalEntity() {
        Writer writer = new StringWriter();
        CharSequence input = "&#101;";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 1, actual: " + length, 1, length);
        assertEquals("Expected: e, actual: " + writer.toString(), "e", writer.toString());
    }

    /**
     * Test translating an entity that is too long.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTooLongEntity() {
        Writer writer = new StringWriter();
        CharSequence input = "&foo&x2234;";
        UNESCAPER.translate(input, 0, writer);
    }

    /**
     * Test translating a sequence of entities.
     */
    @Test
    public void testSequenceOfEntities() {
        Writer writer = new StringWriter();
        CharSequence input = "&amp;&#x61;&#48;&#x65;&#x6C;&#x6C;&#x6F;&#x77;&#x73;&#x74;&#x65;&#x26;&#x23;&#x3B;&#x26;&#x3C;&#x3B;&#x3E;&#x26;&#x3C;&#x3B;&#x26;&#x23;&#x23;" +
                "&amp;";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 18, actual: " + length, 18, length);
        assertEquals("Expected: &amp;amp;amp;and&quotedbl;&amp;lt;&gt;&amp;gt;&amp;quot;&amp;amp;amp;amp;", writer.toString(),
                "&amp;amp;amp;and&quotedbl;&amp;lt;&gt;&amp;gt;&amp;quot;&amp;amp;amp;amp;");
    }

    /**
     * Test translating an empty sequence.
     */
    @Test
    public void testEmptySequence() {
        Writer writer = new StringWriter();
        CharSequence input = "";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 0, actual: " + length, 0, length);
        assertEquals("Expected: ", writer.toString(), "");
    }

    /**
     * Test translating a sequence with no entities.
     */
    @Test
    public void testNoEntitiesSequence() {
        Writer writer = new StringWriter();
        CharSequence input = "foo";
        int length = UNESCAPER.translate(input, 0, writer);
        assertEquals("Expected length: 3, actual: " + length, 3, length);
        assertEquals("Expected: foo, actual: " + writer.toString(), "foo", writer.toString());
    }
}