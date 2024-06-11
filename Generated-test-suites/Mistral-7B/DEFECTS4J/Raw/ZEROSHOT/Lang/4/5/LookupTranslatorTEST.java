package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.CommonUtils;
import org.apache.commons.lang3.test.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private StringWriter writer;

    @Parameterized.Parameters(name = "{index}: lookupTable={0}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}}},
                {new CharSequence[][]{{"", ""}}},
                {new CharSequence[][]{{}, {}}},
                {new CharSequence[][]{{"apple", "banana"}, {"banana", "apple"}}}
        });
    }

    @Parameterized.Constructor
    public LookupTranslatorTest(CharSequence[][] lookup) {
        translator = new LookupTranslator(lookup);
        writer = new StringWriter();
    }

    @Before
    public void setup() {
        writer = new StringWriter();
    }

    @Test
    public void testTranslationWithValidInputAndIndex() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(new String("foobar"), 0, writer);
        assertEquals("bar", writer.toString());
    }

    @Test
    public void testTranslationWithValidInputAndOutOfRangeIndex() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(new String("foobar"), 2, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslationWithNullInput() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate((CharSequence) null, 0, writer);
        assertNull(writer.toString());
    }

    @Test
    public void testTranslationWithEmptyLookupTable() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{}, {}});
        translator.translate(new String(""), 0, writer);
        assertEquals("", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTranslationWithNullLookupTable() throws Exception {
        translator = new LookupTranslator(null);
    }

    @Test
    public void testTranslationWithEmptyInput() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(CharSequenceUtils.EMPTY, 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslationWithEmptyLookupValue() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", ""}, {"baz", "qux"}});
        translator.translate(new String("foobar"), 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslationWithShortestInputSequence() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(new String("foo"), 0, writer);
        assertEquals("bar", writer.toString());
    }

    @Test
    public void testTranslationWithLongestInputSequence() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(new String("foobarqux"), 0, writer);
        assertEquals("barqux", writer.toString());
    }

    @Test
    public void testTranslationWithInputSequenceLongerThanLongestLookupValue() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        translator.translate(new String("foobarqux"), 2, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslationWithMultipleMatchesInLookupTable() throws Exception {
        translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"foo", "qux"}});
        translator.translate(new String("foofoo"), 0, writer);
        assertEquals("qux", writer.toString());
    }

    @Test
    public void testEqualsAndHashcode() throws Exception {
        final CharSequence[][] lookup1 = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        final CharSequence[][] lookup2 = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        final CharSequence[][] lookup3 = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}, {"qux", "qux"}};
        final LookupTranslator translator1 = new LookupTranslator(lookup1);
        final LookupTranslator translator2 = new LookupTranslator(lookup2);
        final LookupTranslator translator3 = new LookupTranslator(lookup3);
        new EqualsTester()
                .addEqualityGroup(translator1, translator2)
                .addEqualityGroup(translator1, translator1)
                .addEqualityGroup(translator2, translator2)
                .addEqualityGroup(translator3)
                .testEquals();
        new EqualsTester()
                .addEqualityGroup(translator1, translator1)
                .testHashcode();
    }

    @Test
    public void testToString() throws Exception {
        final CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("LookupTranslator[lookupTable=[[foo, bar], [baz, qux]]]", translator.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        final LookupTranslator original = new LookupTranslator(lookup);
        final LookupTranslator clone = (LookupTranslator) original.clone();
        new EqualsTester()
                .addEqualityGroup(original, clone)
                .testEquals();
    }
}