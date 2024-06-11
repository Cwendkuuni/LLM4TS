import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;
    
    @Before
    public void setUp() {
        options = mock(Options.class);
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten() {
        String[] arguments = {"--foo", "-a", "arg1", "--bar=arg2", "-bcd", "arg3"};
        String[] expected = {"--", "--foo", "arg1", "--bar", "arg2", "-b", "-c", "-d", "arg3"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
        verify(options).hasOption("--foo");
        verify(options).hasOption("-a");
        verify(options).getOption("--bar");
        verify(options).hasOption("-b");
        verify(options).hasOption("-c");
        verify(options).hasOption("-d");
    }
    
    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] arguments = {"--foo", "-a", "arg1", "--bar=arg2", "-bcd", "arg3"};
        String[] expected = {"--", "--foo", "-a", "arg1", "--bar", "arg2", "-b", "-c", "-d", "arg3"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
        verify(options).hasOption("--foo");
        verify(options).hasOption("-a");
        verify(options).getOption("--bar");
        verify(options).hasOption("-b");
        verify(options).hasOption("-c");
        verify(options).hasOption("-d");
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NullArguments() {
        String[] arguments = null;
        String[] expected = {};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testInit() {
        parser.init();
        
        assertEquals(false, parser.eatTheRest);
        assertEquals(new ArrayList<String>(), parser.tokens);
    }
    
    @Test
    public void testGobble_WithRemainingTokens() {
        Iterator iter = Arrays.asList("token1", "token2", "token3").iterator();
        
        parser.gobble(iter);
        
        assertEquals(Arrays.asList("token1", "token2", "token3"), parser.tokens);
    }
    
    @Test
    public void testGobble_NoRemainingTokens() {
        Iterator iter = Arrays.asList().iterator();
        
        parser.gobble(iter);
        
        assertEquals(new ArrayList<String>(), parser.tokens);
    }
    
    @Test
    public void testGobble_EmptyIterator() {
        Iterator iter = mock(Iterator.class);
        
        parser.gobble(iter);
        
        verify(iter).hasNext();
    }
    
    @Test
    public void testProcessNonOptionToken() {
        parser.processNonOptionToken("value");
        
        assertEquals(true, parser.eatTheRest);
        assertEquals(Arrays.asList("--", "value"), parser.tokens);
    }
    
    @Test
    public void testProcessOptionToken_ExistingOption() {
        parser.processOptionToken("--foo", true);
        
        assertEquals(Arrays.asList("--foo"), parser.tokens);
    }
    
    @Test
    public void testProcessOptionToken_NonExistingOption_StopAtNonOptionTrue() {
        parser.processOptionToken("--baz", true);
        
        assertEquals(true, parser.eatTheRest);
        assertEquals(Arrays.asList("--baz"), parser.tokens);
    }
    
    @Test
    public void testProcessOptionToken_NonExistingOption_StopAtNonOptionFalse() {
        parser.processOptionToken("--baz", false);
        
        assertEquals(Arrays.asList("--baz"), parser.tokens);
    }
    
    @Test
    public void testBurstToken() {
        parser.options.addOption("a", false, "");
        parser.options.addOption("b", false, "");
        parser.options.addOption("c", true, "");
        
        parser.burstToken("-abc", true);
        
        assertEquals(Arrays.asList("-a", "-b", "-c"), parser.tokens);
    }
    
    @Test
    public void testBurstToken_StopAtNonOptionTrue() {
        parser.processNonOptionToken("--baz");
        
        assertEquals(true, parser.eatTheRest);
        assertEquals(Arrays.asList("--", "--baz"), parser.tokens);
    }
    
    @Test
    public void testBurstToken_StopAtNonOptionFalse() {
        parser.options.addOption("a", false, "");
        
        parser.burstToken("-abcd", false);
        
        assertEquals(Arrays.asList("-a"), parser.tokens);
    }
}