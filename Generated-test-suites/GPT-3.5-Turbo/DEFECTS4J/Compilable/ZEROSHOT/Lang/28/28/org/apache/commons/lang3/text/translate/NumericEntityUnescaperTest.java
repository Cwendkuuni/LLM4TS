package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test case 1: Numeric entity in decimal format
        String input1 = "&#65;";
        StringWriter writer1 = new StringWriter();
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer1);
        assertEquals(4, result1);
        assertEquals('A', writer1.toString().charAt(0));
        
        // Test case 2: Numeric entity in hexadecimal format
        String input2 = "&#x41;";
        StringWriter writer2 = new StringWriter();
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer2);
        assertEquals(6, result2);
        assertEquals('A', writer2.toString().charAt(0));
        
        // Test case 3: Invalid input - no numeric entity
        String input3 = "ABC";
        StringWriter writer3 = new StringWriter();
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer3);
        assertEquals(0, result3);
        assertEquals("", writer3.toString());
        
        // Test case 4: Invalid input - incomplete numeric entity
        String input4 = "&#65";
        StringWriter writer4 = new StringWriter();
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer4);
        assertEquals(0, result4);
        assertEquals("", writer4.toString());
    }
}