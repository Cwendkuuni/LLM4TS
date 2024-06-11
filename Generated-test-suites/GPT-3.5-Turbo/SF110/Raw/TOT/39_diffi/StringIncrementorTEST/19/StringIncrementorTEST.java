import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    // Test for diff method with two strings
    @Test
    public void testDiffWithTwoStrings() {
        String a = "abc";
        String b = "def";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("def");
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }
    
    // Test for diff method with two strings and verbose mode
    @Test
    public void testDiffWithTwoStringsAndVerboseMode() {
        String a = "abc";
        String b = "def";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("def");
        RelativeString actual = StringIncrementor.diff(a, b, true);
        assertEquals(expected, actual);
    }

    // Test for diff method with empty strings
    @Test
    public void testDiffWithEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    // Test for diff method with null strings
    @Test
    public void testDiffWithNullStrings() {
        String a = null;
        String b = null;
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }
    
    // Test for patch method with original string and relative string
    @Test
    public void testPatchWithOriginalStringAndRelativeString() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        String expected = "def";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }
    
    // Test for patch method with original string, relative string, and verbose mode
    @Test
    public void testPatchWithOriginalStringRelativeStringAndVerboseMode() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        String expected = "def";
        String actual = StringIncrementor.patch(a, r, true);
        assertEquals(expected, actual);
    }

    // Test for patch method with empty original string and relative string
    @Test
    public void testPatchWithEmptyOriginalStringAndRelativeString() {
        String a = "";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        String expected = "";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    // Test for patch method with null original string and relative string
    @Test
    public void testPatchWithNullOriginalStringAndRelativeString() {
        String a = null;
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        String expected = "";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

}