import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        parser.parseSwitchArgument("arg2");
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2", "--arg3"});
        SwitchArgument switchArg = parser.parseSwitchArgument("arg2");
        assertTrue(switchArg.isSwitch());
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a1", "-a2", "-a3"});
        SwitchArgument switchArg = parser.parseSwitchArgument("a2");
        assertTrue(switchArg.isSwitch());
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("param");
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-p", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("p");
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
    }

}