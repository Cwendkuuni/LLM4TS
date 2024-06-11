import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
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
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLongKeyExists() {
        argsParser.setArgs(new String[]{"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key2");
        assertTrue(switchArgument.isExists());
        assertEquals(1, switchArgument.getIndex());
        assertEquals("key2", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentLongKeyNotExists() {
        argsParser.setArgs(new String[]{"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key3");
        assertFalse(switchArgument.isExists());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("key3", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentShortKeyExists() {
        argsParser.setArgs(new String[]{"-k", "-v"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k");
        assertTrue(switchArgument.isExists());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("k", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentShortKeyNotExists() {
        argsParser.setArgs(new String[]{"-k", "-v"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("x");
        assertFalse(switchArgument.isExists());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("x", switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgumentLongKeyExistsWithValue() {
        argsParser.setArgs(new String[]{"--key1", "value1", "--key2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("key1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentLongKeyExistsWithoutValue() {
        argsParser.setArgs(new String[]{"--key1", "--key2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentLongKeyNotExists() {
        argsParser.setArgs(new String[]{"--key1", "value1", "--key2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key3");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentShortKeyExistsWithValue() {
        argsParser.setArgs(new String[]{"-k", "value"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("k", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentShortKeyExistsWithoutValue() {
        argsParser.setArgs(new String[]{"-k", "-v"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgumentShortKeyNotExists() {
        argsParser.setArgs(new String[]{"-k", "value", "-v"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("x");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals(0, stringArguments.get(0).getIndex());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals(1, stringArguments.get(1).getIndex());
        assertEquals("arg3", stringArguments.get(2).getValue());
        assertEquals(2, stringArguments.get(2).getIndex());
    }
}