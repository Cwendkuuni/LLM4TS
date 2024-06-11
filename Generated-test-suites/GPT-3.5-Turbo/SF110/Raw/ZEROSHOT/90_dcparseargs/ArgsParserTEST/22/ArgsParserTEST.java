package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key1", "value1", "-key2"});
        assertEquals(new SwitchArgument(0, "key1", true), argsParser.parseSwitchArgument("key1"));
        assertEquals(new SwitchArgument(2, "key2", true), argsParser.parseSwitchArgument("key2"));
        assertEquals(new SwitchArgument(-1, "key3", false), argsParser.parseSwitchArgument("key3"));
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key1", "value1", "-key2", "value2"});
        assertEquals(new ParameterArgument(0, "key1", "value1"), argsParser.parseParameterArgument("key1"));
        assertEquals(new ParameterArgument(2, "key2", "value2"), argsParser.parseParameterArgument("key2"));
        assertNull(argsParser.parseParameterArgument("key3"));
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));
        assertEquals(expected, argsParser.parseStringArgument());
    }
}