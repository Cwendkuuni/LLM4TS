import org.junit.Test;
import static org.junit.Assert.*;

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
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch1", "-switch2"});
        SwitchArgument switch1 = parser.parseSwitchArgument("switch1");
        assertTrue(switch1.isValid());
        assertEquals(0, switch1.getIndex());
        assertEquals("switch1", switch1.getKey());

        SwitchArgument switch2 = parser.parseSwitchArgument("switch2");
        assertTrue(switch2.isValid());
        assertEquals(1, switch2.getIndex());
        assertEquals("switch2", switch2.getKey());

        SwitchArgument invalidSwitch = parser.parseSwitchArgument("invalidSwitch");
        assertFalse(invalidSwitch.isValid());
        assertEquals(-1, invalidSwitch.getIndex());
        assertEquals("invalidSwitch", invalidSwitch.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "value1", "-param2", "value2"});
        ParameterArgument param1 = parser.parseParameterArgument("param1");
        assertTrue(param1 != null);
        assertEquals(0, param1.getIndex());
        assertEquals("param1", param1.getKey());
        assertEquals("value1", param1.getValue());

        ParameterArgument param2 = parser.parseParameterArgument("param2");
        assertTrue(param2 != null);
        assertEquals(2, param2.getIndex());
        assertEquals("param2", param2.getKey());
        assertEquals("value2", param2.getValue());

        ParameterArgument invalidParam = parser.parseParameterArgument("invalidParam");
        assertNull(invalidParam);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> args = parser.parseStringArgument();
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0).getValue());
        assertEquals("arg2", args.get(1).getValue());
        assertEquals("arg3", args.get(2).getValue());
    }
}

