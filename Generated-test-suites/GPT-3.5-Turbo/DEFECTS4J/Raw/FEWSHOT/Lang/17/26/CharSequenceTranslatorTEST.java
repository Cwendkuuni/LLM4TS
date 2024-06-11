package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate() {
        // Test empty input
        assertEquals("", CharSequenceTranslator.translate(""));
        
        // Test null input
        assertNull(CharSequenceTranslator.translate(null));
        
        // Test translation
        assertEquals("hello", CharSequenceTranslator.translate("hello"));
        assertEquals("&lt;script&gt;", CharSequenceTranslator.translate("<script>"));
        assertEquals("&amp;&lt;&gt;&#39;&quot;", CharSequenceTranslator.translate("&<>'\""));
    }
    
    @Test
    public void testTranslateWithWriter() {
        // Test empty input
        StringWriter writer = new StringWriter();
        t.translate("", writer);
        assertEquals("", writer.toString());
        
        // Test null input
        writer = new StringWriter();
        t.translate(null, writer);
        assertNull(writer.toString());
        
        // Test translation
        writer = new StringWriter();
        t.translate("hello", writer);
        assertEquals("hello", writer.toString());
        
        writer = new StringWriter();
        t.translate("<script>", writer);
        assertEquals("&lt;script&gt;", writer.toString());
        
        writer = new StringWriter();
        t.translate("&<>'\"", writer);
        assertEquals("&amp;&lt;&gt;&#39;&quot;", writer.toString());
    }
    
    @Test
    public void testTranslateWithIndexAndWriter() {
        // Test empty input
        StringWriter writer = new StringWriter();
        t.translate("", 0, writer);
        assertEquals("", writer.toString());
        
        // Test null input
        writer = new StringWriter();
        t.translate(null, 0, writer);
        assertNull(writer.toString());
        
        // Test translation
        writer = new StringWriter();
        t.translate("hello", 0, writer);
        assertEquals("hello", writer.toString());
        
        writer = new StringWriter();
        t.translate("<script>", 0, writer);
        assertEquals("&lt;script&gt;", writer.toString());
        
        writer = new StringWriter();
        t.translate("&<>'\"", 0, writer);
        assertEquals("&amp;&lt;&gt;&#39;&quot;", writer.toString());
    }
    
    @Test
    public void testWith() {
        // Test with null translators
        assertSame(t, t.with());
        
        // Test with single translator
        CharSequenceTranslator translator = new MyTranslator();
        CharSequenceTranslator merged = t.with(translator);
        assertNotSame(t, merged);
        assertSame(translator, merged);
        
        // Test with multiple translators
        CharSequenceTranslator translator1 = new MyTranslator();
        CharSequenceTranslator translator2 = new MyTranslator();
        CharSequenceTranslator merged = t.with(translator1, translator2);
        assertNotSame(t, merged);
        assertFalse(merged instanceof MyTranslator);
        assertEquals(3, merged.translators.length);
        assertSame(t, merged.translators[0]);
        assertSame(translator1, merged.translators[1]);
        assertSame(translator2, merged.translators[2]);
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F68", CharSequenceTranslator.hex(8040));
        assertEquals("10FFFF", CharSequenceTranslator.hex(1114111));
    }
}

class MyTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        // Do nothing
        return 0;
    }
}