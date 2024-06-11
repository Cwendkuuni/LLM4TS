package de.devcity.parseargs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.devcity.parseargs.arguments.*;
import java.util.*;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int result = argsParser.getArgsCount();
        assertEquals(3, result);
    }

    @Test
    public void testGetArgs() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] result = argsParser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testSetArgs() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] result = argsParser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullInput() {
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int result = argsParser.getArgsLeftCount();
        assertEquals(3, result);
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"-a", "--b", "-c"});
        SwitchArgument result1 = argsParser.parseSwitchArgument("a");
        assertEquals(0, result1.getIndex());
        assertTrue(result1.isSwitched());

        SwitchArgument result2 = argsParser.parseSwitchArgument("b");
        assertEquals(1, result2.getIndex());
        assertTrue(result2.isSwitched());
        
        SwitchArgument result3 = argsParser.parseSwitchArgument("c");
        assertEquals(2, result3.getIndex());
        assertTrue(result3.isSwitched());
        
        SwitchArgument result4 = argsParser.parseSwitchArgument("d");
        assertEquals(-1, result4.getIndex());
        assertFalse(result4.isSwitched());
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--key1", "value1", "--key2=value2", "-key3"});
        ParameterArgument result1 = argsParser.parseParameterArgument("key1");
        assertEquals(0, result1.getIndex());
        assertEquals("value1", result1.getValue());

        ParameterArgument result2 = argsParser.parseParameterArgument("key2");
        assertEquals(1, result2.getIndex());
        assertEquals("value2", result2.getValue());
        
        ParameterArgument result3 = argsParser.parseParameterArgument("key3");
        assertNull(result3);
        
        ParameterArgument result4 = argsParser.parseParameterArgument("key4");
        assertNull(result4);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg3", result.get(2).getValue());
    }
}