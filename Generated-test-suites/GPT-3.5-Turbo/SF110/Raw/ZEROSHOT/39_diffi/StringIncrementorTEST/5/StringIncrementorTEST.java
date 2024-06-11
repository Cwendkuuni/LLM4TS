import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1
        RelativeString result1 = StringIncrementor.diff("abc", "def");
        RelativeString expected1 = new RelativeString();
        expected1.setAbsolute("def");
        assertEquals(expected1, result1);

        // Test case 2
        RelativeString result2 = StringIncrementor.diff("abc", "abc");
        RelativeString expected2 = new RelativeString();
        expected2.setAbsolute("");
        assertEquals(expected2, result2);

        // Test case 3
        RelativeString result3 = StringIncrementor.diff("abc", "abdef");
        RelativeString expected3 = new RelativeString();
        expected3.setAbsolute("def");
        StringLink link3 = new StringLink(0, 2, 3);
        expected3.addLink(link3);
        assertEquals(expected3, result3);

        // Test case 4
        RelativeString result4 = StringIncrementor.diff("abc", "bcdefg");
        RelativeString expected4 = new RelativeString();
        expected4.setAbsolute("defg");
        StringLink link4 = new StringLink(0, 1, 2);
        expected4.addLink(link4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testDiffWithVerbose() {
        // Test case 1
        RelativeString result1 = StringIncrementor.diff("abc", "def", true);
        RelativeString expected1 = new RelativeString();
        expected1.setAbsolute("def");
        assertEquals(expected1, result1);

        // Test case 2
        RelativeString result2 = StringIncrementor.diff("abc", "abc", true);
        RelativeString expected2 = new RelativeString();
        expected2.setAbsolute("");
        assertEquals(expected2, result2);

        // Test case 3
        RelativeString result3 = StringIncrementor.diff("abc", "abdef", true);
        RelativeString expected3 = new RelativeString();
        expected3.setAbsolute("def");
        StringLink link3 = new StringLink(0, 2, 3);
        expected3.addLink(link3);
        assertEquals(expected3, result3);

        // Test case 4
        RelativeString result4 = StringIncrementor.diff("abc", "bcdefg", true);
        RelativeString expected4 = new RelativeString();
        expected4.setAbsolute("defg");
        StringLink link4 = new StringLink(0, 1, 2);
        expected4.addLink(link4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testPatch() {
        // Test case 1
        RelativeString r1 = new RelativeString();
        r1.setAbsolute("def");
        StringLink link1 = new StringLink(0, 2, 3);
        r1.addLink(link1);
        String result1 = StringIncrementor.patch("abc", r1);
        assertEquals("abdefc", result1);

        // Test case 2
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("");
        String result2 = StringIncrementor.patch("abc", r2);
        assertEquals("abc", result2);

        // Test case 3
        RelativeString r3 = new RelativeString();
        r3.setAbsolute("defg");
        StringLink link3 = new StringLink(0, 1, 2);
        r3.addLink(link3);
        String result3 = StringIncrementor.patch("abc", r3);
        assertEquals("bcdefg", result3);
    }

    @Test
    public void testPatchWithVerbose() {
        // Test case 1
        RelativeString r1 = new RelativeString();
        r1.setAbsolute("def");
        StringLink link1 = new StringLink(0, 2, 3);
        r1.addLink(link1);
        String result1 = StringIncrementor.patch("abc", r1, true);
        assertEquals("abdefc", result1);

        // Test case 2
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("");
        String result2 = StringIncrementor.patch("abc", r2, true);
        assertEquals("abc", result2);

        // Test case 3
        RelativeString r3 = new RelativeString();
        r3.setAbsolute("defg");
        StringLink link3 = new StringLink(0, 1, 2);
        r3.addLink(link3);
        String result3 = StringIncrementor.patch("abc", r3, true);
        assertEquals("bcdefg", result3);
    }
}