import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testConstructorNoArgs() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser.getArgs());
        assertEquals(0, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testConstructorWithArgs() {
        String[] args = {"--key", "value", "-f", "file.txt"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
        assertEquals(args.length, parser.getArgsCount());
        assertEquals(args.length, parser.getArgsLeftCount());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        String[] args = {"--key", "value", "-f", "file.txt"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
        assertEquals(args.length, parser.getArgsCount());
        assertEquals(args.length, parser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"--key", "value", "-f", "file.txt"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsLeftCount());
        parser.parseSwitchArgument("key");
        assertEquals(args.length - 1, parser.getArgsLeftCount());
        parser.parseParameterArgument("f");
        assertEquals(args.length - 2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--key", "-f", "file.txt"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switch1 = parser.parseSwitchArgument("key");
        assertTrue(switch1.isSet());
        assertEquals(0, switch1.getIndex());
        assertEquals("key", switch1.getKey());

        SwitchArgument switch2 = parser.parseSwitchArgument("f");
        assertTrue(switch2.isSet());
        assertEquals(1, switch2.getIndex());
        assertEquals("f", switch2.getKey());

        SwitchArgument switch3 = parser.parseSwitchArgument("g");
        assertFalse(switch3.isSet());
        assertEquals(-1, switch3.getIndex());
        assertEquals("g", switch3.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--key", "value", "-f", "file.txt"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument param1 = parser.parseParameterArgument("key");
        assertNotNull(param1);
        assertEquals(0, param1.getIndex());
        assertEquals("key", param1.getKey());
        assertEquals("value", param1.getValue());

        ParameterArgument param2 = parser.parseParameterArgument("f");
        assertNotNull(param2);
        assertEquals(2, param2.getIndex());
        assertEquals("f", param2.getKey());
        assertEquals("file.txt", param2.getValue());

        ParameterArgument param3 = parser.parseParameterArgument("g");
        assertNull(param3);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(args.length, stringArgs.size());
        for (int i = 0; i < args.length; i++) {
            StringArgument stringArg = stringArgs.get(i);
            assertEquals(i, stringArg.getIndex());
            assertEquals(args[i], stringArg.getValue());
        }
    }
}