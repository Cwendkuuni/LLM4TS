import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void diff_test() {
        // Test 1: Typical use case where both string inputs are equal
        String a = "abc";
        String b = "abc";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));

        // Test 2: Edge case where the first string input is empty
        a = "";
        b = "abc";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));

        // Test 3: Edge case where the second string input is empty
        a = "abc";
        b = "";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));

        // Test 4: Typical use case where the second string is an extension of the first string
        a = "abc";
        b = "abcdef";
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 3, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));

        // Test 5: Error scenario where the second string does not contain any characters from the first string
        a = "abc";
        b = "def";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void patch_test() {
        // Test 1: Typical use case where the relative string is empty
        String a = "abc";
        RelativeString r = new RelativeString();
        assertEquals(a, StringIncrementor.patch(a, r));

        // Test 2: Edge case where the relative string is not empty but does not contain any links
        a = "abc";
        r = new RelativeString();
        r.setAbsolute("def");
        assertEquals(a + "def", StringIncrementor.patch(a, r));

        // Test 3: Typical use case where the relative string contains links
        a = "123456";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        assertEquals("abc456", StringIncrementor.patch(a, r));

        // Test 4: Error scenario where the relative string contains links referring to invalid positions in the original string
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(3, 6, 3));
        assertEquals("abc", StringIncrementor.patch(a, r));

        // Test 5: Typical use case where the verbose option is enabled
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        String expected = "Adding 0 absolute Bytes from pos#0\n"
                + "Adding Link: [0,0,3]";
        assertEquals("abc", StringIncrementor.patch(a, r, true));
    }
}