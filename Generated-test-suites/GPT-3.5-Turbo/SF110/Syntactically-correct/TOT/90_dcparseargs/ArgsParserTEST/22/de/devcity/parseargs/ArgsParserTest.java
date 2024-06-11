
import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {

    private ArgsParser parser;
    
    @Before
    public void setUp() {
        parser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        // Typical use case
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
        
        // Edge case: empty args
        parser.setArgs(new String[]{});
        assertEquals(0, parser.getArgsCount());
        
        // Error scenario: null args
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        // Typical use case
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsLeftCount());
        
        // Edge case: all args consumed
        parser.setArgs(new String[]{});
        assertEquals(0, parser.getArgsLeftCount());
        
        // Error scenario: null innerArgs
        parser.setArgs(new String[]{"arg1", "arg2"});
        parser.setArgs(null);
        assertEquals(0, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        // Typical use case: long key found
        parser.setArgs(new String[]{"--key", "value"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertNotNull(result);
        assertTrue(result.isSwitch());
        assertEquals(0, result.getIndex());
        
        // Typical use case: short key found
        parser.setArgs(new String[]{"-k"});
        result = parser.parseSwitchArgument("k");
        assertNotNull(result);
        assertTrue(result.isSwitch());
        assertEquals(0, result.getIndex());
        
        // Edge case: long key not found
        parser.setArgs(new String[]{});
        result = parser.parseSwitchArgument("key");
        assertNotNull(result);
        assertFalse(result.isSwitch());
        assertEquals(-1, result.getIndex());
        
        // Error scenario: null args
        parser.setArgs(null);
        result = parser.parseSwitchArgument("key");
        assertNotNull(result);
        assertFalse(result.isSwitch());
        assertEquals(-1, result.getIndex());
    }
    
    @Test
    public void testParseParameterArgument() {
        // Typical use case: long key found with value
        parser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());
        
        // Typical use case: short key found with value
        parser.setArgs(new String[]{"-k", "value"});
        result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
        
        // Edge case: long key found without value
        parser.setArgs(new String[]{"--key"});
        result = parser.parseParameterArgument("key");
        assertNull(result);
        
        // Edge case: short key found without value
        parser.setArgs(new String[]{"-k"});
        result = parser.parseParameterArgument("k");
        assertNull(result);
        
        // Error scenario: null args
        parser.setArgs(null);
        result = parser.parseParameterArgument("key");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        // Typical use case: one argument
        parser.setArgs(new String[]{"arg"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getIndex());
        assertEquals("arg", result.get(0).getValue());
        
        // Typical use case: multiple arguments
        parser.setArgs(new String[]{"arg1", "arg2"});
        result = parser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).getIndex());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals(1, result.get(1).getIndex());
        assertEquals("arg2", result.get(1).getValue());
        
        // Edge case: no arguments
        parser.setArgs(new String[]{});
        result = parser.parseStringArgument();
        assertTrue(result.isEmpty());
        
        // Error scenario: null innerArgs
        parser.setArgs(new String[]{"arg"});
        parser.setArgs(null);
        result = parser.parseStringArgument();
        assertTrue(result.isEmpty());
    }
}
