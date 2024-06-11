package de.devcity.parseargs;

import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Before;
import org.junit.Test;

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
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[]{"arg1", null, "arg3"});
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"-f", "--verbose", "-x", "--foo"});
        
        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("f");
        assertEquals(0, switchArgument1.getIndex());
        assertEquals("f", switchArgument1.getKey());
        assertTrue(switchArgument1.isValue());
        
        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("verbose");
        assertEquals(1, switchArgument2.getIndex());
        assertEquals("verbose", switchArgument2.getKey());
        assertTrue(switchArgument2.isValue());
        
        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("x");
        assertEquals(2, switchArgument3.getIndex());
        assertEquals("x", switchArgument3.getKey());
        assertTrue(switchArgument3.isValue());
        
        SwitchArgument switchArgument4 = argsParser.parseSwitchArgument("foo");
        assertEquals(3, switchArgument4.getIndex());
        assertEquals("foo", switchArgument4.getKey());
        assertTrue(switchArgument4.isValue());
        
        SwitchArgument switchArgument5 = argsParser.parseSwitchArgument("bar");
        assertEquals(-1, switchArgument5.getIndex());
        assertEquals("bar", switchArgument5.getKey());
        assertFalse(switchArgument5.isValue());
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--file", "test.txt", "-o", "output.txt"});
        
        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("file");
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("file", parameterArgument1.getKey());
        assertEquals("test.txt", parameterArgument1.getValue());
        
        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("o");
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("o", parameterArgument2.getKey());
        assertEquals("output.txt", parameterArgument2.getValue());
        
        ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("input");
        assertNull(parameterArgument3);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
      
        assertEquals(0, stringArguments.get(0).getIndex());
        assertEquals("arg1", stringArguments.get(0).getValue());
        
        assertEquals(1, stringArguments.get(1).getIndex());
        assertEquals("arg2", stringArguments.get(1).getValue());
        
        assertEquals(2, stringArguments.get(2).getIndex());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }
}