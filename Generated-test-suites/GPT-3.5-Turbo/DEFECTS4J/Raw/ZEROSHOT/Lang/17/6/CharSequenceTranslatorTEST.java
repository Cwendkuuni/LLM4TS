import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new AggregateTranslator(new CharSequenceTranslator[]{new UnicodeEscaper()});
    }

    @Test
    public void testTranslate() {
        String input = "hello";
        String expectedResult = "\\u0068\\u0065\\u006c\\u006c\\u006f";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withNullInput() {
        String input = null;
        String expectedResult = null;
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withEmptyInput() {
        String input = "";
        String expectedResult = "";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withUnicodeCharacters() {
        String input = "你好";
        String expectedResult = "\\u4F60\\u597D";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withSpecialCharacters() {
        String input = "!@#$%^&*()";
        String expectedResult = "!@#$%^&*()";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withEscapedCharacters() {
        String input = "\\n\\t\\r";
        String expectedResult = "\\n\\t\\r";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withMixedCharacters() {
        String input = "abc\\n\\t\\r123";
        String expectedResult = "abc\\n\\t\\r123";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withSurrogatePair() {
        String input = "𐐷";
        String expectedResult = "\\uD801\\uDC37";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withEmoji() {
        String input = "";

        String expectedResult = "";
        String result = translator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTranslate_withMultipleTranslators() {
        CharSequenceTranslator translator1 = new UnicodeUnescaper();
        CharSequenceTranslator translator2 = new LookupTranslator(new String[][]{{"abc", "xyz"}});
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String input = "\\u0061\\u0062\\u0063";
        String expectedResult = "xyz";
        String result = mergedTranslator.translate(input);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter() throws Exception {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "hello";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testTranslate_withEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "hello";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        assertEquals(input, result);
    }

    @Test
    public void testTranslate_withWriter() throws Exception {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "hello";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        assertEquals("\\u0068\\u0065\\u006c\\u006c\\u006f", result);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new UnicodeEscaper();
        CharSequenceTranslator translator2 = new LookupTranslator(new String[][]{{"abc", "xyz"}});
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }
}