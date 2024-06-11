
package de.devcity.parseargs;

import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentWithLongKeyAndValidArgument() {
        argsParser.setArgs(new String[]{"--switch"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch");
        assertNotNull(switchArgument);
        assertTrue(switchArgument.isLongKey());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("switch", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithLongKeyAndInvalidArgument() {
        argsParser.setArgs(new String[]{"--switch"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("invalidKey");
        assertNotNull(switchArgument);
        assertTrue(switchArgument.isLongKey());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("invalidKey", switchArgument.getKey());
        assertFalse(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithShortKeyAndValidArgument() {
        argsParser.setArgs(new String[]{"-s"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("s");
        assertNotNull(switchArgument);
        assertFalse(switchArgument.isLongKey());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("s", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithShortKeyAndInvalidArgument() {
        argsParser.setArgs(new String[]{"-s"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("invalidKey");
        assertNotNull(switchArgument);
        assertFalse(switchArgument.isLongKey());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("invalidKey", switchArgument.getKey());
        assertFalse(switchArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithLongKeyAndValidArgument() {
        argsParser.setArgs(new String[]{"--param", "value"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("param");
        assertNotNull(parameterArgument);
        assertTrue(parameterArgument.isLongKey());
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("param", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithLongKeyAndInvalidArgument() {
        argsParser.setArgs(new String[]{"--param"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("invalidKey");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentWithShortKeyAndValidArgument() {
        argsParser.setArgs(new String[]{"-p", "value"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("p");
        assertNotNull(parameterArgument);
        assertFalse(parameterArgument.isLongKey());
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("p", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithShortKeyAndInvalidArgument() {
        argsParser.setArgs(new String[]{"-p"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("invalidKey");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> expectedArguments = new ArrayList<>();
        expectedArguments.add(new StringArgument(0, "arg1"));
        expectedArguments.add(new StringArgument(1, "arg2"));
        expectedArguments.add(new StringArgument(2, "arg3"));
        List<StringArgument> actualArguments = argsParser.parseStringArgument();
        assertEquals(expectedArguments.size(), actualArguments.size());
        for (int i = 0; i < actualArguments.size(); i++) {
            assertEquals(expectedArguments.get(i).getIndex(), actualArguments.get(i).getIndex());
            assertEquals(expectedArguments.get(i).getValue(), actualArguments.get(i).getValue());
        }
    }
}
