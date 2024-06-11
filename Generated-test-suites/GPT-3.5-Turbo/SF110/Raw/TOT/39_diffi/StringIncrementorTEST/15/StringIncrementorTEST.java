import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_validInput_returnExpectedResult() {
        String a = "abcde";
        String b = "bcdef";
        RelativeString expected = new RelativeString();
        StringLink link = new StringLink(0, 0, 5);
        expected.addLink(link);

        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_emptyStrings_returnEmptyResult() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();

        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_missingLink_returnEmptyResult() {
        String a = "abcdefghi";
        String b = "abcjdefghi";
        RelativeString expected = new RelativeString();

        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_validInput_returnExpectedResult() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 0, 5);
        r.addLink(link);
        String expected = "abcde";

        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_emptyInput_returnExpectedResult() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";

        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_missingLink_returnExpectedResult() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        String expected = "abcde";

        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }
}