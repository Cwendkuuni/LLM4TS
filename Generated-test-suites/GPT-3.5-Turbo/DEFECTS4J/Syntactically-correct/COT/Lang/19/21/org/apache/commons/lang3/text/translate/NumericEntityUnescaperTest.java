import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testTranslate_NoEntity_ReturnsZero() throws Exception {
        StringBuilder input = new StringBuilder("This is a test");
        int index = 0;
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate(input, index, output);
        
        assertEquals(0, result);
        assertEquals("This is a test", output.toString());
    }
    
    @Test
    public void testTranslate_ValidDecimalEntity_LessThan0xFFFF() throws Exception {
        StringBuilder input = new StringBuilder("This is a &#65; test");
        int index = 8;
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate(input, index, output);
        
        assertEquals(8, result);
        assertEquals("This is a A test", output.toString());
    }
    
    @Test
    public void testTranslate_ValidDecimalEntity_GreaterThan0xFFFF() throws Exception {
        StringBuilder input = new StringBuilder("This is a &#65536; test");
        int index = 8;
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate(input, index, output);
        
        assertEquals(12, result);
        assertEquals("This is a \uD800\uDC00 test", output.toString());
    }
    
    @Test
    public void testTranslate_ValidHexEntity_LessThan0xFFFF() throws Exception {
        StringBuilder input = new StringBuilder("This is a &#x41; test");
        int index = 8;
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate(input, index, output);
        
        assertEquals(10, result);
        assertEquals("This is a A test", output.toString());
    }
    
    @Test
    public void testTranslate_ValidHexEntity_GreaterThan0xFFFF() throws Exception {
        StringBuilder input = new StringBuilder("This is a &#x10000; test");
        int index = 8;
        StringBuilder output = new StringBuilder();
        
        int result = unescaper.translate(input, index, output);
        
        assertEquals(14, result);
        assertEquals("This is a \uD800\uDC00 test", output.toString());
    }
}