package de.devcity.parseargs;

import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertEquals(2, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"--switch"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch");
        assertTrue(switchArgument.isValid());
        assertEquals("--switch", argsParser.getArgs()[switchArgument.getIndex()]);
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        String[] args = {"-s"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("s");
        assertTrue(switchArgument.isValid());
        assertEquals("-s", argsParser.getArgs()[switchArgument.getIndex()]);
    }

    @Test
    public void testParseSwitchArgumentInvalidKey() {
        String[] args = {"-s"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("invalid");
        assertFalse(switchArgument.isValid());
        assertEquals(-1, switchArgument.getIndex());
    }

    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        String[] args = {"--param", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("param");
        assertNotNull(parameterArgument);
        assertEquals("value", parameterArgument.getValue());
        assertEquals("--param", argsParser.getArgs()[parameterArgument.getIndex()]);
        assertNull(argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        String[] args = {"--param"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("param");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        String[] args = {"-p", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("p");
        assertNotNull(parameterArgument);
        assertEquals("value", parameterArgument.getValue());
        assertEquals("-p", argsParser.getArgs()[parameterArgument.getIndex()]);
        assertNull(argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentInvalidKey() {
        String[] args = {"-p", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("invalid");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        assertNull(argsParser.getArgs()[stringArguments.get(0).getIndex()]);
        assertNull(argsParser.getArgs()[stringArguments.get(1).getIndex()]);
    }

    @Test
    public void testParseStringArgumentWithNullArgs() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        assertNull(argsParser.getArgs()[stringArguments.get(0).getIndex()]);
        assertNull(argsParser.getArgs()[stringArguments.get(1).getIndex()]);
    }
}