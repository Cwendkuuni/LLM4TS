package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        // Test case 1: Input sequence = "&#65;"
        CharSequence input1 = "&#65;";
        int index1 = 0;
        StringWriter out1 = new StringWriter();
        NumericEntityUnescaper translator1 = new NumericEntityUnescaper();
        int result1 = translator1.translate(input1, index1, out1);
        assertEquals("Result is not correct.", 5, result1);
        assertEquals("Output is not correct.", "A", out1.toString());
        
        // Test case 2: Input sequence = "&#x41;"
        CharSequence input2 = "&#x41;";
        int index2 = 0;
        StringWriter out2 = new StringWriter();
        NumericEntityUnescaper translator2 = new NumericEntityUnescaper();
        int result2 = translator2.translate(input2, index2, out2);
        assertEquals("Result is not correct.", 6, result2);
        assertEquals("Output is not correct.", "A", out2.toString());
        
        // Test case 3: Input sequence = "&#x20AC"
        CharSequence input3 = "&#x20AC";
        int index3 = 0;
        StringWriter out3 = new StringWriter();
        NumericEntityUnescaper translator3 = new NumericEntityUnescaper();
        int result3 = translator3.translate(input3, index3, out3);
        assertEquals("Result is not correct.", 6, result3);
        assertEquals("Output is not correct.", "€", out3.toString());
        
        // Test case 4: Input sequence = "&amp;"
        CharSequence input4 = "&amp;";
        int index4 = 0;
        StringWriter out4 = new StringWriter();
        NumericEntityUnescaper translator4 = new NumericEntityUnescaper();
        int result4 = translator4.translate(input4, index4, out4);
        assertEquals("Result is not correct.", 0, result4);
        assertEquals("Output is not correct.", "", out4.toString());
        
        // Test case 5: Input sequence = "&#128514;"
        CharSequence input5 = "&#128514;";
        int index5 = 0;
        StringWriter out5 = new StringWriter();
        NumericEntityUnescaper translator5 = new NumericEntityUnescaper();
        int result5 = translator5.translate(input5, index5, out5);
        assertEquals("Result is not correct.", 8, result5);
        assertEquals("Output is not correct.", "\uD83D\uDE02", out5.toString());
    }
}