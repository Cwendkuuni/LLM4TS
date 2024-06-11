import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--switch", "-s", "--non-switch", "value"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch");
        assertNotNull(switchArgument);
        assertEquals(0, switchArgument.getIndex());
        assertEquals("switch", switchArgument.getKey());
        assertTrue(switchArgument.isSwitch());

        SwitchArgument nonSwitchArgument = argsParser.parseSwitchArgument("non-switch");
        assertNotNull(nonSwitchArgument);
        assertEquals(-1, nonSwitchArgument.getIndex());
        assertEquals("non-switch", nonSwitchArgument.getKey());
        assertFalse(nonSwitchArgument.isSwitch());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--param", "value", "-p", "value", "--non-param", "arg"});
        ParameterArgument paramArgument = argsParser.parseParameterArgument("param");
        assertNotNull(paramArgument);
        assertEquals(0, paramArgument.getIndex());
        assertEquals("param", paramArgument.getKey());
        assertEquals("value", paramArgument.getValue());

        ParameterArgument nonParamArgument = argsParser.parseParameterArgument("non-param");
        assertNull(nonParamArgument);

        ParameterArgument shortParamArgument = argsParser.parseParameterArgument("p");
        assertNotNull(shortParamArgument);
        assertEquals(2, shortParamArgument.getIndex());
        assertEquals("p", shortParamArgument.getKey());
        assertEquals("value", shortParamArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }
}