import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {
    
    private PosixParser posixParser;
    private String[] arguments;
    private Options options;
    
    @Before
    public void setUp() {
        posixParser = new PosixParser();
        arguments = new String[0];
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        boolean stopAtNonOption = true;
        
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_TokensStartWithDoubleHyphen() {
        arguments = new String[] {"--foo", "--bar=123"};
        options.addOption("foo", false, "Foo option");
        options.addOption("bar", true, "Bar option");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar=123", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphenToken() {
        arguments = new String[] {"-"};
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionTokenWithOneCharacter() {
        arguments = new String[] {"-a"};
        options.addOption("a", false, "Short option A");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionTokenWithMultipleCharacters() {
        arguments = new String[] {"-abc"};
        options.addOption("a", false, "Short option A");
        options.addOption("b", false, "Short option B");
        options.addOption("c", false, "Short option C");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }
    
    @Test
    public void testFlatten_ShortOptionTokenWithArgument() {
        arguments = new String[] {"-a123"};
        options.addOption("a", true, "Short option A with argument");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("123", result[1]);
    }
    
    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption() {
        arguments = new String[] {"value1", "value2"};
        
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("value1", result[1]);
    }
    
    @Test
    public void testFlatten_NonOptionToken_NotStopAtNonOption() {
        arguments = new String[] {"value1", "value2"};
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }
    
    @Test
    public void testBurstToken_ValidOptionExists() {
        String token = "-a";
        options.addOption("a", false, "Short option A");
        
        posixParser.burstToken(token, false);
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
    }
    
    @Test
    public void testBurstToken_ValidOptionExistsWithArgument() {
        String token = "-a123";
        options.addOption("a", true, "Short option A with argument");
        
        posixParser.burstToken(token, false);
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
        assertEquals("123", posixParser.getTokens().get(1));
    }
    
    @Test
    public void testBurstToken_OptionDoesNotExist_StopAtNonOption() {
        String token = "-xy";
        boolean stopAtNonOption = true;
        
        posixParser.burstToken(token, stopAtNonOption);
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals(token, posixParser.getTokens().get(0));
    }
    
    @Test
    public void testBurstToken_OptionDoesNotExist_NotStopAtNonOption() {
        String token = "-xy";
        boolean stopAtNonOption = false;
        
        posixParser.burstToken(token, stopAtNonOption);
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-x", posixParser.getTokens().get(0));
    }
    
    @Test
    public void testProcess_OptionHasArg() {
        options.addOption("a", true, "Option A");
        posixParser.setCurrentOption(options.getOption("a"));
        
        posixParser.process("123");
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("123", posixParser.getTokens().get(0));
    }
    
    @Test
    public void testProcess_OptionHasArgs() {
        options.addOption("a", true, "Option A");
        posixParser.setCurrentOption(options.getOption("a"));
        
        posixParser.process("123");
        
        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("123", posixParser.getTokens().get(0));
    }
    
    @Test
    public void testProcess_NoCurrentOption_StopAtNonOption() {
        String value = "value1";
        boolean stopAtNonOption = true;
        
        posixParser.process(value);
        
        assertTrue(posixParser.isEatTheRest());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals(value, posixParser.getTokens().get(1));
    }
    
    @Test
    public void testProcess_NoCurrentOption_NotStopAtNonOption() {
        String value = "value1";
        boolean stopAtNonOption = false;
        
        posixParser.process(value);
        
        assertTrue(posixParser.isEatTheRest());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals(value, posixParser.getTokens().get(1));
    }
    
    @Test
    public void testGobble() {
        int numTokens = 3;
        Iterator<String> iter = Arrays.asList("value1", "value2", "value3").iterator();
        posixParser.setEatTheRest(true);
        
        posixParser.gobble(iter);
        
        assertFalse(posixParser.isEatTheRest());
        assertEquals(numTokens, posixParser.getTokens().size());
        assertEquals("value1", posixParser.getTokens().get(numTokens - 3));
        assertEquals("value2", posixParser.getTokens().get(numTokens - 2));
        assertEquals("value3", posixParser.getTokens().get(numTokens - 1));
    }
}