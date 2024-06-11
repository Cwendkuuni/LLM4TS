import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        try {
            argsParser.setArgs(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key", "-k"});
        SwitchArgument switchArgumentLong = argsParser.parseSwitchArgument("key");
        SwitchArgument switchArgumentShort = argsParser.parseSwitchArgument("k");
        assertEquals(0, switchArgumentLong.getIndex());
        assertEquals("key", switchArgumentLong.getKey());
        assertTrue(switchArgumentLong.isFlag());
        assertEquals(1, switchArgumentShort.getIndex());
        assertEquals("k", switchArgumentShort.getKey());
        assertTrue(switchArgumentShort.isFlag());
        
        SwitchArgument switchArgumentLongInvalid = argsParser.parseSwitchArgument("invalid");
        assertFalse(switchArgumentLongInvalid.isFlag());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key", "value", "-k",});
        ParameterArgument parameterArgumentLong = argsParser.parseParameterArgument("key");
        ParameterArgument parameterArgumentShort = argsParser.parseParameterArgument("k");
        assertEquals(0, parameterArgumentLong.getIndex());
        assertEquals("key", parameterArgumentLong.getKey());
        assertEquals("value", parameterArgumentLong.getValue());
        assertEquals(2, parameterArgumentShort.getIndex());
        assertEquals("k", parameterArgumentShort.getKey());
        assertNull(parameterArgumentShort.getValue());
        
        ParameterArgument parameterArgumentLongInvalid = argsParser.parseParameterArgument("invalid");
        assertNull(parameterArgumentLongInvalid);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals(0, stringArguments.get(0).getIndex());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals(1, stringArguments.get(1).getIndex());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals(2, stringArguments.get(2).getIndex());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }
}