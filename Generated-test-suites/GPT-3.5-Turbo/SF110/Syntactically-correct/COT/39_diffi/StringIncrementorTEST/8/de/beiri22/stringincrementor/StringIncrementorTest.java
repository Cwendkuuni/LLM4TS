
import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Both strings are empty
        String a1 = "";
        String b1 = "";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        RelativeString expected1 = new RelativeString();
        assertEquals(expected1, result1);

        // Test case 2: String a is empty but string b is not
        String a2 = "";
        String b2 = "abc";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        RelativeString expected2 = new RelativeString();
        expected2.setAbsolute("abc");
        assertEquals(expected2, result2);

        // Test case 3: Both strings are not empty and have common links
        String a3 = "abcdef";
        String b3 = "axcdxe";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        RelativeString expected3 = new RelativeString();
        StringLink link3 = new StringLink(1, 1, 1);
        expected3.addLink(link3);
        StringLink link4 = new StringLink(3, 3, 1);
        expected3.addLink(link4);
        StringLink link5 = new StringLink(5, 5, 1);
        expected3.addLink(link5);
        expected3.setAbsolute("axex");
        assertEquals(expected3, result3);
    }

    @Test
    public void testDiffVerbose() {
        // Test case: Verbose mode
        String a = "abcdef";
        String b = "axcdxe";
        boolean verbose = true;
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        RelativeString expected = new RelativeString();
        StringLink link1 = new StringLink(1, 1, 1);
        expected.addLink(link1);
        StringLink link2 = new StringLink(3, 3, 1);
        expected.addLink(link2);
        StringLink link3 = new StringLink(5, 5, 1);
        expected.addLink(link3);
        expected.setAbsolute("axex");
        assertEquals(expected, result);
    }

    @Test
    public void testPatch() {
        // Test case 1: RelativeString is empty
        String a1 = "abcdef";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1);
        String expected1 = "abcdef";
        assertEquals(expected1, result1);

        // Test case 2: RelativeString has links
        String a2 = "abcdef";
        RelativeString r2 = new RelativeString();
        StringLink link4 = new StringLink(1, 1, 1);
        r2.addLink(link4);
        StringLink link5 = new StringLink(3, 3, 1);
        r2.addLink(link5);
        StringLink link6 = new StringLink(5, 5, 1);
        r2.addLink(link6);
        r2.setAbsolute("axex");
        String result2 = StringIncrementor.patch(a2, r2);
        String expected2 = "axcdxe";
        assertEquals(expected2, result2);
    }

    @Test
    public void testPatchVerbose() {
        // Test case: Verbose mode
        String a = "abcdef";
        RelativeString r = new RelativeString();
        StringLink link1 = new StringLink(1, 1, 1);
        r.addLink(link1);
        StringLink link2 = new StringLink(3, 3, 1);
        r.addLink(link2);
        StringLink link3 = new StringLink(5, 5, 1);
        r.addLink(link3);
        r.setAbsolute("axex");
        boolean verbose = true;
        String result = StringIncrementor.patch(a, r, verbose);
        String expected = "axcdxe";
        assertEquals(expected, result);
    }

}

