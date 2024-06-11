import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcd";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        assertEquals(1, result.getLink(0).getPosOrig());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(4, result.getLink(0).getLen());
        assertEquals("e", result.getAbsolute());
        
        a = "abcd";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(1, result.getLink(0).getPosNew());
        assertEquals(4, result.getLink(0).getLen());
        assertEquals("", result.getAbsolute());
        
        a = "abcd";
        b = "efgh";
        result = StringIncrementor.diff(a, b);
        assertEquals(0, result.linksCount());
        assertEquals("efgh", result.getAbsolute());
    }
    
    @Test
    public void testDiffVerbose() {
        String a = "abcdefg";
        String b = "changedefgh";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(6, result.getLink(0).getPosNew());
        assertEquals(5, result.getLink(0).getLen());
        assertEquals("h", result.getAbsolute());
        
        a = "abcdefg";
        b = "abcdefg";
        result = StringIncrementor.diff(a, b, true);
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        String a = "abc";
        String b = "abcd";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(3, 4, 1));
        rs.setAbsolute("");
        String result = StringIncrementor.patch(a, rs);
        assertEquals("abcd", result);
        
        a = "abcdef";
        b = "abef";
        rs = new RelativeString();
        rs.addLink(new StringLink(2, 2, 4));
        rs.setAbsolute("cd");
        result = StringIncrementor.patch(a, rs);
        assertEquals("abefcd", result);
        
        a = "abc";
        b = "def";
        rs = new RelativeString();
        rs.setAbsolute("def");
        result = StringIncrementor.patch(a, rs);
        assertEquals("def", result);
    }
    
    @Test
    public void testPatchVerbose() {
        String a = "abc";
        String b = "abcd";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(3, 4, 1));
        rs.setAbsolute("");
        String result = StringIncrementor.patch(a, rs, true);
        assertEquals("abcd", result);
        
        a = "abcdef";
        b = "abef";
        rs = new RelativeString();
        rs.addLink(new StringLink(2, 2, 4));
        rs.setAbsolute("cd");
        result = StringIncrementor.patch(a, rs, true);
        assertEquals("abefcd", result);
        
        a = "abc";
        b = "def";
        rs = new RelativeString();
        rs.setAbsolute("def");
        result = StringIncrementor.patch(a, rs, true);
        assertEquals("def", result);
    }
    
}