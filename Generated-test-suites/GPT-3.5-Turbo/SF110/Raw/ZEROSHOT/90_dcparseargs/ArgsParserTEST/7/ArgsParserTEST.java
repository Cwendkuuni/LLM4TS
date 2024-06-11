import de.devcity.parseargs.arguments.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"arg1", null, "arg3", null});
        assertEquals(2, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key2");
        assertNotNull(switchArgument);
        assertEquals(1, switchArgument.getIndex());
        assertEquals("key2", switchArgument.getKey());
        assertTrue(switchArgument.isFound());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyNotFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key3");
        assertNotNull(switchArgument);
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("key3", switchArgument.getKey());
        assertFalse(switchArgument.isFound());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"-k1", "-k2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k2");
        assertNotNull(switchArgument);
        assertEquals(1, switchArgument.getIndex());
        assertEquals("k2", switchArgument.getKey());
        assertTrue(switchArgument.isFound());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyNotFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"-k1", "-k2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k3");
        assertNotNull(switchArgument);
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("k3", switchArgument.getKey());
        assertFalse(switchArgument.isFound());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"--key1", "value1", "--key2", "value2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key2");
        assertNotNull(parameterArgument);
        assertEquals(2, parameterArgument.getIndex());
        assertEquals("key2", parameterArgument.getKey());
        assertEquals("value2", parameterArgument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyNotFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"--key1", "value1", "--key2", "value2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key3");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"-k1", "value1", "-k2", "value2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k2");
        assertNotNull(parameterArgument);
        assertEquals(2, parameterArgument.getIndex());
        assertEquals("k2", parameterArgument.getKey());
        assertEquals("value2", parameterArgument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyNotFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"-k1", "value1", "-k2", "value2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k3");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertNotNull(stringArguments);
        assertEquals(3, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }
}