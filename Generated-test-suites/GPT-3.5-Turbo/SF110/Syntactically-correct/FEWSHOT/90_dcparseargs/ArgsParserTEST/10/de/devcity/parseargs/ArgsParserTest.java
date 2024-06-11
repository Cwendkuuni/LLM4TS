package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

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

        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(args, argsParser.getArgs());

        args = new String[]{};
        argsParser.setArgs(args);
        assertEquals(args, argsParser.getArgs());

        assertNull(argsParser.getArgs());

        args = null;
        argsParser.setArgs(args);
        assertNull(argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.setArgs(new String[]{"arg1"});
        assertEquals(1, argsParser.getArgsLeftCount());

        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"--flag1", "-f", "--flag2", "arg1", "arg2"});

        SwitchArgument switchArgument = argsParser.parseSwitchArgument("flag1");
        assertTrue(switchArgument.isValid());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("flag1", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("flag2");
        assertTrue(switchArgument.isValid());
        assertEquals(2, switchArgument.getIndex());
        assertEquals("flag2", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("flag3");
        assertFalse(switchArgument.isValid());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("flag3", switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--param1=value1", "-pvalue2", "--param2", "value3", "arg1", "arg2"});

        ParameterArgument parameterArgument = argsParser.parseParameterArgument("param1");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("param1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = argsParser.parseParameterArgument("param2");
        assertNotNull(parameterArgument);
        assertEquals(2, parameterArgument.getIndex());
        assertEquals("param2", parameterArgument.getKey());
        assertEquals("value3", parameterArgument.getValue());

        parameterArgument = argsParser.parseParameterArgument("param3");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});

        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertNotNull(stringArguments);
        assertEquals(3, stringArguments.size());

        List<String> argumentValues = new ArrayList<String>();
        for (StringArgument stringArgument : stringArguments) {
            argumentValues.add(stringArgument.getValue());
        }

        assertTrue(argumentValues.contains("arg1"));
        assertTrue(argumentValues.contains("arg2"));
        assertTrue(argumentValues.contains("arg3"));
    }
}