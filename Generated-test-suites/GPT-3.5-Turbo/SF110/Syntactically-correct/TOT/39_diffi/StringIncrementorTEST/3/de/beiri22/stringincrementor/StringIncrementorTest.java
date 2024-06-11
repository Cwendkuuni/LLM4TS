import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_typicalCase() {
        String a = "abcde";
        String b = "abxdef";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(2, result.getLinksCount());
        assertEquals(a.length(), result.getAbsolute().length());
    }
    
    @Test
    public void testDiff_emptyStrings() {
        String a = "";
        String b = "";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.getLinksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testDiff_caseSensitive() {
        String a = "abcdef";
        String b = "ABCDEF";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.getLinksCount());
        assertEquals(b.length(), result.getAbsolute().length());
    }
    
    @Test
    public void testDiff_sameString() {
        String a = "abcdef";
        String b = "abcdef";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.getLinksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testDiff_edgeCase() {
        String a = "abcdefghijk";
        String b = "xyzabcdefghijk";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(1, result.getLinksCount());
        assertEquals(a.length(), result.getAbsolute().length());
    }
    
    @Test
    public void testPatch_typicalCase() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 2));
        r.addLink(new StringLink(2, 3, 1));
        r.addLink(new StringLink(4, 5, 1));
        r.setAbsolute("f");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abcdef", result);
    }
    
    @Test
    public void testPatch_emptyRelativeString() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testPatch_emptyOriginalString() {
        String a = "";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.setAbsolute("a");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("a", result);
    }
    
    @Test
    public void testPatch_caseSensitive() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 2, 2));
        r.addLink(new StringLink(4, 3, 1));
        r.setAbsolute("F");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("aFde", result);
    }
    
    @Test
    public void testPatch_sameRelativeString() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 2));
        r.addLink(new StringLink(2, 3, 1));
        r.addLink(new StringLink(4, 5, 1));
        r.setAbsolute("f");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abcdef", result);
    }
}