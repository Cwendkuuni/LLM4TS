
//Junit 4 test cases
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test OPTION.semiColonRequired
        assertEquals(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        
        // Test OPTION.semiColonOptional
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        
        // Test OPTION.errorIfNoSemiColon
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test translation of numeric entity "&#65;"
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        
        // Test translation of hexadecimal numeric entity "&#x41;"
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
        
        // Test translation of non-existent numeric entity "&#1234567;" without semicolon
        result = unescaper.translate("&#1234567", 0, writer);
        assertEquals(0, result);
        
        // Test translation of non-existent numeric entity "&#1234567;" with semicolon
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        result = unescaper.translate("&#1234567;", 0, writer);
        assertEquals(0, result);
        
        // Test translation of non-existent numeric entity "&#1234567;" with error
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            unescaper.translate("&#1234567", 0, writer);
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        
        // Test translation of high value numeric entity "&#65536;"
        result = unescaper.translate("&#65536;", 0, writer);
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }
}
