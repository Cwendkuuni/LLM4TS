package org.apache.commons.lang3.text.translate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    private TestTranslator testTranslator;

    @Before
    public void setUp() {
        testTranslator = new TestTranslator();
    }

    @Test
    public void testTranslateWithPositiveIndexAndValidWriter() throws IOException {
        StringWriter writer = new StringWriter();

        int consumed = testTranslator.translate("input", 0, writer);

        Assert.assertEquals(5, consumed);
        Assert.assertEquals("translated", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        testTranslator.translate("input", 0, null);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        StringWriter writer = new StringWriter();

        int consumed = testTranslator.translate(null, 0, writer);

        Assert.assertEquals(0, consumed);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithZeroConsumed() throws IOException {
        StringWriter writer = new StringWriter();

        int consumed = testTranslator.translate("input", 6, writer);

        Assert.assertEquals(0, consumed);
        Assert.assertEquals("input", writer.toString());
    }

    @Test
    public void testTranslateWithCharacterOverflow() throws IOException {
        StringWriter writer = new StringWriter();

        int consumed = testTranslator.translate("input", 0, writer);

        Assert.assertEquals(100, consumed);
        Assert.assertEquals("overflow", writer.toString());
    }

    @Test
    public void testTranslateWithoutWriter() {
        String output = testTranslator.translate("input");

        Assert.assertEquals("translated", output);
    }

    @Test
    public void testTranslateWithNullInputWithoutWriter() {
        String output = testTranslator.translate(null);

        Assert.assertNull(output);
    }

    @Test
    public void testTranslateWithCharacterOverflowWithoutWriter() {
        String output = testTranslator.translate("input");

        Assert.assertEquals("overflow", output);
    }

    @Test
    public void testWith() {
        TestTranslator translator1 = new TestTranslator();
        TestTranslator translator2 = new TestTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        Assert.assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        String hex = CharSequenceTranslator.hex(65);

        Assert.assertEquals("41", hex);
    }

    @After
    public void tearDown() {
        testTranslator = null;
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            switch (input.charAt(index)) {
                case 'i':
                    out.write("translated");
                    return 5;
                case 'o':
                    out.write("overflow");
                    return 100;
                default:
                    out.write(input.charAt(index));
                    return 1;
            }
        }
    }
}