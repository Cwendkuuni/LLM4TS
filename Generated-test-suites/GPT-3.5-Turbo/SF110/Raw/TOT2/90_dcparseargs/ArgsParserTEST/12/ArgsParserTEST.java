package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--test"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("test");
        assertTrue(switchArgument.isValid());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument paramArgument = argsParser.parseParameterArgument("key");
        assertEquals("value", paramArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertEquals(3, stringArgs.size());
    }

}