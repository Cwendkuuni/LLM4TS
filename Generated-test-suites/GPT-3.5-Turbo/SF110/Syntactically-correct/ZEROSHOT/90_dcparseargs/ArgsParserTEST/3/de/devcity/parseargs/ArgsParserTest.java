package de.devcity.parseargs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }
    
    @Test
    public void testConstructorWithArgs() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        int expected = args.length;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
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
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", null};
        argsParser.setArgs(args);
        int expected = 2;
        int actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgumentWithLongKey() {
        String key = "key";
        String[] args = {"--key"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, key, true);
        SwitchArgument actual = argsParser.parseSwitchArgument(key);
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseSwitchArgumentWithShortKey() {
        String key = "k";
        String[] args = {"-k"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, key, true);
        SwitchArgument actual = argsParser.parseSwitchArgument(key);
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
    }
    
    @Test
    public void testParseSwitchArgumentNotFound() {
        String key = "key";
        String[] args = {};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, key, false);
        SwitchArgument actual = argsParser.parseSwitchArgument(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgumentWithLongKey() {
        String key = "key";
        String value = "value";
        String[] args = {"--key", "value"};
        argsParser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, key, value);
        ParameterArgument actual = argsParser.parseParameterArgument(key);
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
    
    @Test
    public void testParseParameterArgumentWithShortKey() {
        String key = "k";
        String value = "value";
        String[] args = {"-k", "value"};
        argsParser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, key, value);
        ParameterArgument actual = argsParser.parseParameterArgument(key);
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
    
    @Test
    public void testParseParameterArgumentNotFound() {
        String key = "key";
        String[] args = {};
        argsParser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgumentWithValueAtEnd() {
        String key = "key";
        String value = "value";
        String[] args = {"-k", "value"};
        argsParser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, key, value);
        ParameterArgument actual = argsParser.parseParameterArgument(key);
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        StringArgument arg1 = new StringArgument(0, "arg1");
        StringArgument arg2 = new StringArgument(1, "arg2");
        java.util.List<StringArgument> expected = java.util.Arrays.asList(arg1, arg2);
        java.util.List<StringArgument> actual = argsParser.parseStringArgument();
        assertEquals(expected, actual);
        assertNull(argsParser.getArgs()[0]);
        assertNull(argsParser.getArgs()[1]);
    }
}