
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        assertNull(Util.translate(null));
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        assertEquals("", Util.translate(""));
    }
    
    @Test
    public void testTranslate_whenInputContainsNoTranslatableCharacters_shouldReturnInputString() {
        assertEquals("abc123", Util.translate("abc123"));
    }
    
    @Test
    public void testTranslate_whenInputContainsTranslatableCharacters_shouldTranslateAndReturnResult() {
        assertEquals("\\u0020\\u0021\\u0022\\u0023", Util.translate(" !\"#"));
        assertEquals("\\u0024\\u0025\\u0026\\u0027", Util.translate("$%&'"));
        assertEquals("\\u0028\\u0029\\u002A\\u002B", Util.translate("()*+"));
        assertEquals("\\u002C\\u002D\\u002E\\u002F", Util.translate(",.-/"));
        assertEquals("\\u0030\\u0031\\u0032\\u0033", Util.translate("0123"));
        assertEquals("\\u0034\\u0035\\u0036\\u0037", Util.translate("4567"));
        assertEquals("\\u0038\\u0039\\u003A\\u003B", Util.translate("89:;"));
        assertEquals("\\u003C\\u003D\\u003E\\u003F", Util.translate("<=>?"));
        assertEquals("\\u0040\\u0041\\u0042\\u0043", Util.translate("@ABC"));
        assertEquals("\\u0044\\u0045\\u0046\\u0047", Util.translate("DEFG"));
        assertEquals("\\u0048\\u0049\\u004A\\u004B", Util.translate("HIJK"));
        assertEquals("\\u004C\\u004D\\u004E\\u004F", Util.translate("LMNO"));
        assertEquals("\\u0050\\u0051\\u0052\\u0053", Util.translate("PQRS"));
        assertEquals("\\u0054\\u0055\\u0056\\u0057", Util.translate("TUVW"));
        assertEquals("\\u0058\\u0059\\u005A\\u005B", Util.translate("XYZ["));
        assertEquals("\\u005C\\u005D\\u005E\\u005F", Util.translate("\\]^_"));
        assertEquals("\\u0060\\u0061\\u0062\\u0063", Util.translate("`abc"));
        assertEquals("\\u0064\\u0065\\u0066\\u0067", Util.translate("defg"));
        assertEquals("\\u0068\\u0069\\u006A\\u006B", Util.translate("hijk"));
        assertEquals("\\u006C\\u006D\\u006E\\u006F", Util.translate("lmno"));
        assertEquals("\\u0070\\u0071\\u0072\\u0073", Util.translate("pqrs"));
        assertEquals("\\u0074\\u0075\\u0076\\u0077", Util.translate("tuvw"));
        assertEquals("\\u0078\\u0079\\u007A\\u007B", Util.translate("xyz{"));
        assertEquals("\\u007C\\u007D\\u007E", Util.translate("|}~"));
    }
    
    @Test
    public void testTranslate_whenInputContainsTranslatableUnicodeCharacters_shouldTranslateAndReturnResult() {
        assertEquals("\\u00A9\\u00AE\\u00B0\\u00BD", Util.translate("\u00A9\u00AE\u00B0\u00BD"));
        assertEquals("\\u2571\\u2572\\u25A0\\u25A1", Util.translate("\u2571\u2572\u25A0\u25A1"));
        assertEquals("\\u2600\\u2601\\u2602\\u2603", Util.translate("\u2600\u2601\u2602\u2603"));
        assertEquals("\\u2604\\u2605\\u2606\\u2607", Util.translate("\u2604\u2605\u2606\u2607"));
        assertEquals("\\u2608\\u2609\\u260A\\u260B", Util.translate("\u2608\u2609\u260A\u260B"));
    }
    
    @Test
    public void testTranslate_whenTranslatableCharactersAreUnicodeSurrogates_shouldTranslateAndReturnResult() {
        assertEquals("\\uD83D\\uDCA9\\uD83D\\uDCAB\\uD83D\\uDCC8", Util.translate("\uD83D\uDCA9\uD83D\uDCAB\uD83D\uDCC8"));
        assertEquals("\\uD83D\\uDCE0\\uD83D\\uDCE1\\uD83D\\uDCE2", Util.translate("\uD83D\uDCE0\uD83D\uDCE1\uD83D\uDCE2"));
        assertEquals("\\uD83D\\uDE80\\uD83D\\uDE81\\uD83D\\uDE82", Util.translate("\uD83D\uDE80\uD83D\uDE81\uD83D\uDE82"));
    }
    
    @Test
    public void testTranslate_withEmptyString_shouldTranslateAndReturnEmptyString() {
        assertEquals("", Util.translate(""));
    }
    
    @Test
    public void testTranslate_whenTranslationProducesEmptyString_shouldTranslateAndReturnEmptyString() {
        assertEquals("", Util.translate("abcde"));
    }
}
