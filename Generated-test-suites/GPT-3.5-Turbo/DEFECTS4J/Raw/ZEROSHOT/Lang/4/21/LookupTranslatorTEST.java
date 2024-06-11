package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[] lookup1 = {{"a","A"},{"b","B"}};
        LookupTranslator translator1 = new LookupTranslator(lookup1);
        
        assertEquals(2, translator1.lookupMap.size());
        assertEquals(1, translator1.shortest);
        assertEquals(1, translator1.longest);
        
        CharSequence[] lookup2 = {{"a","A"},{"b","B"},{"cd","CD"}};
        LookupTranslator translator2 = new LookupTranslator(lookup2);
        
        assertEquals(3, translator2.lookupMap.size());
        assertEquals(1, translator2.shortest);
        assertEquals(2, translator2.longest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] lookup = {{"a","A"},{"b","B"},{"cd","CD"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        CharSequence input1 = "abcd";
        StringWriter writer1 = new StringWriter();
        int result1 = translator.translate(input1, 0, writer1);
        assertEquals("ABCD", writer1.toString());
        assertEquals(4, result1);
        
        CharSequence input2 = "xyz";
        StringWriter writer2 = new StringWriter();
        int result2 = translator.translate(input2, 0, writer2);
        assertEquals("", writer2.toString());
        assertEquals(0, result2);
    }
    
    @Test
    public void testTranslate_withMaxLength() throws IOException {
        CharSequence[] lookup = {{"a","A"},{"b","B"},{"cd","CD"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        CharSequence input1 = "abcd";
        StringWriter writer1 = new StringWriter();
        int result1 = translator.translate(input1, 0, writer1);
        assertEquals("ABCD", writer1.toString());
        assertEquals(4, result1);
        
        CharSequence input2 = "abcdefghijk";
        StringWriter writer2 = new StringWriter();
        int result2 = translator.translate(input2, 0, writer2);
        assertEquals("ABCDefghijk", writer2.toString());
        assertEquals(4, result2);
    }

}