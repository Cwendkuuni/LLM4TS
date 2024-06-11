package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsCount());

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        assertNull(argsParser.getArgs());

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        assertNull(argsParser.getArgs());

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsLeftCount());

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("arg1");
        argsParser.parseParameterArgument("arg2");

        assertEquals(1, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-switch", "--longSwitch"});

        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch");
        assertEquals("switch", switchArgument.getKey());
        assertTrue(switchArgument.isLongKey());
        assertEquals(0, switchArgument.getIndex());

        switchArgument = argsParser.parseSwitchArgument("longSwitch");
        assertEquals("longSwitch", switchArgument.getKey());
        assertTrue(switchArgument.isLongKey());
        assertEquals(1, switchArgument.getIndex());

        switchArgument = argsParser.parseSwitchArgument("nonexistentSwitch");
        assertEquals("nonexistentSwitch", switchArgument.getKey());
        assertFalse(switchArgument.isLongKey());
        assertEquals(-1, switchArgument.getIndex());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--parameter", "-p", "value"});

        ParameterArgument parameterArgument = argsParser.parseParameterArgument("parameter");
        assertEquals("parameter", parameterArgument.getKey());
        assertTrue(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());

        parameterArgument = argsParser.parseParameterArgument("p");
        assertEquals("p", parameterArgument.getKey());
        assertFalse(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(1, parameterArgument.getIndex());

        parameterArgument = argsParser.parseParameterArgument("nonexistentParameter");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "-switch", "arg2", "--parameter", "value", "arg3"});

        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());

        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals(0, stringArguments.get(0).getIndex());

        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals(2, stringArguments.get(1).getIndex());

        assertEquals("arg3", stringArguments.get(2).getValue());
        assertEquals(5, stringArguments.get(2).getIndex());
    }

}