package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {
   @Test
    public void testTranslate() throws IOException {
        // create a lookup table for testing
        CharSequence[][] lookupTable = {
            {"a", "alpha"},
            {"b", "beta"},
            {"c", "gamma"}
        };
        
        LookupTranslator translator = new LookupTranslator(lookupTable);
        
        // test when input contains a matching substring
        StringBuilder input1 = new StringBuilder("foo bar");
        StringBuilder output1 = new StringBuilder();
        int result1 = translator.translate(input1, 4, output1);
        assertEquals(result1, 3);
        assertEquals(output1.toString(), "bar");
        
        // test when input does not contain a matching substring
        StringBuilder input2 = new StringBuilder("foo bar");
        StringBuilder output2 = new StringBuilder();
        int result2 = translator.translate(input2, 0, output2);
        assertEquals(result2, 0);
        assertEquals(output2.toString(), "");
        
        // test when input is empty
        StringBuilder input3 = new StringBuilder("");
        StringBuilder output3 = new StringBuilder();
        int result3 = translator.translate(input3, 0, output3);
        assertEquals(result3, 0);
        assertEquals(output3.toString(), "");
    }
}