package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    // Test case for the constructor ArgsParser()
    @Test
    public void testConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }

    // Test case for the constructor ArgsParser(String[] args)
    @Test
    public void testConstructorWithArgs() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for getArgsCount()
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
    }

    // Test case for getArgs()
    @Test
    public void testGetArgs() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for setArgs(String[] args)
    @Test
    public void testSetArgs() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for getArgsLeftCount()
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    // Test case for parseSwitchArgument(String key)
    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertNotNull(switchArg);
        assertEquals(0, switchArg.getIndex());
        assertEquals("a", switchArg.getKey());
        assertTrue(switchArg.getValue());

        switchArg = parser.parseSwitchArgument("b");
        assertNotNull(switchArg);
        assertEquals(-1, switchArg.getIndex());
        assertEquals("b", switchArg.getKey());
        assertFalse(switchArg.getValue());
    }

    // Test case for parseParameterArgument(String key)
    @Test
    public void testParseParameterArgument() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("key");
        assertNotNull(paramArg);
        assertEquals(1, paramArg.getIndex());
        assertEquals("key", paramArg.getKey());
        assertEquals("value", paramArg.getValue());

        paramArg = parser.parseParameterArgument("unknown");
        assertNull(paramArg);
    }

    // Test case for parseStringArgument()
    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
        assertEquals(0, stringArgs.get(0).getIndex());
        assertEquals("-a", stringArgs.get(0).getValue());
        assertEquals(1, stringArgs.get(1).getIndex());
        assertEquals("--key", stringArgs.get(1).getValue());
        assertEquals(2, stringArgs.get(2).getIndex());
        assertEquals("value", stringArgs.get(2).getValue());
    }
}