import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertNotNull(result);
        assertEquals(0, result.linksCount());
        assertEquals("def", result.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 3));
        rs.addLink(new StringLink(3, 3, 3));
        rs.setAbsolute("abcghi");
        
        String result = StringIncrementor.patch("abcdef", rs);
        assertNotNull(result);
        assertEquals("abcdefghi", result);
    }
}