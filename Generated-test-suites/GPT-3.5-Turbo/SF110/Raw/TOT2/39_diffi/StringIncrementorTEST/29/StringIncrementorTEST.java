import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result = StringIncrementor.diff("abcdef", "abcxyz");
        assertEquals(1, result.linksCount());
        
        // Edge case
        result = StringIncrementor.diff("abcdef", "abcdef", true);
        assertEquals(0, result.linksCount());
        
        // Error scenario
        result = StringIncrementor.diff("abcdef", "123");
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch() {
        // Typical use case
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 3));
        rs.setAbsolute("xyz");
        String result = StringIncrementor.patch("abcdef", rs);
        assertEquals("xyzdef", result);
        
        // Edge case
        rs = new RelativeString();
        rs.setAbsolute("xyz");
        result = StringIncrementor.patch("abcdef", rs, true);
        assertEquals("xyz", result);
        
        // Error scenario
        rs = new RelativeString();
        result = StringIncrementor.patch("abcdef", rs);
        assertEquals("abcdef", result);
    }
}