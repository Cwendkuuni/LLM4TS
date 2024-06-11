
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test single digit decimal entity
        assertEquals(5, unescaper.translate("&#53;", 0, new StringWriter()));
        assertEquals(7, unescaper.translate("&#55;", 0, new StringWriter()));
        
        // Test multiple digit decimal entity
        assertEquals(65, unescaper.translate("&#65;", 0, new StringWriter()));
        assertEquals(3665, unescaper.translate("&#3665;", 0, new StringWriter()));
        
        // Test single digit hexadecimal entity
        assertEquals(10, unescaper.translate("&#xA;", 0, new StringWriter()));
        assertEquals(13, unescaper.translate("&#xD;", 0, new StringWriter()));
        
        // Test multiple digit hexadecimal entity
        assertEquals(255, unescaper.translate("&#xFF;", 0, new StringWriter()));
        assertEquals(32767, unescaper.translate("&#x7FFF;", 0, new StringWriter()));
    }
}
