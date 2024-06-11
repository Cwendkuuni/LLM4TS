package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

    @Test
    public void testTranslate() throws IOException {
        Assert.assertEquals(0, unescaper.translate("", 0, new StringWriter()));
        Assert.assertEquals(0, unescaper.translate("abc", 0, new StringWriter()));
        Assert.assertEquals(0, unescaper.translate("&", 0, new StringWriter()));
        Assert.assertEquals(0, unescaper.translate("&#", 0, new StringWriter()));
        Assert.assertEquals(0, unescaper.translate("&#x", 0, new StringWriter()));
        
        Writer writer = new StringWriter();
        Assert.assertEquals(3, unescaper.translate("&#65;", 0, writer));
        Assert.assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        Assert.assertEquals(4, unescaper.translate("&#x41;", 0, writer));
        Assert.assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        Assert.assertEquals(5, unescaper.translate("&#65535;", 0, writer));
        Assert.assertEquals("\uDFFF", writer.toString());
        
        writer = new StringWriter();
        Assert.assertEquals(5, unescaper.translate("&#x10FFFF;", 0, writer));
        Assert.assertEquals("\uD800\uDC00", writer.toString());
        
        try {
            unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
            unescaper.translate("&#65", 0, new StringWriter());
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Assert.assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        Assert.assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Assert.assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        Assert.assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

}