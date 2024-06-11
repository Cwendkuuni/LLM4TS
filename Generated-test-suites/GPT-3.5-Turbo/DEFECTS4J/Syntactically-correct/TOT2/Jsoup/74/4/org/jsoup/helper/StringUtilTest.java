import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Typical use case
        Collection<String> strings = Arrays.asList("a", "b", "c");
        String result = StringUtil.join(strings, ",");
        assertEquals("a,b,c", result);

        // Edge case
        strings = Arrays.asList("abc");
        result = StringUtil.join(strings, ",");
        assertEquals("abc", result);

        // Exception handling
        try {
            StringUtil.join(null, ",");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testJoinIterator() {
        // Typical use case
        Iterator<String> strings = Arrays.asList("a", "b", "c").iterator();
        String result = StringUtil.join(strings, ",");
        assertEquals("a,b,c", result);

        // Edge case
        strings = Arrays.asList("abc").iterator();
        result = StringUtil.join(strings, ",");
        assertEquals("abc", result);

        // Exception handling
        try {
            StringUtil.join(null, ",");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testJoinStringArray() {
        // Typical use case
        String[] strings = {"a", "b", "c"};
        String result = StringUtil.join(strings, ",");
        assertEquals("a,b,c", result);

        // Edge case
        strings = new String[]{"abc"};
        result = StringUtil.join(strings, ",");
        assertEquals("abc", result);

        // Exception handling
        try {
            StringUtil.join(null, ",");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    // Similar test methods for other public methods in StringUtil class

}