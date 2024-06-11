import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = mock(Options.class);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-abc", "arg1", "arg2"};
        
        when(options.hasOption("-a")).thenReturn(true);
        when(options.getOption("-a")).thenReturn(new Option("a", false, ""));
        when(options.hasOption("-b")).thenReturn(true);
        when(options.getOption("-b")).thenReturn(new Option("b", true, ""));
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg2", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_NoOption() {
        String[] arguments = {"-abc", "arg1", "arg2"};
        
        when(options.hasOption("-a")).thenReturn(false);
        when(options.hasOption("-b")).thenReturn(false);
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-abc", result[1]);
        assertEquals("arg2", result[2]);
    }
    
    @Test
    public void testFlatten_DontStopAtNonOption() {
        String[] arguments = {"-abc", "arg1", "arg2"};
        
        when(options.hasOption("-a")).thenReturn(true);
        when(options.getOption("-a")).thenReturn(new Option("a", false, ""));
        when(options.hasOption("-b")).thenReturn(true);
        when(options.getOption("-b")).thenReturn(new Option("b", true, ""));
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }
    
    @Test
    public void testFlatten_DontStopAtNonOption_NoOption() {
        String[] arguments = {"-abc", "arg1", "arg2"};
        
        when(options.hasOption("-a")).thenReturn(false);
        when(options.hasOption("-b")).thenReturn(false);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }
    
}