package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = Util.stripLeadingHyphens("--test");
        assertEquals("test", str1);
        
        String str2 = Util.stripLeadingHyphens("-hello");
        assertEquals("hello", str2);
        
        String str3 = Util.stripLeadingHyphens("nohyphen");
        assertEquals("nohyphen", str3);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = Util.stripLeadingAndTrailingQuotes("\"quoted\"");
        assertEquals("quoted", str1);
        
        String str2 = Util.stripLeadingAndTrailingQuotes("\"hello world\"");
        assertEquals("hello world", str2);
        
        String str3 = Util.stripLeadingAndTrailingQuotes("noquotes");
        assertEquals("noquotes", str3);
    }
}