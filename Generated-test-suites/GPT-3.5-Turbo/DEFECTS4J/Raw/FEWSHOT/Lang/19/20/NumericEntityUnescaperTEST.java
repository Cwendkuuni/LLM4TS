package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test case 1
        String input1 = "This is a &#65; test";
        unescaper.translate(input1, 10, writer);
        assertEquals("This is a A test", writer.toString());
        
        // Test case 2
        String input2 = "This is a &#x41; test";
        unescaper.translate(input2, 10, writer);
        assertEquals("This is a A test", writer.toString());
        
        // Test case 3
        String input3 = "This is a &#65 test";
        unescaper.translate(input3, 10, writer);
        assertEquals("This is a A test", writer.toString());
        
        // Test case 4
        String input4 = "This is a &#x41 test";
        unescaper.translate(input4, 10, writer);
        assertEquals("This is a A test", writer.toString());
        
        // Test case 5
        String input5 = "This is a &#65";
        unescaper.translate(input5, 10, writer);
        assertEquals("This is a A", writer.toString());
        
        // Test case 6
        String input6 = "This is a &#x41";
        unescaper.translate(input6, 10, writer);
        assertEquals("This is a A", writer.toString());
        
        // Test case 7
        String input7 = "This is a &amp#65; test";
        unescaper.translate(input7, 11, writer);
        assertEquals("This is a &ampA test", writer.toString());
        
        // Test case 8
        String input8 = "This is a &amp#x41; test";
        unescaper.translate(input8, 11, writer);
        assertEquals("This is a &ampA test", writer.toString());
        
        // Test case 9
        String input9 = "This is a &amp#65 test";
        unescaper.translate(input9, 11, writer);
        assertEquals("This is a &ampA test", writer.toString());
        
        // Test case 10
        String input10 = "This is a &amp#x41 test";
        unescaper.translate(input10, 11, writer);
        assertEquals("This is a &ampA test", writer.toString());
        
        // Test case 11
        String input11 = "This is a &amp#65";
        unescaper.translate(input11, 11, writer);
        assertEquals("This is a &ampA", writer.toString());
        
        // Test case 12
        String input12 = "This is a &amp#x41";
        unescaper.translate(input12, 11, writer);
        assertEquals("This is a &ampA", writer.toString());
    }
}