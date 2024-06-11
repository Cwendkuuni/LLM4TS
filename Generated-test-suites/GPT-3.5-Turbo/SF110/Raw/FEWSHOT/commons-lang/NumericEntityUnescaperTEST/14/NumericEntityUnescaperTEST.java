package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() {
        NumericEntityUnescaper neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter out = new StringWriter();

        try {
            int result = neunescaper.translate("&#10;", 0, out);
            assertEquals(6, result);
            assertEquals("\n", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        try {
            int result = neunescaper.translate("&amp;", 0, out);
            assertEquals(0, result);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        out = new StringWriter();

        try {
            int result = neunescaper.translate("&#10&", 0, out);
            assertEquals(0, result);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        try {
            int result = neunescaper.translate("&#10;", 0, out);
            assertEquals(6, result);
            assertEquals("\n", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        try {
            int result = neunescaper.translate("&#x10;", 0, out);
            assertEquals(7, result);
            assertEquals("\u0010", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        out = new StringWriter();

        try {
            int result = neunescaper.translate("&#10", 0, out);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));

        neunescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(neunescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(neunescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}