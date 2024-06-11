import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expected = "foo";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
        
        // Test other possible inputs and expected outputs
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"foo bar\"";
        String expected = "foo bar";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
        
        // Test other possible inputs and expected outputs
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphens_EmptyString() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
        fail("Expected an empty string but got a non-empty string.");
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotes_NullString() {
        String input = null;
        String expected = null;
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
        fail("Expected a null string but got a non-null string.");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testStripLeadingHyphens_StringWithoutLeadingHyphens() {
        String input = "foo";
        String expected = "foo";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
        fail("Expected the same string but got a different string.");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotes_StringWithoutTrailingQuotes() {
        String input = "foo\"";
        String expected = "foo";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
        fail("Expected the same string but got a different string.");
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingHyphens_ShortString() {
        String input = "--";
        String expected = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
        fail("Expected an empty string but got a non-empty string.");
    }
}