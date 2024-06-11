package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator_EmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        String input = "";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTranslator_SingleTranslation() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        String input = "hello";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("hi", writer.toString());
        assertEquals(5, length);
    }

    @Test
    public void testLookupTranslator_MultipleTranslations() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "hello"}});
        String input = "helloworld";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("hellohello", writer.toString());
        assertEquals(11, length);
    }

    @Test
    public void testLookupTranslator_SingleTranslation_PartialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        String input = "helloworld";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("hi", writer.toString());
        assertEquals(5, length);
    }

    @Test
    public void testLookupTranslator_MultipleTranslations_PartialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "hello"}});
        String input = "helloworld";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("hello", writer.toString());
        assertEquals(5, length);
    }

    @Test
    public void testLookupTranslator_EmptyKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{}, {"world", "hello"}});
        String input = "hello";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTranslator_NullKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{null, "world"}, {"world", "hello"}});
        String input = "hello";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, length);
    }

    @Test(expected = IOException.class)
    public void testLookupTranslator_WriteError() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        String input = "helloworld";
        Writer writer = new Writer() {
            @Override
            public void write(int c, int len) throws IOException {
                throw new IOException("Write error");
            }
        };
        int length = translator.translate(input, 0, writer);
        fail("Expected IOException");
    }

    @Test
    public void testLookupTranslator_EmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        StringWriter writer = new StringWriter();
        int length = translator.translate(StringUtils.EMPTY, 0, writer);
        assertEquals(StringUtils.EMPTY, writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTranslator_LongInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        String input = CharSequenceUtils.repeat("hello", 1000);
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        String expected = CharSequenceUtils.repeat("hi", 1000);
        assertEquals(expected, writer.toString());
        assertEquals(5000, length);
    }

    @Test
    public void testLookupTranslator_LongestInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"longest", "shortest"}});
        String input = new String(new char[Integer.MAX_VALUE]);
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        fail("Expected IOException: Input too large");
    }

    @Test
    public void testLookupTranslator_ShortestInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"shortest", "longest"}});
        String input = StringUtils.EMPTY;
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        assertEquals(StringUtils.EMPTY, writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTranslator_MixedInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "hello"}});
        String input = "helloworldhellohelloworld";
        StringWriter writer = new StringWriter();
        int length = translator.translate(input, 0, writer);
        String expected = "hellohellohelloworldhello";
        assertEquals(expected, writer.toString());
        assertEquals(23, length);
    }
}