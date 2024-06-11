import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_StopAtNonOption_True() {
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-a"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertArrayEquals(arguments, flattened);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_False() {
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-a"};
        boolean stopAtNonOption = false;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertArrayEquals(arguments, flattened);
    }
    
    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-a"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertArrayEquals(arguments, flattened);
    }
    
    @Test
    public void testFlatten_LongOptionNoValue() {
        Options options = new Options();
        options.addOption("foo", false, "Option foo");
        String[] arguments = {"--foo"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("--foo", flattened[0]);
    }
    
    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        String[] arguments = {"--foo", "bar"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("--foo", flattened[0]);
        assertEquals("bar", flattened[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-", flattened[0]);
    }
    
    @Test
    public void testFlatten_ShortOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOption_True() {
        Options options = new Options();
        String[] arguments = {"-x"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-x", flattened[0]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOption_False() {
        Options options = new Options();
        String[] arguments = {"-x"};
        boolean stopAtNonOption = false;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-x", flattened[0]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-abc"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-abc", flattened[0]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-abc"};
        boolean stopAtNonOption = false;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-abc", flattened[0]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_True_WithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        String[] arguments = {"-abc", "value"};
        boolean stopAtNonOption = true;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("-abc", flattened[0]);
        assertEquals("value", flattened[1]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_False_WithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        String[] arguments = {"-abc", "value"};
        boolean stopAtNonOption = false;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("-abc", flattened[0]);
        assertEquals("value", flattened[1]);
    }
}