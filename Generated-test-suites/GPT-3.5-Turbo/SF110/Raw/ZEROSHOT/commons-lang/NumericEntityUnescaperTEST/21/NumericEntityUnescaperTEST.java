package org.apache.commons.lang3.text.translate;

import org.junit.Assert;
import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        // Test for valid numeric entity with semi-colon required
        CharSequence input = "&#65;";
        CharArrayWriter writer = new CharArrayWriter();
        int result = unescaper.translate(input, 0, writer);
        Assert.assertEquals("Result should be 4", 4, result);
        Assert.assertArrayEquals("Writer should contain 'A'", new char[]{'A'}, writer.toCharArray());

        // Test for valid numeric entity with semi-colon optional
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        input = "&#65";
        writer = new CharArrayWriter();
        result = unescaper.translate(input, 0, writer);
        Assert.assertEquals("Result should be 3", 3, result);
        Assert.assertArrayEquals("Writer should contain 'A'", new char[]{'A'}, writer.toCharArray());

        // Test for valid numeric entity with error if no semi-colon
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        input = "&#65";
        writer = new CharArrayWriter();
        try {
            result = unescaper.translate(input, 0, writer);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }

        // Test for invalid numeric entity
        unescaper = new NumericEntityUnescaper();
        input = "&";
        writer = new CharArrayWriter();
        result = unescaper.translate(input, 0, writer);
        Assert.assertEquals("Result should be 0", 0, result);
        Assert.assertArrayEquals("Writer should be empty", new char[]{}, writer.toCharArray());

        // Test for invalid numeric entity with semi-colon required
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        input = "&#";
        writer = new CharArrayWriter();
        result = unescaper.translate(input, 0, writer);
        Assert.assertEquals("Result should be 0", 0, result);
        Assert.assertArrayEquals("Writer should be empty", new char[]{}, writer.toCharArray());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        Assert.assertTrue("OPTION.semiColonRequired should be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        Assert.assertFalse("OPTION.semiColonOptional should not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        Assert.assertFalse("OPTION.errorIfNoSemiColon should not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}