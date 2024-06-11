package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setup() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());

        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(args, argsParser.getArgs());

        argsParser.setArgs(new String[]{});
        assertEquals(new String[]{}, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", null, "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.setArgs(new String[]{null, null});
        assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"--switch", "-s", "-switch"});
        SwitchArgument switchArg1 = argsParser.parseSwitchArgument("switch");
        SwitchArgument switchArg2 = argsParser.parseSwitchArgument("s");
        SwitchArgument switchArg3 = argsParser.parseSwitchArgument("nonexistent");

        assertTrue(switchArg1.isSwitchFound());
        assertEquals(0, switchArg1.getIndex());
        assertEquals("switch", switchArg1.getKey());

        assertTrue(switchArg2.isSwitchFound());
        assertEquals(1, switchArg2.getIndex());
        assertEquals("s", switchArg2.getKey());

        assertTrue(!switchArg3.isSwitchFound());
        assertEquals(-1, switchArg3.getIndex());
        assertEquals("nonexistent", switchArg3.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--param=value", "-p=value", "-param=value", "--param"});

        ParameterArgument paramArg1 = argsParser.parseParameterArgument("param");
        ParameterArgument paramArg2 = argsParser.parseParameterArgument("p");
        ParameterArgument paramArg3 = argsParser.parseParameterArgument("nonexistent");

        assertEquals(0, paramArg1.getIndex());
        assertEquals("param", paramArg1.getKey());
        assertEquals("value", paramArg1.getValue());

        assertEquals(1, paramArg2.getIndex());
        assertEquals("p", paramArg2.getKey());
        assertEquals("value", paramArg2.getValue());

        assertNull(paramArg3);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.parseStringArgument().size());

        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.parseStringArgument().size());
    }
}