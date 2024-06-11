package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        int expected = 2;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgsCountWithEmptyArgs() {
        argsParser.setArgs(new String[]{});
        int expected = 0;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        String[] expected = new String[]{"arg1", "arg2"};
        String[] actual = argsParser.getArgs();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        int expected = 2;
        int actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgsLeftCountWithNullArgs() {
        argsParser.setArgs(new String[]{null, null});
        int expected = 0;
        int actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"--key"});
        SwitchArgument expected = new SwitchArgument(0, "key", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("key");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgumentWithNonexistentKey() {
        argsParser.setArgs(new String[]{"--otherkey"});
        SwitchArgument expected = new SwitchArgument(-1, "key", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("key");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--key", "value"});
        ParameterArgument expected = new ParameterArgument(0, "key", "value");
        ParameterArgument actual = argsParser.parseParameterArgument("key");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgumentWithNonexistentKey() {
        argsParser.setArgs(new String[]{"--otherkey", "value"});
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("key");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        List<StringArgument> actual = argsParser.parseStringArgument();
        assertEquals(expected, actual);
    }
}