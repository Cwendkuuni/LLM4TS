package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateTypical() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("");
        StringBuilder output = new StringBuilder("");
        assertEquals(0, unescaper.translate(input, 0, output));
        assertEquals("", output.toString());
        
        input = new StringBuilder("&#65;");
        output = new StringBuilder("");
        assertEquals(5, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        input = new StringBuilder("&#119;&#105;&#107;&#105;&#32;&#84;&#101;&#115;&#116;&#32;&#83;&#117;&#99;&#99;&#101;&#101;&#100;&#115;&#59;");
        output = new StringBuilder("");
        assertEquals(43, unescaper.translate(input, 0, output));
        assertEquals("wiki Test Succeeds;", output.toString());
    }

    @Test
    public void testTranslateEdge() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#38;#119;&#x105;&#97;&#109;&#112;&#59;");
        StringBuilder output = new StringBuilder("");
        assertEquals(18, unescaper.translate(input, 0, output));
        assertEquals("&waimp;", output.toString());

        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        input = new StringBuilder("&#38;#119;&#x105;&#97;&#109;&#112;&#59;");
        output = new StringBuilder("");
        assertEquals(18, unescaper.translate(input, 0, output));
        assertEquals("&waimp;", output.toString());

        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        input = new StringBuilder("&#38;#119;&#x105;&#97;&#109;&#112;&#59;");
        output = new StringBuilder("");
        assertEquals(16, unescaper.translate(input, 0, output));
        assertEquals("&#119;iamp;", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateError() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder("&#119;");
        StringBuilder output = new StringBuilder("");
        unescaper.translate(input, 0, output);

        unescaper = new NumericEntityUnescaper();
        input = new StringBuilder("&#38;#119;&#x105;&#97;&#109;&#112;");
        output = new StringBuilder("");
        unescaper.translate(input, 0, output);
    }
}