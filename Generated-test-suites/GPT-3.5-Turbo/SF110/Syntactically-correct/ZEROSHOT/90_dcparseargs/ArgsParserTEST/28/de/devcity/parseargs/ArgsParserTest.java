package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentWithLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1"});
        SwitchArgument argument = parser.parseSwitchArgument("key1");
        assertNotNull(argument);
        assertTrue(argument.isSwitch());
        assertEquals(0, argument.getIndex());
        assertEquals("key1", argument.getKey());
    }

    @Test
    public void testParseSwitchArgumentWithShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-k"});
        SwitchArgument argument = parser.parseSwitchArgument("k");
        assertNotNull(argument);
        assertTrue(argument.isSwitch());
        assertEquals(0, argument.getIndex());
        assertEquals("k", argument.getKey());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1"});
        SwitchArgument argument = parser.parseSwitchArgument("key2");
        assertNotNull(argument);
        assertFalse(argument.isSwitch());
        assertEquals(-1, argument.getIndex());
        assertEquals("key2", argument.getKey());
    }

    @Test
    public void testParseParameterArgumentWithLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1"});
        ParameterArgument argument = parser.parseParameterArgument("key1");
        assertNotNull(argument);
        assertEquals(0, argument.getIndex());
        assertEquals("key1", argument.getKey());
        assertEquals("value1", argument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-k", "value1"});
        ParameterArgument argument = parser.parseParameterArgument("k");
        assertNotNull(argument);
        assertEquals(0, argument.getIndex());
        assertEquals("k", argument.getKey());
        assertEquals("value1", argument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithInvalidKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1"});
        ParameterArgument argument = parser.parseParameterArgument("key2");
        assertNull(argument);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> arguments = parser.parseStringArgument();
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0).getValue());
        assertEquals("arg2", arguments.get(1).getValue());
    }

}