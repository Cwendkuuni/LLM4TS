package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    // Test ArgsParser constructor with no arguments
    @Test
    public void testConstructorNoArguments() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }
    
    // Test ArgsParser constructor with arguments
    @Test
    public void testConstructorWithArguments() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test setArgs method with valid arguments
    @Test
    public void testSetArgsValidArguments() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test setArgs method with null arguments
    @Test(expected = NullPointerException.class)
    public void testSetArgsNullArguments() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    // Test getArgsCount method
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }
    
    // Test getArgsLeftCount method
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsLeftCount());
    }
    
    // Test parseSwitchArgument method
    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertNotNull(switchArg);
        assertEquals(0, switchArg.getIndex());
        assertTrue(switchArg.isFound());
    }
    
    // Test parseParameterArgument method
    @Test
    public void testParseParameterArgument() {
        String[] args = {"-a", "value1", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("b");
        assertNotNull(paramArg);
        assertEquals(3, paramArg.getIndex());
        assertEquals("value2", paramArg.getValue());
    }
    
    // Test parseStringArgument method
    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "value1", "-b", "value2", "extra"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
    }
}