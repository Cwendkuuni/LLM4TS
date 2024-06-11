package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test numeric entity without semicolon
        StringBuilder input1 = new StringBuilder();
        input1.append("&amp");
        StringBuilder output1 = new StringBuilder();
        Writer writer1 = new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
                output1.append(cbuf, off, len);
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
        };
        assertEquals(1, unescaper.translate(input1, 0, writer1));
        assertEquals("&amp", output1.toString());
        
        // Test numeric entity with semicolon
        StringBuilder input2 = new StringBuilder();
        input2.append("&#65;");
        StringBuilder output2 = new StringBuilder();
        Writer writer2 = new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
                output2.append(cbuf, off, len);
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
        };
        assertEquals(5, unescaper.translate(input2, 0, writer2));
        assertEquals("A", output2.toString());
        
        // Test numeric entity with hexadecimal value and semicolon
        StringBuilder input3 = new StringBuilder();
        input3.append("&#x1F4A9;");
        StringBuilder output3 = new StringBuilder();
        Writer writer3 = new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
                output3.append(cbuf, off, len);
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
        };
        assertEquals(9, unescaper.translate(input3, 0, writer3));
        assertEquals("\uD83D\uDCA9", output3.toString());
        
        // Test invalid numeric entity
        StringBuilder input4 = new StringBuilder();
        input4.append("&#");
        StringBuilder output4 = new StringBuilder();
        Writer writer4 = new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
                output4.append(cbuf, off, len);
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
        };
        assertEquals(0, unescaper.translate(input4, 0, writer4));
        assertEquals("", output4.toString());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test OPTION.semiColonRequired
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        
        // Test OPTION.semiColonOptional
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));

        // Test OPTION.errorIfNoSemiColon
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        // Test OPTION with different options
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
}