
package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsCountWithNullArgs() {
        assertThrows(NullPointerException.class, () -> {
            argsParser.setArgs(null);
            argsParser.getArgsCount();
        });
    }

    @Test
    public void testGetArgs() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgsWithNullArgs() {
        assertThrows(NullPointerException.class, () -> {
            argsParser.setArgs(null);
        });
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentWithLongKey() {
        String[] args = {"--verbose", "--help"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("verbose");
        assertNotNull(switchArgument);
        assertEquals(0, switchArgument.getIndex());
        assertEquals("verbose", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithShortKey() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertNotNull(switchArgument);
        assertEquals(0, switchArgument.getIndex());
        assertEquals("a", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidKey() {
        String[] args = {"--verbose", "--help"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("invalid");
        assertNotNull(switchArgument);
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("invalid", switchArgument.getKey());
        assertFalse(switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidKeyAndNoValue() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("c");
        assertNotNull(switchArgument);
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("c", switchArgument.getKey());
        assertFalse(switchArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithLongKey() {
        String[] args = {"--name", "John"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("name");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("name", parameterArgument.getKey());
        assertEquals("John", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithShortKey() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("a");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "--help", "-b"};
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals(0, stringArguments.get(0).getIndex());
        assertEquals("a", stringArguments.get(0).getValue());
        assertEquals(1, stringArguments.get(1).getIndex());
        assertEquals("--help", stringArguments.get(1).getValue());
        assertEquals(2, stringArguments.get(2).getIndex());
        assertEquals("b", stringArguments.get(2).getValue());
    }
}

