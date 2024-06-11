import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff_typical() {
        String a = "abcd";
        String b = "adc";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_edge() {
        String a = "abcd";
        String b = "abcd";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_error() {
        String a = "";
        String b = "adc";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_verbose() {
        String a = "abcd";
        String b = "adc";
        boolean verbose = true;
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(0, 2, 1));
        expected.addLink(new StringLink(2, 2, 1));
        RelativeString actual = StringIncrementor.diff(a, b, verbose);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch_typical() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.addLink(new StringLink(2, 2, 1));
        String expected = "aacd";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch_edge() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        String expected = "abcd";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch_verbose() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.addLink(new StringLink(0, 2, 1));
        r.addLink(new StringLink(2, 2, 1));
        boolean verbose = true;
        String expected = "aacdb";
        String actual = StringIncrementor.patch(a, r, verbose);
        assertEquals(expected, actual);
    }

}