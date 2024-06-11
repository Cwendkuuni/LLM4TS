import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result = StringIncrementor.diff("hello", "hallo");
        assertEquals(1, result.linksCount());
        
        // Edge case
        result = StringIncrementor.diff("hello", "world");
        assertEquals(0, result.linksCount());
        
        // Error scenario
        result = StringIncrementor.diff("hello", "");
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        // Typical use case
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 5));
        rs.setAbsolute("world");
        String result = StringIncrementor.patch("hello", rs);
        assertEquals("world", result);
        
        // Edge case
        rs = new RelativeString();
        result = StringIncrementor.patch("hello", rs);
        assertEquals("hello", result);
        
        // Error scenario
        rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 10)); // Link length exceeds original string length
        rs.setAbsolute("world");
        result = StringIncrementor.patch("hello", rs);
        assertEquals("hello", result);
    }
}