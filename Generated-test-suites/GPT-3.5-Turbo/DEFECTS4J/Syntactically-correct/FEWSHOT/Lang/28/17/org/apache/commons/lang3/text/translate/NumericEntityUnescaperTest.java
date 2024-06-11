package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;
    private Writer writer;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
        writer = mock(Writer.class);
    }

    @Test
    public void testTranslate() throws IOException {
        // Test case 1
        assertEquals(0, unescaper.translate("abc", 0, writer));
        
        // Test case 2
        String input = "&#65;";
        assertEquals(5, unescaper.translate(input, 0, writer));
        
        // Test case 3
        input = "&#x41;";
        assertEquals(6, unescaper.translate(input, 0, writer));
        
        // Test case 4
        input = "&#100;";
        assertEquals(6, unescaper.translate(input, 0, writer));
        
        // Test case 5
        input = "&#x64;";
        assertEquals(7, unescaper.translate(input, 0, writer));
    }
}