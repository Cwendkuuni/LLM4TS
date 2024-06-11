package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = {"abc", "123"};
        CharSequence[] seq2 = {"def", "456"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        StringBuffer buffer = new StringBuffer();
        Writer writer = mock(Writer.class);
        
        // Test longest sequence
        CharSequence input1 = "abcdef";
        int index1 = 0;
        int result1 = translator.translate(input1, index1, writer);
        verify(writer).write("123456");
        assertEquals(6, result1);
        
        // Test shortest sequence
        CharSequence input2 = "abc";
        int index2 = 0;
        int result2 = translator.translate(input2, index2, writer);
        verify(writer).write("123");
        assertEquals(3, result2);
        
        // Test sequence not found
        CharSequence input3 = "xyz";
        int index3 = 0;
        int result3 = translator.translate(input3, index3, writer);
        verify(writer).write("xyz"); // The same sequence is written
        assertEquals(0, result3);
        
        // Test input shorter than shortest sequence
        CharSequence input4 = "ab";
        int index4 = 0;
        int result4 = translator.translate(input4, index4, writer);
        verify(writer).write("ab"); // The same sequence is written
        assertEquals(0, result4);
    }

    @Test
    public void testLookupTranslator() {
        CharSequence[] seq1 = {"abc", "123"};
        CharSequence[] seq2 = {"def", "456"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<CharSequence, CharSequence>();
        expectedLookupMap.put(seq1[0], seq1[1]);
        expectedLookupMap.put(seq2[0], seq2[1]);
        
        assertEquals(expectedLookupMap, translator.lookupMap);
    }

}