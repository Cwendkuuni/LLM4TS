import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount_EmptyArgs_ReturnZero() {
        argsParser.setArgs(new String[]{});
        int result = argsParser.getArgsCount();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetArgsCount_NonEmptyArgs_ReturnCount() {
        argsParser.setArgs(new String[]{"-a", "value", "--flag"});
        int result = argsParser.getArgsCount();
        assertEquals(3, result);
    }
    
    @Test
    public void testGetArgs_NullArgs_ThrowNullPointerException() {
        try {
            argsParser.setArgs(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testGetArgs_ReturnArgsArray() {
        String[] args = new String[]{"-a", "value", "--flag"};
        argsParser.setArgs(args);
        String[] result = argsParser.getArgs();
        assertArrayEquals(args, result);
    }
    
    @Test
    public void testGetArgsLeftCount_AllArgsParsed_ReturnZero() {
        argsParser.setArgs(new String[]{"-a"});
        argsParser.parseSwitchArgument("a");
        int result = argsParser.getArgsLeftCount();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetArgsLeftCount_UnparsedArgs_ReturnCount() {
        argsParser.setArgs(new String[]{"-a", "-b", "value", "--flag"});
        int result = argsParser.getArgsLeftCount();
        assertEquals(4, result);
    }
    
    @Test
    public void testParseSwitchArgument_ValidLongKey_ReturnSwitchArgument() {
        argsParser.setArgs(new String[]{"--flag"});
        SwitchArgument result = argsParser.parseSwitchArgument("flag");
        assertTrue(result.isValid());
        assertEquals("--flag", argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseSwitchArgument_ValidShortKey_ReturnSwitchArgument() {
        argsParser.setArgs(new String[]{"-a"});
        SwitchArgument result = argsParser.parseSwitchArgument("a");
        assertTrue(result.isValid());
        assertNull(argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseSwitchArgument_InvalidLongKey_ReturnSwitchArgument() {
        argsParser.setArgs(new String[]{"--flag"});
        SwitchArgument result = argsParser.parseSwitchArgument("invalid");
        assertFalse(result.isValid());
        assertNull(argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseSwitchArgument_InvalidShortKey_ReturnSwitchArgument() {
        argsParser.setArgs(new String[]{"-a"});
        SwitchArgument result = argsParser.parseSwitchArgument("invalid");
        assertFalse(result.isValid());
        assertEquals("-a", argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseParameterArgument_ValidLongKeyWithValue_ReturnParameterArgument() {
        argsParser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals("value", result.getValue());
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
    
    @Test
    public void testParseParameterArgument_ValidLongKeyWithoutValue_ReturnNull() {
        argsParser.setArgs(new String[]{"--key"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertNull(result);
        assertEquals("--key", argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseParameterArgument_ValidShortKeyWithValue_ReturnParameterArgument() {
        argsParser.setArgs(new String[]{"-k", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals("value", result.getValue());
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
    
    @Test
    public void testParseParameterArgument_ValidShortKeyWithoutValue_ReturnNull() {
        argsParser.setArgs(new String[]{"-k"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertNull(result);
        assertEquals("-k", argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseStringArgument_AllArgs_ReturnListOfStringArguments() {
        argsParser.setArgs(new String[]{"-a", "value", "--flag"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
        assertNull(argsParser.getArgs()[2]);
    }
}