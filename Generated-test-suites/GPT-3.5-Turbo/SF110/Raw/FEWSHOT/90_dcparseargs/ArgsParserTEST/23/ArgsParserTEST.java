package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

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
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("-arg1");
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "-arg2", "--arg3", "arg4", "--arg5"};
        argsParser.setArgs(args);

        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("arg1");
        assertFalse(switchArgument1.isLongKey());
        assertEquals(-1, switchArgument1.getIndex());
        assertFalse(switchArgument1.isFound());

        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("arg2");
        assertFalse(switchArgument2.isLongKey());
        assertEquals(1, switchArgument2.getIndex());
        assertTrue(switchArgument2.isFound());

        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("arg3");
        assertTrue(switchArgument3.isLongKey());
        assertEquals(2, switchArgument3.getIndex());
        assertTrue(switchArgument3.isFound());

        SwitchArgument switchArgument4 = argsParser.parseSwitchArgument("arg4");
        assertFalse(switchArgument4.isLongKey());
        assertEquals(-1, switchArgument4.getIndex());
        assertFalse(switchArgument4.isFound());

        SwitchArgument switchArgument5 = argsParser.parseSwitchArgument("arg5");
        assertTrue(switchArgument5.isLongKey());
        assertEquals(4, switchArgument5.getIndex());
        assertTrue(switchArgument5.isFound());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "-arg2", "--arg3", "value1", "-arg4value2"};
        argsParser.setArgs(args);

        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("arg1");
        assertFalse(parameterArgument1.isLongKey());
        assertEquals(-1, parameterArgument1.getIndex());
        assertNull(parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("arg2");
        assertFalse(parameterArgument2.isLongKey());
        assertEquals(-1, parameterArgument2.getIndex());
        assertNull(parameterArgument2.getValue());

        ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("arg3");
        assertTrue(parameterArgument3.isLongKey());
        assertEquals(-1, parameterArgument3.getIndex());
        assertNull(parameterArgument3.getValue());

        ParameterArgument parameterArgument4 = argsParser.parseParameterArgument("arg4");
        assertFalse(parameterArgument4.isLongKey());
        assertEquals(-1, parameterArgument4.getIndex());
        assertNull(parameterArgument4.getValue());

        ParameterArgument parameterArgument5 = argsParser.parseParameterArgument("value1");
        assertFalse(parameterArgument5.isLongKey());
        assertEquals(-1, parameterArgument5.getIndex());
        assertNull(parameterArgument5.getValue());

        ParameterArgument parameterArgument6 = argsParser.parseParameterArgument("value2");
        assertFalse(parameterArgument6.isLongKey());
        assertEquals(-1, parameterArgument6.getIndex());
        assertNull(parameterArgument6.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "-arg2", "--arg3", "value1", "-arg4value2"};
        argsParser.setArgs(args);

        List<StringArgument> stringArguments = argsParser.parseStringArgument();

        assertEquals(5, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("-arg2", stringArguments.get(1).getValue());
        assertEquals("--arg3", stringArguments.get(2).getValue());
        assertEquals("value1", stringArguments.get(3).getValue());
        assertEquals("-arg4value2", stringArguments.get(4).getValue());
    }
}