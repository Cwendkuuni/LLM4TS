import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Typical use case
        String result = StringUtil.join(Arrays.asList("a", "b", "c"), ",");
        assertEquals("a,b,c", result);

        // Edge case - empty collection
        result = StringUtil.join(Collections.emptyList(), ",");
        assertEquals("", result);
        
        // Exception handling - null collection
        result = StringUtil.join(null, ",");
        assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        // Typical use case
        String result = StringUtil.join(Arrays.asList("a", "b", "c").iterator(), ",");
        assertEquals("a,b,c", result);

        // Edge case - single element
        result = StringUtil.join(Collections.singletonList("a").iterator(), ",");
        assertEquals("a", result);

        // Exception handling - null iterator
        result = StringUtil.join((Iterator)null, ",");
        assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        // Typical use case
        String result = StringUtil.join(new String[]{"a", "b", "c"}, ",");
        assertEquals("a,b,c", result);

        // Edge case - empty array
        result = StringUtil.join(new String[]{}, ",");
        assertEquals("", result);

        // Exception handling - null array
        result = StringUtil.join((String[])null, ",");
        assertEquals("", result);
    }

    // Test cases for other StringUtil methods can be added as below

}