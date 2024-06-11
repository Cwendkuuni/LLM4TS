import de.devcity.parseargs.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
        argsParser.parseSwitchArgument("arg1");
        assertEquals(2, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--arg1", "-arg2", "arg3"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("arg1");
        assertTrue(switchArgument.isSwitch());
        assertEquals("--arg1", argsParser.getArgs()[switchArgument.getIndex()]);
        assertEquals(null, argsParser.getArgs()[switchArgument.getIndex()+1]);
        
        switchArgument = argsParser.parseSwitchArgument("arg4");
        assertFalse(switchArgument.isSwitch());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--arg1", "value1", "-arg2value2", "arg3"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("arg1");
        assertEquals("value1", parameterArgument.getValue());
        
        parameterArgument = argsParser.parseParameterArgument("arg2");
        assertEquals("value2", parameterArgument.getValue());
        
        parameterArgument = argsParser.parseParameterArgument("arg3");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"value1", "value2", "value3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("value1", stringArguments.get(0).getValue());
        assertEquals("value2", stringArguments.get(1).getValue());
        assertEquals("value3", stringArguments.get(2).getValue());
    }
}