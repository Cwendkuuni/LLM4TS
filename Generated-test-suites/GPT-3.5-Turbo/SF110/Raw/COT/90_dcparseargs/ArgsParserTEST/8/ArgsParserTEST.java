
import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }

    @Test
    public void testConstructorWithArgs() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testSetArgsWithNull() {
        ArgsParser parser = new ArgsParser();
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
        parser.parseSwitchArgument("-arg1");
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-arg1", "--arg2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("arg1");
        assertNotNull(switchArg);
        assertTrue(switchArg.isSwitch());
        assertEquals(0, switchArg.getIndex());
        assertEquals("arg1", switchArg.getKey());
    }

    @Test
    public void testParseSwitchArgumentWithNonexistentSwitch() {
        String[] args = {"-arg1", "--arg2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("arg3");
        assertNotNull(switchArg);
        assertFalse(switchArg.isSwitch());
        assertEquals(-1, switchArg.getIndex());
        assertEquals("arg3", switchArg.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"-arg1", "value1"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("arg1");
        assertNotNull(paramArg);
        assertEquals(0, paramArg.getIndex());
        assertEquals("arg1", paramArg.getKey());
        assertEquals("value1", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgumentWithNoValue() {
        String[] args = {"-arg1"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("arg1");
        assertNull(paramArg);
    }

    @Test
    public void testParseParameterArgumentWithNonexistentParameter() {
        String[] args = {"-arg1", "value1"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("arg2");
        assertNull(paramArg);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(2, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
    }
}
