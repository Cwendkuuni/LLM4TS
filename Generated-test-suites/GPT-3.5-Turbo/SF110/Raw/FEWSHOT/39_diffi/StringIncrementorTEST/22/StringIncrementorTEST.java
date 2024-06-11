import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcde";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "";
        b = "";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abcde";
        b = "fghij";
        expected = new RelativeString();
        expected.setAbsolute("fghij");
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abcde";
        b = "abcdefghij";
        expected = new RelativeString();
        StringLink link = new StringLink(0, 0, 5);
        expected.addLink(link);
        expected.setAbsolute("fghij");
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abcdefghij";
        b = "abcde";
        expected = new RelativeString();
        link = new StringLink(0, 0, 5);
        expected.addLink(link);
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abcdefghij";
        b = "abcdefg";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        String expected = "abcde";
        assertEquals(expected, StringIncrementor.patch(a, r));

        r.setAbsolute("fghij");
        expected = "fghij";
        assertEquals(expected, StringIncrementor.patch(a, r));

        r.setAbsolute("");
        StringLink link = new StringLink(0, 0, 5);
        r.addLink(link);
        expected = "abcde";
        assertEquals(expected, StringIncrementor.patch(a, r));

        r = new RelativeString();
        link = new StringLink(0, 0, 5);
        r.addLink(link);
        expected = "abcde";
        assertEquals(expected, StringIncrementor.patch("", r));

        r = new RelativeString();
        link = new StringLink(0, 0, 5);
        r.addLink(link);
        expected = "";
        assertEquals(expected, StringIncrementor.patch("", r));

        a = "abcde";
        r = new RelativeString();
        link = new StringLink(0, 5, 5);
        r.addLink(link);
        expected = "fghijabcde";
        assertEquals(expected, StringIncrementor.patch("", r));

        a = "abcde";
        r = new RelativeString();
        link = new StringLink(0, 0, 5);
        r.addLink(link);
        expected = "fghijabcde";
        assertEquals(expected, StringIncrementor.patch("", r));

        a = "abcde";
        r = new RelativeString();
        link = new StringLink(0, 0, 5);
        r.addLink(link);
        link = new StringLink(5, 5, 5);
        r.addLink(link);
        expected = "fghijabcdeklmno";
        assertEquals(expected, StringIncrementor.patch("klmno", r));
    }
}