import org.apache.commons.cli.*;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test for hyphens at the beginning
        assertEquals("example", Util.stripLeadingHyphens("-example"));
        assertEquals("example", Util.stripLeadingHyphens("--example"));
        assertEquals("example", Util.stripLeadingHyphens("---example"));
        
        // Test for no hyphens at the beginning
        assertEquals("example", Util.stripLeadingHyphens("example"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test for quotes at the beginning and end
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("\"example\""));
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("\"\"example\"\""));
        
        // Test for single quotes at the beginning and end
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("'example'"));
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("''example''"));
        
        // Test for no quotes at the beginning and end
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("example"));
    }
}