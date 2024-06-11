package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.NullResolver;
import org.apache.commons.lang3.test.equality.NullEqualityStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private StringWriter writer;

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"empty", new CharSequence[][]{}},
                {"simple", new CharSequence[][]{{"hi", "hello"}}},
                {"multiple", new CharSequence[][]{{"hi", "hello"}, {"bye", "goodbye"}}}
        });
    }

    public LookupTranslatorTest(String name, CharSequence[][] lookup) {
        this.translator = new LookupTranslator(lookup);
        this.writer = new StringWriter();
    }

    @Before
    public void setUp() {
        this.writer = new StringWriter();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(this.translator);
    }

    @Test
    public void testEmptyTranslation() {
        Assert.assertEquals("", this.translator.translate(CharSequenceUtils.EMPTY, 0, this.writer));
    }

    @Test
    public void testSimpleTranslation() {
        this.translator.translate("hi", 0, this.writer);
        Assert.assertEquals("hello", this.writer.toString());
    }

    @Test
    public void testMultipleTranslation() {
        this.translator.translate("hi", 0, this.writer);
        Assert.assertEquals("hello", this.writer.toString());
        this.translator.translate("bye", 0, this.writer);
        Assert.assertEquals("goodbye", this.writer.toString());
    }

    @Test(expected = IOException.class)
    public void testNullInput() {
        this.translator.translate(NullResolver.NULL, 0, this.writer);
    }

    @Test
    public void testNegativeIndex() {
        Assert.assertEquals(0, this.translator.translate(CharSequenceUtils.EMPTY, -1, this.writer));
    }

    @Test
    public void testEmptyLookupMap() {
        LookupTranslator emptyTranslator = new LookupTranslator(new CharSequence[][]{});
        StringWriter emptyWriter = new StringWriter();
        Assert.assertEquals(0, emptyTranslator.translate(CharSequenceUtils.EMPTY, 0, emptyWriter));
        Assert.assertEquals("", emptyWriter.toString());
    }

    @Test
    public void testLongerInput() {
        LookupTranslator longerTranslator = new LookupTranslator(new CharSequence[][]{{"hi", "hello"}});
        StringWriter longerWriter = new StringWriter();
        longerTranslator.translate("hi there", 0, longerWriter);
        Assert.assertEquals("hello there", longerWriter.toString());
    }

    @Test
    public void testShorterInput() {
        LookupTranslator shorterTranslator = new LookupTranslator(new CharSequence[][]{{"hi", "hello"}});
        StringWriter shorterWriter = new StringWriter();
        shorterTranslator.translate("hi", 4, shorterWriter);
        Assert.assertEquals("hi", shorterWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShorterInputThanShortestKey() {
        LookupTranslator shorterTranslator = new LookupTranslator(new CharSequence[][]{{"hi", "hello"}});
        shorterTranslator.translate("hi", -1, this.writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongerInputThanLongestKey() {
        LookupTranslator longerTranslator = new LookupTranslator(new CharSequence[][]{{"hi", "hello"}});
        longerTranslator.translate("hi there", 6, this.writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupMap() {
        LookupTranslator translator = new LookupTranslator(null);
    }
}