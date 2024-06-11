package de.devcity.parseargs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgsParserTest {
    private ArgsParser parser;

    @Before
    public void setup() {
        parser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        assertEquals(0, parser.getArgsCount());

        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        assertNull(parser.getArgs());

        parser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        parser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        assertEquals(0, parser.getArgsLeftCount());

        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsLeftCount());

        parser.parseSwitchArgument("arg1");
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        parser.setArgs(new String[]{"--arg1", "-arg2", "--arg3"});

        SwitchArgument switchArgument1 = parser.parseSwitchArgument("arg1");
        assertTrue(switchArgument1.isPresent());
        assertEquals(0, switchArgument1.getIndex());
        assertEquals("arg1", switchArgument1.getKey());

        SwitchArgument switchArgument2 = parser.parseSwitchArgument("arg2");
        assertTrue(switchArgument2.isPresent());
        assertEquals(1, switchArgument2.getIndex());
        assertEquals("arg2", switchArgument2.getKey());

        SwitchArgument switchArgument3 = parser.parseSwitchArgument("arg3");
        assertTrue(switchArgument3.isPresent());
        assertEquals(2, switchArgument3.getIndex());
        assertEquals("arg3", switchArgument3.getKey());

        SwitchArgument switchArgument4 = parser.parseSwitchArgument("arg4");
        assertFalse(switchArgument4.isPresent());
        assertEquals(-1, switchArgument4.getIndex());
        assertEquals("arg4", switchArgument4.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        parser.setArgs(new String[]{"--param1", "value1", "-param2", "value2"});

        ParameterArgument parameterArgument1 = parser.parseParameterArgument("param1");
        assertTrue(parameterArgument1.isPresent());
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("param1", parameterArgument1.getKey());
        assertEquals("value1", parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("param2");
        assertTrue(parameterArgument2.isPresent());
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("param2", parameterArgument2.getKey());
        assertEquals("value2", parameterArgument2.getValue());

        ParameterArgument parameterArgument3 = parser.parseParameterArgument("param3");
        assertFalse(parameterArgument3.isPresent());
        assertEquals(-1, parameterArgument3.getIndex());
        assertEquals("param3", parameterArgument3.getKey());
    }

    @Test
    public void testParseStringArgument() {
        parser.setArgs(new String[]{"arg1", "arg2"});

        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getIndex());
        assertEquals("arg1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getIndex());
        assertEquals("arg2", stringArgument2.getValue());
    }
}