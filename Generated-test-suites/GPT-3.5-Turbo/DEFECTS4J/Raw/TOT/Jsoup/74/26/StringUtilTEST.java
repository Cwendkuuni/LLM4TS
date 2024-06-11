
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class StringUtilTest {

    @Test
    public void testJoinCollection(){
        Collection<String> collection = Arrays.asList("One", "Two", "Three");
        String joinedString = StringUtil.join(collection, ",");
        assertEquals("One,Two,Three", joinedString);
    }

    @Test
    public void testJoinIterator(){
        Iterator<String> iterator = Arrays.asList("One", "Two", "Three").iterator();
        String joinedString = StringUtil.join(iterator, ",");
        assertEquals("One,Two,Three", joinedString);
    }

    @Test
    public void testJoinArray(){
        String[] array = {"One", "Two", "Three"};
        String joinedString = StringUtil.join(array, ",");
        assertEquals("One,Two,Three", joinedString);
    }

    @Test
    public void testPadding(){
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }

    @Test
    public void testIsBlank(){
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Test"));
    }

    @Test
    public void testIsNumeric(){
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("0"));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric("123a45"));
    }

    @Test
    public void testIsWhitespace(){
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace(){
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace(){
        String input = "This   is a    test";
        String normalised = StringUtil.normaliseWhitespace(input);
        assertEquals("This is a test", normalised);
    }

    @Test
    public void testIn(){
        assertTrue(StringUtil.in("One", "One", "Two", "Three"));
        assertFalse(StringUtil.in("Four", "One", "Two", "Three"));
    }

    @Test
    public void testInSorted(){
        assertTrue(StringUtil.inSorted("One", new String[]{"One", "Three", "Two"}));
        assertFalse(StringUtil.inSorted("Four", new String[]{"One", "Three", "Two"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relative = "/path/to/resource";
        URL resolved = StringUtil.resolve(base, relative);
        assertEquals("https://www.example.com/path/to/resource", resolved.toString());
    }

    @Test
    public void testResolveEmptyString(){
        String baseUrl = "https://www.example.com/";
        String relative = "";
        String resolved = StringUtil.resolve(baseUrl, relative);
        assertEquals("", resolved);
    }

    @Test
    public void testResolveMalformedUrl(){
        String baseUrl = "https://www.example.com/";
        String relative = "invalid-url";
        String resolved = StringUtil.resolve(baseUrl, relative);
        assertEquals("", resolved);
    }

    @Test
    public void testStringBuilder(){
        StringBuilder stringBuilder = StringUtil.stringBuilder();
        assertNotNull(stringBuilder);
    }
}

