import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1
        String a1 = "abcd";
        String b1 = "defg";
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(0, 0, 0));
        expected1.setAbsolute("defg");
        RelativeString actual1 = StringIncrementor.diff(a1, b1);
        assertEquals(expected1, actual1);

        // Test case 2
        String a2 = "abcde";
        String b2 = "acbde";
        RelativeString expected2 = new RelativeString();
        expected2.addLink(new StringLink(0, 0, 1));
        expected2.addLink(new StringLink(2, 2, 1));
        expected2.setAbsolute("abd");
        RelativeString actual2 = StringIncrementor.diff(a2, b2);
        assertEquals(expected2, actual2);

        // Test case 3
        String a3 = "abcde";
        String b3 = "abdde";
        RelativeString expected3 = new RelativeString();
        expected3.addLink(new StringLink(0, 0, 2));
        expected3.addLink(new StringLink(3, 3, 1));
        expected3.setAbsolute("e");
        RelativeString actual3 = StringIncrementor.diff(a3, b3);
        assertEquals(expected3, actual3);

        // Test case 4
        String a4 = "abcdef";
        String b4 = "abcdef";
        RelativeString expected4 = new RelativeString();
        expected4.setAbsolute("");
        RelativeString actual4 = StringIncrementor.diff(a4, b4);
        assertEquals(expected4, actual4);
    }

    @Test
    public void testPatch() {
        // Test case 1
        String a1 = "abcd";
        RelativeString r1 = new RelativeString();
        r1.setAbsolute("defg");
        String expected1 = "defg";
        String actual1 = StringIncrementor.patch(a1, r1);
        assertEquals(expected1, actual1);

        // Test case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(0, 0, 1));
        r2.addLink(new StringLink(2, 2, 1));
        r2.setAbsolute("abd");
        String expected2 = "acbde";
        String actual2 = StringIncrementor.patch(a2, r2);
        assertEquals(expected2, actual2);

        // Test case 3
        String a3 = "abcde";
        RelativeString r3 = new RelativeString();
        r3.addLink(new StringLink(0, 0, 2));
        r3.addLink(new StringLink(3, 3, 1));
        r3.setAbsolute("e");
        String expected3 = "abdde";
        String actual3 = StringIncrementor.patch(a3, r3);
        assertEquals(expected3, actual3);

        // Test case 4
        String a4 = "abcdef";
        RelativeString r4 = new RelativeString();
        r4.setAbsolute("");
        String expected4 = "abcdef";
        String actual4 = StringIncrementor.patch(a4, r4);
        assertEquals(expected4, actual4);
    }

}