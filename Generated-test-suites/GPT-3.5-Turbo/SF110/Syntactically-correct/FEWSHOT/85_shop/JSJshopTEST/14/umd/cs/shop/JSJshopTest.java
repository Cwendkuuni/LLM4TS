package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {

    private JSJshop jsjShop;

    @Before
    public void setUp() {
        jsjShop = new JSJshop();
    }

    @Test
    public void testMainWhenArgsLengthLessThan2() {
        jsjShop.main(new String[] {});
        assertTrue(JSJshopVars.flagExit);
    }

    @Test
    public void testMainWhenArgsLengthGreaterThan4() {
        jsjShop.main(new String[] {"arg1", "arg2", "arg3", "arg4", "arg5"});
        assertTrue(JSJshopVars.flagExit);
    }

    @Test
    public void testMainWhenArgsLengthEqualTo2() {
        jsjShop.main(new String[] {"arg1", "arg2"});
        assertNull(JSJshopVars.flagExit);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("string", JSJshop.stripLeadingAndTrailingQuotes("\"string\""));
        assertEquals("\"string", JSJshop.stripLeadingAndTrailingQuotes("\"\"string"));
        assertEquals("string\"", JSJshop.stripLeadingAndTrailingQuotes("string\"\""));
        assertEquals("this is a \"quoted\" string", JSJshop.stripLeadingAndTrailingQuotes("\"this is a \"\"quoted\"\" string\""));
        assertEquals("", JSJshop.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", JSJshop.stripLeadingHyphens("-f"));
        assertEquals("foo", JSJshop.stripLeadingHyphens("--foo"));
        assertEquals("--foo", JSJshop.stripLeadingHyphens("---foo"));
        assertNull(JSJshop.stripLeadingHyphens(null));
    }
}