package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSJshopTest {
    @Test
    public void testMain() {
        // Test case 1: args.length < 2
        String[] args1 = {};
        JSJshop.main(args1);
        assertNull(JSJshop.applet);
        
        // Test case 2: args.length > 4
        String[] args2 = {"file1", "file2", "number", "extra"};
        JSJshop.main(args2);
        assertNull(JSJshop.applet);
        
        // Test case 3: one.equalsIgnoreCase(args[i])
        String[] args3 = {"file1", "file2", "one"};
        JSJshop.main(args3);
        assertNull(JSJshop.applet);
        
        // Test case 4: all.equalsIgnoreCase(args[i])
        String[] args4 = {"file1", "file2", "all"};
        JSJshop.main(args4);
        assertNull(JSJshop.applet);
        
        // Test case 5: catch block for NumberFormatException
        String[] args5 = {"file1", "file2", "invalid"};
        JSJshop.main(args5);
        assertNull(JSJshop.applet);
        
        // Test case 6: valid input
        String[] args6 = {"file1", "file2"};
        JSJshop.main(args6);
        assertNull(JSJshop.applet);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str.length() <= 1
        String str1 = "";
        assertEquals(str1, Util.stripLeadingAndTrailingQuotes(str1));
        
        // Test case 2: str does not start or end with '"'
        String str2 = "foo";
        assertEquals(str2, Util.stripLeadingAndTrailingQuotes(str2));
        
        // Test case 3: str starts and ends with '"', but contains additional '"'
        String str3 = "\"foo\"";
        assertEquals(str3, Util.stripLeadingAndTrailingQuotes(str3));
        
        // Test case 4: str starts and ends with '"', and does not contain additional '"'
        String str4 = "\"foo\"";
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes(str4));
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str is null
        String str1 = null;
        assertNull(Util.stripLeadingHyphens(str1));
        
        // Test case 2: str starts with "--"
        String str2 = "--foo";
        assertEquals("foo", Util.stripLeadingHyphens(str2));
        
        // Test case 3: str starts with "-"
        String str3 = "-f";
        assertEquals("f", Util.stripLeadingHyphens(str3));
        
        // Test case 4: str does not start with "-" or "--"
        String str4 = "foo";
        assertEquals(str4, Util.stripLeadingHyphens(str4));
    }
}