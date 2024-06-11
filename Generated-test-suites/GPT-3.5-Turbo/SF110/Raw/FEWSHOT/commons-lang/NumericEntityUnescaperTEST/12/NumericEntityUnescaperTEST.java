
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate() throws IOException {
        StringBuilder inputBuilder = new StringBuilder();
        inputBuilder.append("&amp;");
        inputBuilder.append("&#36;");
        inputBuilder.append("&#x41;");
        inputBuilder.append("&#97;");
        inputBuilder.append("&#999999;");
        inputBuilder.append("&gt;");
        inputBuilder.append("&lt;");
        inputBuilder.append("&quot;");
        inputBuilder.append("&apos;");
        String input = inputBuilder.toString();

        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals("Unexpected result", 5, result);
        assertEquals("Unexpected output", "&$Aa\uE401&gt;&lt;\"\''", writer.toString());
    }

    @Test
    public void testIsSet() {
        assertFalse("Unexpected result", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("Unexpected result", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        assertTrue("Unexpected result", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
}
