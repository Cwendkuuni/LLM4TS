import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for LookupTranslator
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringWriter writer = new StringWriter();
        Assert.assertEquals(0, translator.translate(CharSequence.EMPTY, 0, writer));
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}, {"b", "B"}});
        StringWriter writer = new StringWriter();
        Assert.assertEquals(0, translator.translate(CharSequence.EMPTY, 0, writer));
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testSingleCharacterInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}, {"b", "B"}});
        StringWriter writer = new StringWriter();
        for (char c = 'a'; c <= 'z'; c++) {
            Assert.assertEquals(1, translator.translate(CharSequence.of(c), 0, writer));
            Assert.assertEquals(String.valueOf(c.toUpperCase()), writer.toString());
            writer.getBuffer().setLength(0);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            Assert.assertEquals(1, translator.translate(CharSequence.of(c), 0, writer));
            Assert.assertEquals(String.valueOf(c), writer.toString());
            writer.getBuffer().setLength(0);
        }
    }

    @Test
    public void testMultiCharacterInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "AB"}, {"cd", "CD"}});
        StringWriter writer = new StringWriter();
        Assert.assertEquals(2, translator.translate(CharSequence.of("ab"), 0, writer));
        Assert.assertEquals("AB", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(2, translator.translate(CharSequence.of("cd"), 0, writer));
        Assert.assertEquals("CD", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(0, translator.translate(CharSequence.of("ef"), 0, writer));
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testOverlappingInputs() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "AB"}, {"cd", "CD"}});
        StringWriter writer = new StringWriter();

        Assert.assertEquals(2, translator.translate(CharSequence.of("abcd"), 0, writer));
        Assert.assertEquals("ABCD", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(2, translator.translate(CharSequence.of("cdab"), 0, writer));
        Assert.assertEquals("CDAB", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(1, translator.translate(CharSequence.of("abcd"), 2, writer));
        Assert.assertEquals("CD", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(1, translator.translate(CharSequence.of("cdab"), 2, writer));
        Assert.assertEquals("AB", writer.toString());
        writer.getBuffer().setLength(0);

        Assert.assertEquals(0, translator.translate(CharSequence.of("efghij"), 0, writer));
        Assert.assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLookupEntry() {
        new LookupTranslator(new CharSequence[][]{{}, {}});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputCharSequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}, {"b", "B"}});
        translator.translate(null, 0, new StringWriter());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndex() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}, {"b", "B"}});
        translator.translate(CharSequence.of("ab"), -1, new StringWriter());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInputBeyondEndOfSequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}, {"b", "B"}});
        translator.translate(CharSequence.of("ab"), 2, new StringWriter());
    }
}