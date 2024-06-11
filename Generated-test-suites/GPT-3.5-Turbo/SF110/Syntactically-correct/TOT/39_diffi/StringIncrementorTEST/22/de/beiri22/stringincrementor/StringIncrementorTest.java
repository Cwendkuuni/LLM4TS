import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiffCaseSensitive() {
        String a = "Hello World";
        String b = "hello world";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }
    
    @Test
    public void testDiffSameStrings() {
        String a = "Hello World";
        String b = "Hello World";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }
    
    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }
    
    @Test
    public void testDiffOneEmptyString() {
        String a = "";
        String b = "Hello";
        RelativeString result = StringIncrementor.diff(a, b);
        RelativeString expected = new RelativeString();
        expected.setAbsolute(b);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiffOneCharDifferent() {
        String a = "Hello World";
        String b = "Hello Wurld";
        RelativeString result = StringIncrementor.diff(a, b);
        RelativeString expected = new RelativeString();
        expected.setAbsolute(" Wurld");
        expected.addLink(new StringLink(6, 6, 6));
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatchEmptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }
    
    @Test
    public void testPatchNoLinks() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.setAbsolute("Hello World");
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }
    
    @Test
    public void testPatchOneLink() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.setAbsolute(" wr");
        r.addLink(new StringLink(6, 6, 2));
        String result = StringIncrementor.patch(a, r);
        assertEquals("Hello wrld", result);
    }
    
    @Test
    public void testPatchMultipleLinks() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.setAbsolute("goode!");
        r.addLink(new StringLink(0, 0, 4));
        r.addLink(new StringLink(6, 6, 2));
        r.addLink(new StringLink(11, 11, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("goodeHeld!", result);
    }
    
}