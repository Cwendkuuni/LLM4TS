import de.devcity.parseargs.arguments.*;
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
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        int count = parser.getArgsCount();
        
        assertEquals(3, count);
    }
    
    @Test
    public void testGetArgsCountWithNoArgs() {
        String[] args = {};
        parser.setArgs(args);
        
        int count = parser.getArgsCount();
        
        assertEquals(0, count);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        String[] args = null;
        
        parser.setArgs(args);
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        String[] result = parser.getArgs();
        
        assertArrayEquals(args, result);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        int count = parser.getArgsLeftCount();
        
        assertEquals(3, count);
    }
    
    @Test
    public void testGetArgsLeftCountWithSomeArgsParsed() {
        String[] args = {"--switch", "param", "arg2", "--switch2", "param2"};
        parser.setArgs(args);
        
        parser.parseSwitchArgument("switch");
        parser.parseParameterArgument("param");
        
        int count = parser.getArgsLeftCount();
        
        assertEquals(2, count);
    }
    
    @Test
    public void testParseSwitchArgumentWithLongKey() {
        String[] args = {"--switch"};
        parser.setArgs(args);
        
        SwitchArgument result = parser.parseSwitchArgument("switch");
        
        assertEquals(0, result.getIndex());
        assertEquals("switch", result.getKey());
        assertTrue(result.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentWithNonExistingLongKey() {
        String[] args = {"--switch"};
        parser.setArgs(args);
        
        SwitchArgument result = parser.parseSwitchArgument("nonexistent");
        
        assertEquals(-1, result.getIndex());
        assertEquals("nonexistent", result.getKey());
        assertFalse(result.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentWithShortKey() {
        String[] args = {"-s"};
        parser.setArgs(args);
        
        SwitchArgument result = parser.parseSwitchArgument("s");
        
        assertEquals(0, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentWithNonExistingShortKey() {
        String[] args = {"-s"};
        parser.setArgs(args);
        
        SwitchArgument result = parser.parseSwitchArgument("nonexistent");
        
        assertEquals(-1, result.getIndex());
        assertEquals("nonexistent", result.getKey());
        assertFalse(result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentWithLongKey() {
        String[] args = {"--param", "value"};
        parser.setArgs(args);
        
        ParameterArgument result = parser.parseParameterArgument("param");
        
        assertEquals(0, result.getIndex());
        assertEquals("param", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentWithNonExistingLongKey() {
        String[] args = {"--param", "value"};
        parser.setArgs(args);
        
        ParameterArgument result = parser.parseParameterArgument("nonexistent");
        
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentWithShortKey() {
        String[] args = {"-p", "value"};
        parser.setArgs(args);
        
        ParameterArgument result = parser.parseParameterArgument("p");
        
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentWithNonExistingShortKey() {
        String[] args = {"-p", "value"};
        parser.setArgs(args);
        
        ParameterArgument result = parser.parseParameterArgument("nonexistent");
        
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentWithMissingValue() {
        String[] args = {"--param"};
        parser.setArgs(args);
        
        ParameterArgument result = parser.parseParameterArgument("param");
        
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        List<StringArgument> result = parser.parseStringArgument();
        
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg3", result.get(2).getValue());
    }
    
    @Test
    public void testParseStringArgumentWithSomeArgsParsed() {
        String[] args = {"arg1", "--switch", "param", "arg2", "--switch2", "param2"};
        parser.setArgs(args);
        
        parser.parseSwitchArgument("switch");
        parser.parseParameterArgument("param");
        
        List<StringArgument> result = parser.parseStringArgument();
        
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg2", result.get(2).getValue());
    }
}