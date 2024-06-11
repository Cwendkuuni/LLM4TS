
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Normal inputs
        String a1 = "abcabc";
        String b1 = "abc";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        assertEquals(1, result1.linksCount());
        assertEquals(0, result1.getLink(0).getPosOrig());
        assertEquals(0, result1.getLink(0).getPosNew());
        assertEquals(3, result1.getLink(0).getLen());
        assertEquals("cabc", result1.getAbsolute());

        // Test case 2: Empty string inputs
        String a2 = "";
        String b2 = "abc";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        assertEquals(0, result2.linksCount());
        assertEquals("", result2.getAbsolute());

        // Test case 3: Identical strings
        String a3 = "abc";
        String b3 = "abc";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        assertEquals(0, result3.linksCount());
        assertEquals("", result3.getAbsolute());

        // Test case 4: Strings with no common suffix
        String a4 = "abc";
        String b4 = "def";
        RelativeString result4 = StringIncrementor.diff(a4, b4);
        assertEquals(0, result4.linksCount());
        assertEquals("def", result4.getAbsolute());
    }

    @Test
    public void testDiffVerbose() {
        // Test case 1: Normal inputs with verbose output
        String a1 = "abcabc";
        String b1 = "abc";
        RelativeString result1 = StringIncrementor.diff(a1, b1, true);
        assertEquals(1, result1.linksCount());
        assertEquals(0, result1.getLink(0).getPosOrig());
        assertEquals(0, result1.getLink(0).getPosNew());
        assertEquals(3, result1.getLink(0).getLen());
        assertEquals("cabc", result1.getAbsolute());
    }

    @Test
    public void testPatch() {
        // Test case 1: Normal inputs
        String a1 = "abc";
        StringLink sl1 = new StringLink(0, 0, 3);
        RelativeString r1 = new RelativeString();
        r1.addLink(sl1);
        r1.setAbsolute("");
        String result1 = StringIncrementor.patch(a1, r1);
        assertEquals("abc", result1);

        // Test case 2: Empty string inputs
        String a2 = "";
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("abc");
        String result2 = StringIncrementor.patch(a2, r2);
        assertEquals("abc", result2);

        // Test case 3: No links
        String a3 = "abc";
        RelativeString r3 = new RelativeString();
        r3.setAbsolute("");
        String result3 = StringIncrementor.patch(a3, r3);
        assertEquals("abc", result3);

        // Test case 4: Multiple links
        String a4 = "abc";
        StringLink sl4a = new StringLink(0, 0, 1);
        StringLink sl4b = new StringLink(0, 1, 1);
        RelativeString r4 = new RelativeString();
        r4.addLink(sl4a);
        r4.addLink(sl4b);
        r4.setAbsolute("");
        String result4 = StringIncrementor.patch(a4, r4);
        assertEquals("abb", result4);
    }

    @Test
    public void testPatchVerbose() {
        // Test case 1: Normal inputs with verbose output
        String a1 = "abc";
        StringLink sl1 = new StringLink(0, 0, 3);
        RelativeString r1 = new RelativeString();
        r1.addLink(sl1);
        r1.setAbsolute("");
        String result1 = StringIncrementor.patch(a1, r1, true);
        assertEquals("abc", result1);
    }
}

