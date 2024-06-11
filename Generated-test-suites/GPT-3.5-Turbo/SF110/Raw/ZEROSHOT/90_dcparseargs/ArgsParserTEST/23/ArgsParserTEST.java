import org.junit.*;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
        parser.setArgs(new String[]{"arg1"});
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--long"});
        SwitchArgument switchArg1 = parser.parseSwitchArgument("s");
        SwitchArgument switchArg2 = parser.parseSwitchArgument("long");
        assertEquals(0, switchArg1.getIndex());
        assertTrue(switchArg1.isFound());
        assertEquals(1, switchArg2.getIndex());
        assertTrue(switchArg2.isFound());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-p", "value", "--long", "value2"});
        ParameterArgument paramArg1 = parser.parseParameterArgument("p");
        ParameterArgument paramArg2 = parser.parseParameterArgument("long");
        assertEquals(0, paramArg1.getIndex());
        assertEquals("value", paramArg1.getValue());
        assertEquals(2, paramArg2.getIndex());
        assertEquals("value2", paramArg2.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
        assertEquals("arg3", stringArgs.get(2).getValue());
    }

}