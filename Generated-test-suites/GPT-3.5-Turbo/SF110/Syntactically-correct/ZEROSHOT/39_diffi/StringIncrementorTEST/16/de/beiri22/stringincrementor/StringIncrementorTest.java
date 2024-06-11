import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test diff method with different inputs
        RelativeString rs1 = StringIncrementor.diff("abcd", "abcd");
        assertEquals("", rs1.getAbsolute());
        assertEquals(0, rs1.linksCount());

        RelativeString rs2 = StringIncrementor.diff("abcd", "abcdef");
        assertEquals("ef", rs2.getAbsolute());
        assertEquals(1, rs2.linksCount());
        assertEquals("0, 4, 2", rs2.getLink(0).toString());

        RelativeString rs3 = StringIncrementor.diff("abcdef", "abcd");
        assertEquals("", rs3.getAbsolute());
        assertEquals(1, rs3.linksCount());
        assertEquals("4, 0, 2", rs3.getLink(0).toString());
    }

    @Test
    public void testDiffVerbose() {
        // Test diff method with verbose set to true
        RelativeString rs1 = StringIncrementor.diff("abcd", "abcdef", true);
        assertEquals("ef", rs1.getAbsolute());
        assertEquals(1, rs1.linksCount());
        assertEquals("0, 4, 2", rs1.getLink(0).toString());
    }

    @Test
    public void testPatch() {
        // Test patch method with different inputs
        String result1 = StringIncrementor.patch("abcd", new RelativeString());
        assertEquals("abcd", result1);

        RelativeString rs2 = new RelativeString();
        rs2.addLink(new StringLink(0, 4, 2));
        String result2 = StringIncrementor.patch("abcd", rs2);
        assertEquals("abcdef", result2);

        RelativeString rs3 = new RelativeString();
        rs3.addLink(new StringLink(4, 0, 2));
        String result3 = StringIncrementor.patch("abcdef", rs3);
        assertEquals("abcd", result3);
    }

    @Test
    public void testPatchVerbose() {
        // Test patch method with verbose set to true
        RelativeString rs1 = new RelativeString();
        rs1.addLink(new StringLink(0, 4, 2));
        String result1 = StringIncrementor.patch("abcd", rs1, true);
        assertEquals("abcdef", result1);
    }
}