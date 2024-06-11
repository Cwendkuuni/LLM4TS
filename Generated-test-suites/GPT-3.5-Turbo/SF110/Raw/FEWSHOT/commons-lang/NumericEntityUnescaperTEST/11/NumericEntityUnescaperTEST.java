
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        // Test translation of decimal entity
        assertEquals(6, unescaper.translate("&#100;", 0, new StringWriter()));
        assertEquals("d", unescaper.translate("&#100;", 0, new StringWriter()).toString());

        // Test translation of hexadecimal entity
        assertEquals(6, unescaper.translate("&#x61;", 0, new StringWriter()));
        assertEquals("a", unescaper.translate("&#x61;", 0, new StringWriter()).toString());

        // Test translation of entity without semicolon
        assertThrows(IllegalArgumentException.class, () -> {
            unescaper.translate("&amp", 0, new StringWriter());
        });

        // Test translation of entity with invalid format
        assertEquals(0, unescaper.translate("&#", 0, new StringWriter()));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(!unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(!unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

}
