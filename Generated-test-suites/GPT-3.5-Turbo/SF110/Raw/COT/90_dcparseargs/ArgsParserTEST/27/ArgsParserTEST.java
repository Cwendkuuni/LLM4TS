import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

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

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());

        parser.parseSwitchArgument("arg1");
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        SwitchArgument switch1 = parser.parseSwitchArgument("arg1");
        assertTrue(switch1.isSwitchPresent());
        assertEquals(0, switch1.getIndex());

        SwitchArgument switch2 = parser.parseSwitchArgument("arg2");
        assertTrue(switch2.isSwitchPresent());
        assertEquals(1, switch2.getIndex());

        SwitchArgument switch3 = parser.parseSwitchArgument("arg3");
        assertTrue(switch3.isSwitchPresent());
        assertEquals(2, switch3.getIndex());

        SwitchArgument switch4 = parser.parseSwitchArgument("arg4");
        assertFalse(switch4.isSwitchPresent());
        assertEquals(-1, switch4.getIndex());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "value1", "-arg2", "value2"});
        ParameterArgument param1 = parser.parseParameterArgument("arg1");
        assertNotNull(param1);
        assertEquals(0, param1.getIndex());
        assertEquals("value1", param1.getValue());

        ParameterArgument param2 = parser.parseParameterArgument("arg2");
        assertNotNull(param2);
        assertEquals(2, param2.getIndex());
        assertEquals("value2", param2.getValue());

        ParameterArgument param3 = parser.parseParameterArgument("arg3");
        assertNull(param3);
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


// Paste generated test cases here

