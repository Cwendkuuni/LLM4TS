
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = Util.stripLeadingHyphens("--test");
        assertEquals("test", str);
        str = Util.stripLeadingHyphens("-test");
        assertEquals("test", str);
        str = Util.stripLeadingHyphens("test");
        assertEquals("test", str);
        str = Util.stripLeadingHyphens(null);
        assertNull(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", str);
        str = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("test", str);
        str = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test", str);
        str = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", str);
    }

}
