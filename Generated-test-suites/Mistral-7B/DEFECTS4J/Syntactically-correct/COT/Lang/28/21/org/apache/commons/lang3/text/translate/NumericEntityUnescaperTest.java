package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

/**
 * NumericEntityUnescaperTest
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &#x41; World! &#10;";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
            String result = out.toString();
            Assert.assertEquals("&amp; Hello &#x41; World! \n", testString.substring(0, 17));
            Assert.assertEquals("Hello Á World!\n", result.substring(0, length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateHexCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &#x41#x4D; World!";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
            String result = out.toString();
            Assert.assertEquals("&amp; Hello &#x41&#x4D; World!", testString.substring(0, 25));
            Assert.assertEquals("Hello AC\n", result.substring(0, length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateOutOfBounds() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &x1F& World! &#10;";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
            String result = out.toString();
            Assert.assertEquals("&amp; Hello &x1F& World! \n", testString.substring(0, 23));
            Assert.assertEquals("", result.substring(0, length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &#xZZZ; World!";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &#xAAA; World!";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateEmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "";
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(new StringReader(testString), 0, out);
            String result = out.toString();
            Assert.assertEquals("", testString);
            Assert.assertEquals("", result.substring(0, length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        try {
            int length = unescaper.translate(null, 0, out);
            String result = out.toString();
            Assert.assertEquals(0, length);
            Assert.assertEquals("", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateNullOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp; Hello &#x41; World!";
        try {
            int length = unescaper.translate(new StringReader(testString), 0, null);
            Assert.assertEquals(0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}