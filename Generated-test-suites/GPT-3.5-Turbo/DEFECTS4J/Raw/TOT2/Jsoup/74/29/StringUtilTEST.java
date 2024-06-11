
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("a", "b", "c");
        String sep = "-";
        String joinedString = StringUtil.join(strings, sep);
        assertEquals("a-b-c", joinedString);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("1", "2", "3");
        String sep = "/";
        String joinedString = StringUtil.join(strings.iterator(), sep);
        assertEquals("1/2/3", joinedString);
    }

    @Test
    public void testIsBlank() {
        String blankString = "";
        assertTrue(StringUtil.isBlank(blankString));

        String nonBlankString = "  ";
        assertFalse(StringUtil.isBlank(nonBlankString));
    }
}
