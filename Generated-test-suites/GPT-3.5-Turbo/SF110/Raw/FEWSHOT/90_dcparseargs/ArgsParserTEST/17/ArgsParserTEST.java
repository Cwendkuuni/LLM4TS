
package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.SwitchArgument;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testConstructor() {
        assertNull(argsParser.getArgs());
    }

    @Test
    public void testConstructorWithArgs() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser = new ArgsParser(args);
        assertEquals(args.length, argsParser.getArgsCount());
        assertEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser.setArgs(args);
        assertEquals(args.length, argsParser.getArgsCount());
        assertEquals(args, argsParser.getArgs());
        assertEquals(args.length, argsParser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser.setArgs(args);
        assertEquals(args.length, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertTrue(switchArgument.isFound());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("a", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("c");
        assertFalse(switchArgument.isFound());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("c", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("flag");
        assertTrue(switchArgument.isFound());
        assertEquals(2, switchArgument.getIndex());
        assertEquals("flag", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("extra");
        assertFalse(switchArgument.isFound());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("extra", switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("flag");
        assertTrue(parameterArgument.isFound());
        assertEquals(2, parameterArgument.getIndex());
        assertEquals("flag", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());

        parameterArgument = argsParser.parseParameterArgument("a");
        assertFalse(parameterArgument.isFound());
        assertEquals(-1, parameterArgument.getIndex());
        assertEquals("a", parameterArgument.getKey());

        parameterArgument = argsParser.parseParameterArgument("extra");
        assertFalse(parameterArgument.isFound());
        assertEquals(-1, parameterArgument.getIndex());
        assertEquals("extra", parameterArgument.getKey());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = { "-a", "-b", "--flag", "value", "--", "extra" };
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(6, stringArguments.size());
        for (int i = 0; i < args.length; i++) {
            assertNull(argsParser.getArgs()[i]);
        }
    }
}
