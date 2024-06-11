import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Verbose is false and both strings match
        String a1 = "abcd";
        String b1 = "abcd";
        RelativeString result1 = StringIncrementor.diff(a1, b1, false);
        assertEquals(result1.getLength(), 0);
        assertEquals(result1.getAbsolute(), "");

        // Test case 2: Verbose is true and a is empty
        String a2 = "";
        String b2 = "xyz";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        assertEquals(result2.getLength(), 3);
        assertEquals(result2.getAbsolute(), "xyz");

        // Test case 3: Verbose is true and b is empty
        String a3 = "abc";
        String b3 = "";
        RelativeString result3 = StringIncrementor.diff(a3, b3, true);
        assertEquals(result3.getLength(), 0);
        assertEquals(result3.getAbsolute(), "abc");

        // Test case 4: No common substring between a and b
        String a4 = "mno";
        String b4 = "xyz";
        RelativeString result4 = StringIncrementor.diff(a4, b4);
        assertEquals(result4.getLength(), 0);
        assertEquals(result4.getAbsolute(), "xyz");

        // Test case 5: a is longer than b and b is a substring of a
        String a5 = "abcdefg";
        String b5 = "bcde";
        RelativeString result5 = StringIncrementor.diff(a5, b5);
        assertEquals(result5.getLength(), 5);
        assertEquals(result5.getAbsolute(), "a");

        // Test case 6: a is shorter than b and a is a substring of b
        String a6 = "ghij";
        String b6 = "abcdefg";
        RelativeString result6 = StringIncrementor.diff(a6, b6);
        assertEquals(result6.getLength(), 4);
        assertEquals(result6.getAbsolute(), "abcdefg");

        // Test case 7: a is longer than b and b is partially contained in a
        String a7 = "abcxyz1234";
        String b7 = "xyz";
        RelativeString result7 = StringIncrementor.diff(a7, b7);
        assertEquals(result7.getLength(), 7);
        assertEquals(result7.getAbsolute(), "abc1234");

        // Test case 8: Both a and b are equal but verbose is true
        String a8 = "abcdefgh";
        String b8 = "abcdefgh";
        RelativeString result8 = StringIncrementor.diff(a8, b8, true);
        assertEquals(result8.getLength(), 0);
        assertEquals(result8.getAbsolute(), "");

        // Test case 9: Verbose is true and no match found between a and b
        String a9 = "pqr";
        String b9 = "xyz";
        RelativeString result9 = StringIncrementor.diff(a9, b9, true);
        assertEquals(result9.getLength(), 0);
        assertEquals(result9.getAbsolute(), "pqr");
    }

    @Test
    public void testPatch() {
        // Test case 1: Verbose is false and there are no links
        String a1 = "abcd";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1, false);
        assertEquals(result1, "abcd");

        // Test case 2: Verbose is true and the patch is empty
        String a2 = "abcd";
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("");
        String result2 = StringIncrementor.patch(a2, r2, true);
        assertEquals(result2, "abcd");

        // Test case 3: Verbose is true and there are links present
        String a3 = "abcdefgh";
        RelativeString r3 = new RelativeString();
        StringLink link1 = new StringLink(0, 4, 4);
        StringLink link2 = new StringLink(4, 8, 4);
        r3.addLink(link1);
        r3.addLink(link2);
        String result3 = StringIncrementor.patch(a3, r3, true);
        assertEquals(result3, "abcdabcdefgh");

        // Test case 4: Verbose is true and absolute string is empty
        String a4 = "abcd";
        RelativeString r4 = new RelativeString();
        r4.setAbsolute("");
        StringLink link3 = new StringLink(0, 1, 1);
        r4.addLink(link3);
        String result4 = StringIncrementor.patch(a4, r4, true);
        assertEquals(result4, "b");

        // Test case 5: Verbose is true and all characters in result are from absolute string
        String a5 = "abcd";
        RelativeString r5 = new RelativeString();
        r5.setAbsolute("xyz");
        StringLink link4 = new StringLink(0, 1, 1);
        r5.addLink(link4);
        String result5 = StringIncrementor.patch(a5, r5, true);
        assertEquals(result5, "xy");

        // Test case 6: Verbose is true and link points to non-existent index in original string
        String a6 = "abcd";
        RelativeString r6 = new RelativeString();
        StringLink link5 = new StringLink(5, 6, 1);
        r6.addLink(link5);
        String result6 = StringIncrementor.patch(a6, r6, true);
        assertEquals(result6, "abcd");

        // Test case 7: Verbose is true and link has negative length
        String a7 = "abcdefgh";
        RelativeString r7 = new RelativeString();
        StringLink link6 = new StringLink(0, 4, -4);
        r7.addLink(link6);
        String result7 = StringIncrementor.patch(a7, r7, true);
        assertEquals(result7, "abcdefgh");

        // Test case 8: Verbose is true and absolute string contains extra characters
        String a8 = "abcd";
        RelativeString r8 = new RelativeString();
        r8.setAbsolute("xyz");
        StringLink link7 = new StringLink(0, 1, 1);
        r8.addLink(link7);
        String result8 = StringIncrementor.patch(a8, r8, true);
        assertEquals(result8, "xy");
    }
}