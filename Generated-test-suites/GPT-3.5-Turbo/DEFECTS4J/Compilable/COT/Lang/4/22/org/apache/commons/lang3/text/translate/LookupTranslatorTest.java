package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{
                {"foo", "bar"},
                {"abc", "xyz"}
        });

        Assert.assertNotNull(lookupTranslator);
    }

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{
                {"foo", "bar"},
                {"abc", "xyz"}
        });

        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("foo", 0, writer);

        Assert.assertEquals(3, result);
        Assert.assertEquals("bar", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{});
        Assert.assertNotNull(lookupTranslator);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{});
        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("", 0, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithIndexOutOfRange() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{
                {"foo", "bar"}
        });
        StringWriter writer = new StringWriter();
        lookupTranslator.translate("foo", 10, writer);
    }

    @Test
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator lookupTranslator = new LookupTranslator((CharSequence[][]) null);
        Assert.assertNotNull(lookupTranslator);
    }

    @Test
    public void testLookupTranslatorWithNullInput() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{
                {"foo", "bar"}
        });
        Assert.assertNotNull(lookupTranslator);
    }
}