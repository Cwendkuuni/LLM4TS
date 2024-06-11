import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    private ArgsParser parser;
    
    @Before
    public void setUp() {
        parser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test
    public void testSetArgs_NullArgs() {
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyFound() {
        String[] args = {"--key1", "-k"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("key1");
        assertTrue(result.isExists());
        assertEquals(0, result.getIndex());
        assertEquals("key1", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyNotFound() {
        String[] args = {"--key1", "-k"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("key2");
        assertFalse(result.isExists());
        assertEquals(-1, result.getIndex());
        assertEquals("key2", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyFound() {
        String[] args = {"-k", "-a"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertTrue(result.isExists());
        assertEquals(1, result.getIndex());
        assertEquals("a", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyNotFound() {
        String[] args = {"-k", "-a"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("b");
        assertFalse(result.isExists());
        assertEquals(-1, result.getIndex());
        assertEquals("b", result.getKey());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithValueFound() {
        String[] args = {"--key1", "value1"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("key1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("key1", result.getKey());
        assertEquals("value1", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithoutValue() {
        String[] args = {"--key1"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("key1");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithValueFound() {
        String[] args = {"-a", "value1"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("a", result.getKey());
        assertEquals("value1", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithoutValue() {
        String[] args = {"-a"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument_AllArgsParsed() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertNull(parser.getArgs()[0]);
        assertNull(parser.getArgs()[1]);
        assertNull(parser.getArgs()[2]);
    }
    
    @Test
    public void testParseStringArgument_NoArgsParsed() {
        String[] args = {};
        parser.setArgs(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(0, result.size());
    }
}