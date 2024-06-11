import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilterWithNullMessage() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = HTMLFilter.filter(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testFilterWithEmptyMessage() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = HTMLFilter.filter(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testFilterWithSpecialCharacters() {
        String input = "<body>Hello &amp; \"World\"</body>";
        String expectedOutput = "&lt;body&gt;Hello &amp;amp; &quot;World&quot;&lt;/body&gt;";
        String actualOutput = HTMLFilter.filter(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Add more test cases for additional scenarios
    
}