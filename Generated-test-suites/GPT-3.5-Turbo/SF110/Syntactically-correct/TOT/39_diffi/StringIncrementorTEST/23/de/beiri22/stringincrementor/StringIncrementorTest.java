import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result1 = StringIncrementor.diff("abcd", "abcd");
        assertTrue(result1.getLinks().isEmpty());
        assertEquals("", result1.getAbsolute());

        // Edge case: a and b are empty strings
        RelativeString result2 = StringIncrementor.diff("", "");
        assertTrue(result2.getLinks().isEmpty());
        assertEquals("", result2.getAbsolute());

        // Error scenario: b is empty string
        RelativeString result3 = StringIncrementor.diff("abcd", "");
        assertTrue(result3.getLinks().isEmpty());
        assertEquals("abcd", result3.getAbsolute());

        // Error scenario: a is empty string
        RelativeString result4 = StringIncrementor.diff("", "abcd");
        assertTrue(result4.getLinks().isEmpty());
        assertEquals("", result4.getAbsolute());

        // Error scenario: a is null
        RelativeString result5 = StringIncrementor.diff(null, "abcd");
        assertNull(result5);

        // Error scenario: b is null
        RelativeString result6 = StringIncrementor.diff("abcd", null);
        assertNull(result6);

        // Error scenario: a and b are null
        RelativeString result7 = StringIncrementor.diff(null, null);
        assertNull(result7);
    }

    @Test
    public void testPatch() {
        // Typical use case
        RelativeString r1 = new RelativeString();
        StringLink link1 = new StringLink(0, 0, 4);
        r1.addLink(link1);
        r1.setAbsolute("");
        String result1 = StringIncrementor.patch("abcd", r1);
        assertEquals("abcd", result1);

        // Edge case: r is empty
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("");
        String result2 = StringIncrementor.patch("abcd", r2);
        assertEquals("abcd", result2);

        // Error scenario: a is empty string
        RelativeString r3 = new RelativeString();
        StringLink link3 = new StringLink(0, 0, 4);
        r3.addLink(link3);
        r3.setAbsolute("");
        String result3 = StringIncrementor.patch("", r3);
        assertEquals("abcd", result3);

        // Error scenario: r is null
        String result4 = StringIncrementor.patch("abcd", null);
        assertNull(result4);

        // Error scenario: a is null
        RelativeString r5 = new RelativeString();
        StringLink link5 = new StringLink(0, 0, 4);
        r5.addLink(link5);
        r5.setAbsolute("");
        String result5 = StringIncrementor.patch(null, r5);
        assertNull(result5);

        // Error scenario: a and r are null
        String result6 = StringIncrementor.patch(null, null);
        assertNull(result6);
    }
}