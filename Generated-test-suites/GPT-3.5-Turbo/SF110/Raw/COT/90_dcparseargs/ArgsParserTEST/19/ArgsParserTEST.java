import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    private String[] testArgs;
    
    @Before
    public void setup() {
        testArgs = new String[] {"--arg1", "value1", "-a", "--arg2", "-s"};
        argsParser = new ArgsParser(testArgs);
    }
    
    @Test
    public void testGetArgsCount() {
        int result = argsParser.getArgsCount();
        assertEquals(5, result);
    }
    
    @Test
    public void testGetArgs() {
        String[] result = argsParser.getArgs();
        assertArrayEquals(testArgs, result);
    }
    
    @Test
    public void testSetArgs() {
        String[] newArgs = new String[] {"--arg1", "value1", "-a"};
        argsParser.setArgs(newArgs);
        String[] result = argsParser.getArgs();
        assertArrayEquals(newArgs, result);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        int result = argsParser.getArgsLeftCount();
        assertEquals(5, result);
    }
    
    @Test
    public void testParseSwitchArgument() {
        SwitchArgument result = argsParser.parseSwitchArgument("arg1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertTrue(result.isLongKey());
        
        result = argsParser.parseSwitchArgument("a");
        assertNotNull(result);
        assertEquals(2, result.getIndex());
        assertFalse(result.isLongKey());
        
        result = argsParser.parseSwitchArgument("arg2");
        assertNotNull(result);
        assertEquals(-1, result.getIndex());
        assertTrue(result.isLongKey());
    }
    
    @Test
    public void testParseParameterArgument() {
        ParameterArgument result = argsParser.parseParameterArgument("arg1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertTrue(result.isLongKey());
        assertEquals("value1", result.getValue());
        
        result = argsParser.parseParameterArgument("a");
        assertNull(result);
        
        result = argsParser.parseParameterArgument("arg2");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        List<StringArgument> result = argsParser.parseStringArgument();
        assertNotNull(result);
        assertEquals(3, result.size());
        
        StringArgument arg1 = result.get(0);
        assertEquals(0, arg1.getIndex());
        assertEquals("--arg1", arg1.getValue());
        
        StringArgument arg2 = result.get(1);
        assertEquals(1, arg2.getIndex());
        assertEquals("-a", arg2.getValue());
        
        StringArgument arg3 = result.get(2);
        assertEquals(3, arg3.getIndex());
        assertEquals("--arg2", arg3.getValue());
    }
}