import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-flag1", "--flag2", "-flag3"});
        SwitchArgument switchArg = parser.parseSwitchArgument("flag1");
        assertEquals(0, switchArg.getIndex());
        assertEquals("flag1", switchArg.getKey());
        assertEquals(true, switchArg.getValue());

        switchArg = parser.parseSwitchArgument("flag2");
        assertEquals(1, switchArg.getIndex());
        assertEquals("flag2", switchArg.getKey());
        assertEquals(true, switchArg.getValue());

        switchArg = parser.parseSwitchArgument("flag3");
        assertEquals(2, switchArg.getIndex());
        assertEquals("flag3", switchArg.getKey());
        assertEquals(true, switchArg.getValue());

        switchArg = parser.parseSwitchArgument("flag4");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("flag4", switchArg.getKey());
        assertEquals(false, switchArg.getValue());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-param1", "value1", "--param2", "value2"});
        ParameterArgument paramArg = parser.parseParameterArgument("param1");
        assertEquals(0, paramArg.getIndex());
        assertEquals("param1", paramArg.getKey());
        assertEquals("value1", paramArg.getValue());

        paramArg = parser.parseParameterArgument("param2");
        assertEquals(2, paramArg.getIndex());
        assertEquals("param2", paramArg.getKey());
        assertEquals("value2", paramArg.getValue());

        paramArg = parser.parseParameterArgument("param3");
        assertNull(paramArg);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
    }
}