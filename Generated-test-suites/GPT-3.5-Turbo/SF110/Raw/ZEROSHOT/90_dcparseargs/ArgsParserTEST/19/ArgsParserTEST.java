import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {
    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @After
    public void tearDown() {
        argsParser = null;
    }
    
    // Test getArgsCount method
    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }
    
    // Test getArgs method
    @Test
    public void testGetArgs() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[] {"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    // Test setArgs method with valid arguments
    @Test
    public void testSetArgsValid() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[] {"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    // Test setArgs method with null arguments
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }
    
    // Test getArgsLeftCount method with all arguments remaining
    @Test
    public void testGetArgsLeftCountAllRemaining() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }
    
    // Test getArgsLeftCount method with some arguments remaining
    @Test
    public void testGetArgsLeftCountSomeRemaining() {
        argsParser.setArgs(new String[] {"arg1", null, "arg3"});
        assertEquals(2, argsParser.getArgsLeftCount());
    }
    
    // Test parseSwitchArgument method with long key found
    @Test
    public void testParseSwitchArgumentLongKeyFound() {
        argsParser.setArgs(new String[] {"--switch"});
        SwitchArgument result = argsParser.parseSwitchArgument("switch");
        assertTrue(result.isLongKey());
        assertEquals(0, result.getIndex());
        assertEquals("switch", result.getKey());
        assertTrue(result.getValue());
    }
    
    // Test parseSwitchArgument method with long key not found
    @Test
    public void testParseSwitchArgumentLongKeyNotFound() {
        argsParser.setArgs(new String[] {"--other"});
        SwitchArgument result = argsParser.parseSwitchArgument("switch");
        assertFalse(result.isLongKey());
        assertEquals(-1, result.getIndex());
        assertEquals("switch", result.getKey());
        assertFalse(result.getValue());
    }
    
    // Test parseSwitchArgument method with short key found
    @Test
    public void testParseSwitchArgumentShortKeyFound() {
        argsParser.setArgs(new String[] {"-s"});
        SwitchArgument result = argsParser.parseSwitchArgument("s");
        assertTrue(result.isLongKey());
        assertEquals(0, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.getValue());
    }
    
    // Test parseSwitchArgument method with short key not found
    @Test
    public void testParseSwitchArgumentShortKeyNotFound() {
        argsParser.setArgs(new String[] {"-o"});
        SwitchArgument result = argsParser.parseSwitchArgument("s");
        assertFalse(result.isLongKey());
        assertEquals(-1, result.getIndex());
        assertEquals("s", result.getKey());
        assertFalse(result.getValue());
    }
    
    // Test parseParameterArgument method with long key found
    @Test
    public void testParseParameterArgumentLongKeyFound() {
        argsParser.setArgs(new String[] {"--key", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertTrue(result.isLongKey());
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    // Test parseParameterArgument method with long key not found
    @Test
    public void testParseParameterArgumentLongKeyNotFound() {
        argsParser.setArgs(new String[] {"--other"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertFalse(result.isLongKey());
        assertEquals(-1, result.getIndex());
        assertEquals("key", result.getKey());
        assertNull(result.getValue());
    }
    
    // Test parseParameterArgument method with short key found
    @Test
    public void testParseParameterArgumentShortKeyFound() {
        argsParser.setArgs(new String[] {"-k", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertTrue(result.isLongKey());
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    // Test parseParameterArgument method with short key not found
    @Test
    public void testParseParameterArgumentShortKeyNotFound() {
        argsParser.setArgs(new String[] {"-o"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertFalse(result.isLongKey());
        assertEquals(-1, result.getIndex());
        assertEquals("k", result.getKey());
        assertNull(result.getValue());
    }
    
    // Test parseStringArgument method with all arguments
    @Test
    public void testParseStringArgumentAllArguments() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg3", result.get(2).getValue());
    }
    
    // Test parseStringArgument method with some arguments
    @Test
    public void testParseStringArgumentSomeArguments() {
        argsParser.setArgs(new String[] {"arg1", null, "arg3"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg3", result.get(1).getValue());
    }
}