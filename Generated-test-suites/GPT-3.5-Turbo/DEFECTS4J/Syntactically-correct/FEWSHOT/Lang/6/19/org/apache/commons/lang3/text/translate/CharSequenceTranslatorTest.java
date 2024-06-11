
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    private static class DummyTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(""+input.charAt(index));
            return 1;
        }
        
    }
    
    private static class TestTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            String s = input.subSequence(index, input.length()).toString();
            int consumed;
            
            if (s.startsWith("abc")) {
                out.write("ABC");
                consumed = 3;
            } else if (s.startsWith("def")) {
                out.write("DEF");
                consumed = 3;
            } else if (s.startsWith("ghi")) {
                out.write("GHI");
                consumed = 3;
            } else if (s.startsWith("123")) {
                out.write("321");
                consumed = 3;
            } else {
                out.write(""+input.charAt(index));
                consumed = 1;
            }
            
            return consumed;
        }
        
    }
    
    @Test
    public void testTranslate_DummyTranslator() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate("hello", out);
        assertEquals("hello", out.toString());
        
        out = new StringWriter();
        dummy.translate("123", out);
        assertEquals("123", out.toString());
    }
    
    @Test
    public void testTranslate_TestTranslator() throws IOException {
        TestTranslator test = new TestTranslator();
        
        StringWriter out = new StringWriter();
        test.translate("abcxyz", out);
        assertEquals("ABCxyz", out.toString());
        
        out = new StringWriter();
        test.translate("def", out);
        assertEquals("DEF", out.toString());
        
        out = new StringWriter();
        test.translate("ghi", out);
        assertEquals("GHI", out.toString());
        
        out = new StringWriter();
        test.translate("123", out);
        assertEquals("321", out.toString());
        
        out = new StringWriter();
        test.translate("xyz", out);
        assertEquals("xyz", out.toString());
    }
    
    @Test
    public void testTranslate_nullInput() throws IOException {
        TestTranslator test = new TestTranslator();
        
        StringWriter out = new StringWriter();
        test.translate(null, out);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_emptyInput() throws IOException {
        TestTranslator test = new TestTranslator();
        
        StringWriter out = new StringWriter();
        test.translate("", out);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_singleCharacterInput() throws IOException {
        TestTranslator test = new TestTranslator();
        
        StringWriter out = new StringWriter();
        test.translate("a", out);
        assertEquals("a", out.toString());
        
        out = new StringWriter();
        test.translate("x", out);
        assertEquals("x", out.toString());
        
        out = new StringWriter();
        test.translate("1", out);
        assertEquals("1", out.toString());
    }
    
    @Test
    public void testTranslate_multipleCharacterInput() throws IOException {
        TestTranslator test = new TestTranslator();
        
        StringWriter out = new StringWriter();
        test.translate("abc def ghi", out);
        assertEquals("ABC DEF GHI", out.toString());
        
        out = new StringWriter();
        test.translate("xyz", out);
        assertEquals("xyz", out.toString());
        
        out = new StringWriter();
        test.translate("123", out);
        assertEquals("321", out.toString());
    }
    
    @Test
    public void testTranslate_singleCharacterTranslator() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate("abc def ghi", out);
        assertEquals("abc def ghi", out.toString());
        
        out = new StringWriter();
        dummy.translate("xyz", out);
        assertEquals("xyz", out.toString());
        
        out = new StringWriter();
        dummy.translate("123", out);
        assertEquals("123", out.toString());
    }
    
    @Test
    public void testTranslate_singleCharacterTranslator_nullInput() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate(null, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_singleCharacterTranslator_emptyInput() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate("", out);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_singleCharacterTranslator_singleCharacterInput() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate("a", out);
        assertEquals("a", out.toString());
        
        out = new StringWriter();
        dummy.translate("x", out);
        assertEquals("x", out.toString());
        
        out = new StringWriter();
        dummy.translate("1", out);
        assertEquals("1", out.toString());
    }
    
    @Test
    public void testTranslate_singleCharacterTranslator_multipleCharacterInput() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        
        StringWriter out = new StringWriter();
        dummy.translate("abc def ghi", out);
        assertEquals("abc def ghi", out.toString());
        
        out = new StringWriter();
        dummy.translate("xyz", out);
        assertEquals("xyz", out.toString());
        
        out = new StringWriter();
        dummy.translate("123", out);
        assertEquals("123", out.toString());
    }
    
    @Test
    public void testTranslate_nullWriter() {
        TestTranslator test = new TestTranslator();
        
        try {
            test.translate("abc", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testWith() throws IOException {
        DummyTranslator dummy = new DummyTranslator();
        TestTranslator test = new TestTranslator();
        
        CharSequenceTranslator merged = test.with(dummy).with(dummy);
        
        StringWriter out = new StringWriter();
        merged.translate("abc", out);
        assertEquals("abc", out.toString());
    }
    
    @Test
    public void testHex() {
        assertEquals("1", CharSequenceTranslator.hex(1));
        assertEquals("A", CharSequenceTranslator.hex(10));
        assertEquals("F", CharSequenceTranslator.hex(15));
        assertEquals("10", CharSequenceTranslator.hex(16));
        assertEquals("C13", CharSequenceTranslator.hex(3091));
    }
    
}
