package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {
    ArgsParser parser;

    @Before
    public void setUp() {
        parser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());

        parser.setArgs(new String[]{});
        assertEquals(0, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);

        parser.setArgs(new String[]{});
        args = parser.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void testSetArgs() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);

        parser.setArgs(new String[]{});
        args = parser.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgsLeftCount() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsLeftCount());

        parser.parseSwitchArgument("arg1");
        parser.parseSwitchArgument("arg2");
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        parser.setArgs(new String[]{"--arg1", "-arg2"});
        SwitchArgument switch1 = parser.parseSwitchArgument("arg1");
        assertNotNull(switch1);
        assertEquals(0, switch1.getIndex());
        assertEquals("arg1", switch1.getKey());
        assertEquals(true, switch1.isSwitch());

        SwitchArgument switch2 = parser.parseSwitchArgument("arg2");
        assertNotNull(switch2);
        assertEquals(1, switch2.getIndex());
        assertEquals("arg2", switch2.getKey());
        assertEquals(true, switch2.isSwitch());

        SwitchArgument switch3 = parser.parseSwitchArgument("arg3");
        assertNotNull(switch3);
        assertEquals(-1, switch3.getIndex());
        assertEquals("arg3", switch3.getKey());
        assertEquals(false, switch3.isSwitch());
    }

    @Test
    public void testParseParameterArgument() {
        parser.setArgs(new String[]{"--arg1", "value1", "-arg2", "value2"});
        ParameterArgument parameter1 = parser.parseParameterArgument("arg1");
        assertNotNull(parameter1);
        assertEquals(0, parameter1.getIndex());
        assertEquals("arg1", parameter1.getKey());
        assertEquals("value1", parameter1.getValue());

        ParameterArgument parameter2 = parser.parseParameterArgument("arg2");
        assertNotNull(parameter2);
        assertEquals(2, parameter2.getIndex());
        assertEquals("arg2", parameter2.getKey());
        assertEquals("value2", parameter2.getValue());
    }

    @Test
    public void testParseStringArgument() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        List<StringArgument> arguments = parser.parseStringArgument();
        assertNotNull(arguments);
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0).getValue());
        assertEquals("arg2", arguments.get(1).getValue());

        arguments = parser.parseStringArgument();
        assertNotNull(arguments);
        assertEquals(0, arguments.size());
    }
}