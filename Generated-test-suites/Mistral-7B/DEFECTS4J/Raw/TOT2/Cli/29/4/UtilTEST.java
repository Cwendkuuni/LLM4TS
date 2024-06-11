import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest
{
    @Test
    void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(str));
        
        str = "test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(str));
        
        str = "---test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(str));
        
        str = "";
        assertEquals(null, Util.stripLeadingHyphens(str));
    }
}