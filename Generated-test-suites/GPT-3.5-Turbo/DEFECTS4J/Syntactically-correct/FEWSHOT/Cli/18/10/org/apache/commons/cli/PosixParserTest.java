import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(new Option("f", false, "This is option f"));
        options.addOption(new Option("foo", true, "This is option foo"));
        options.addOption(new Option("bar", true, "This is option bar"));
    }
    
    @Test
    public void testFlatten() {
        String[] arguments = {"-f", "--foo", "value", "-bar", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-bar", result[3]);
        assertEquals("value", result[4]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-f", "--foo", "value", "non-option", "-bar", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("value", result[2]);
        assertEquals("non-option", result[3]);
        assertEquals("-bar", result[4]);
    }
    
    @Test
    public void testFlatten_NoMatch() {
        String[] arguments = {"-f", "--unknown", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--unknown", result[1]);
        assertEquals("value", result[2]);
    }
    
    @Test
    public void testFlatten_NoMatch_StopAtNonOption() {
        String[] arguments = {"-f", "--unknown", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--unknown", result[1]);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        String[] arguments = {"--", "-f"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-f", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(0, result.length);
    }
}