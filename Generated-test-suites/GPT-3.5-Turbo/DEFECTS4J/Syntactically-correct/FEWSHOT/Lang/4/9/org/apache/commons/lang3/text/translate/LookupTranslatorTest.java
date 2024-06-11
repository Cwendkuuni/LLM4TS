
package org.apache.commons.lang3.text.translate;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = {"foo", "bar"};
        CharSequence[] seq2 = {"baz", "qux"};
        CharSequence[][] lookup = {seq1, seq2};

        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();

        Assert.assertEquals(3, translator.translate("foobarbaz", 0, out));
        Assert.assertEquals("bar", out.toString());
        out.getBuffer().setLength(0);

        Assert.assertEquals(2, translator.translate("bazquxfoo", 3, out));
        Assert.assertEquals("qux", out.toString());
        out.getBuffer().setLength(0);

        Assert.assertEquals(0, translator.translate("abcde", 0, out));
        Assert.assertEquals("", out.toString());
    }
}
