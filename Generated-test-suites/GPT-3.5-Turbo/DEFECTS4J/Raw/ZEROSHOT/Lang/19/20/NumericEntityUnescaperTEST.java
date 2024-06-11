import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper numericEntityUnescaper;
    
    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_withValidInput_shouldTranslateToCodepoint() throws IOException {
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withValidHexInput_shouldTranslateToCodepoint() throws IOException {
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withInvalidInput_shouldNotTranslate() throws IOException {
        String input = "&Invalid;";
        StringWriter writer = new StringWriter();
        
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }
    
    @Test
    public void testTranslate_withInvalidHexInput_shouldNotTranslate() throws IOException {
        String input = "&#xInvalid;";
        StringWriter writer = new StringWriter();
        
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }
}