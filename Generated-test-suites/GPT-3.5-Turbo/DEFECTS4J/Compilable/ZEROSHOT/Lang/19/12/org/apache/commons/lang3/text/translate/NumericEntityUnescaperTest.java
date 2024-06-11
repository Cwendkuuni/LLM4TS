package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testTranslate() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                stringBuffer.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        };
        
        // Test case 1: Numeric entity with decimal value
        CharSequence input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(4, result1);
        assertTrue(stringBuffer.toString().equals("A"));
        
        // Test case 2: Numeric entity with hex value
        CharSequence input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(4, result2);
        assertTrue(stringBuffer.toString().equals("A"));
        
        // Test case 3: Numeric entity without semicolon
        CharSequence input3 = "&#65";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(4, result3);
        assertTrue(stringBuffer.toString().equals("A"));
        
        // Test case 4: Numeric entity with value greater than 0xFFFF
        CharSequence input4 = "&#65536;";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer);
        assertEquals(9, result4);
        assertTrue(stringBuffer.toString().equals("𐀀"));
        
        // Test case 5: Numeric entity with invalid format
        CharSequence input5 = "&#XYZ;";
        int index5 = 0;
        int result5 = unescaper.translate(input5, index5, writer);
        assertEquals(0, result5);
        
    }
}