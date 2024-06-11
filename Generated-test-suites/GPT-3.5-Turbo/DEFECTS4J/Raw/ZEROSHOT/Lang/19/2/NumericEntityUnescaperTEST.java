import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.CharArrayWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test with numeric entity "&#65;"
        CharSequence input1 = "&#65;";
        Writer out1 = new CharArrayWriter();
        int result1 = unescaper.translate(input1, 0, out1);
        assertEquals(6, result1);
        assertEquals("A", out1.toString());
        
        // Test with hex numeric entity "&#x41;"
        CharSequence input2 = "&#x41;";
        Writer out2 = new CharArrayWriter();
        int result2 = unescaper.translate(input2, 0, out2);
        assertEquals(7, result2);
        assertEquals("A", out2.toString());
        
        // Test with numeric entity "&#8364;"
        CharSequence input3 = "&#8364;";
        Writer out3 = new CharArrayWriter();
        int result3 = unescaper.translate(input3, 0, out3);
        assertEquals(8, result3);
        assertEquals("€", out3.toString());
    }
}