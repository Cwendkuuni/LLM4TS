
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_WithEmptyStrings_ReturnsEmptyRelativeString() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.getLinks().isEmpty());
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.getLength());
    }

    @Test
    public void testDiff_WithSameStrings_ReturnsEmptyRelativeString() {
        String a = "abc";
        String b = "abc";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.getLinks().isEmpty());
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.getLength());
    }

    @Test
    public void testDiff_WithStringIncreasedAtIndexZero_ReturnsNonEmptyRelativeString() {
        String a = "abc";
        String b = "dbc";
        RelativeString result = StringIncrementor.diff(a, b);
        assertFalse(result.getLinks().isEmpty());
        assertEquals("d", result.getAbsolute());
        assertEquals(1, result.getLength());
    }

    @Test
    public void testDiff_WithStringAddedInMiddle_ReturnsNonEmptyRelativeString() {
        String a = "abc";
        String b = "abxc";
        RelativeString result = StringIncrementor.diff(a, b);
        assertFalse(result.getLinks().isEmpty());
        assertEquals("x", result.getAbsolute());
        assertEquals(1, result.getLength());
    }

    @Test
    public void testPatch_WithEmptyString_ReturnsOriginalString() {
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }

    @Test
    public void testPatch_WithNonEmptyRelativeString_ReturnsPatchedString() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.setAbsolute("x");
        String result = StringIncrementor.patch(a, r);
        assertEquals("xbc", result);
    }

    @Test
    public void testPatch_WithDifferentPosNewWithinRelativeString_ReturnsPatchedString() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 1, 1));
        r.setAbsolute("x");
        String result = StringIncrementor.patch(a, r);
        assertEquals("axc", result);
    }

    @Test
    public void testPatch_WithDifferentLengthWithinRelativeString_ReturnsPatchedString() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 2));
        r.setAbsolute("x");
        String result = StringIncrementor.patch(a, r);
        assertEquals("xac", result);
    }
}
