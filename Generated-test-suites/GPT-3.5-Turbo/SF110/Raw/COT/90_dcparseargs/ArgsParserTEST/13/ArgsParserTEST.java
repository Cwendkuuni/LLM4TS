import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int count = parser.getArgsCount();
        assertEquals(2, count);
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int count = parser.getArgsLeftCount();
        assertEquals(2, count);
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch1", "-s", "--switch2"});
        SwitchArgument switch1 = parser.parseSwitchArgument("switch1");
        assertTrue(switch1.isValid());
        assertEquals(0, switch1.getIndex());
        assertEquals("switch1", switch1.getKey());

        SwitchArgument switch2 = parser.parseSwitchArgument("switch2");
        assertTrue(switch2.isValid());
        assertEquals(2, switch2.getIndex());
        assertEquals("switch2", switch2.getKey());

        SwitchArgument invalidSwitch = parser.parseSwitchArgument("invalid");
        assertFalse(invalidSwitch.isValid());
        assertEquals(-1, invalidSwitch.getIndex());
        assertEquals("invalid", invalidSwitch.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "value1", "-p", "value2"});
        ParameterArgument param1 = parser.parseParameterArgument("param1");
        assertNotNull(param1);
        assertTrue(param1.isValid());
        assertEquals(0, param1.getIndex());
        assertEquals("param1", param1.getKey());
        assertEquals("value1", param1.getValue());

        ParameterArgument param2 = parser.parseParameterArgument("param2");
        assertNull(param2);

        ParameterArgument param3 = parser.parseParameterArgument("p");
        assertNotNull(param3);
        assertTrue(param3.isValid());
        assertEquals(2, param3.getIndex());
        assertEquals("p", param3.getKey());
        assertEquals("value2", param3.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> args = parser.parseStringArgument();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0).getValue());
        assertEquals("arg2", args.get(1).getValue());
    }
}
