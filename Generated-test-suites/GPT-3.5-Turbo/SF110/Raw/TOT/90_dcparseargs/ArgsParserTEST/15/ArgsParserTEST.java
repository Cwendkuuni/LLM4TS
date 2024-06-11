import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParserTest {

    ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        // Test case 1: When args is empty
        argsParser.setArgs(new String[]{});
        Assert.assertEquals(0, argsParser.getArgsCount());

        // Test case 2: When args has one element
        argsParser.setArgs(new String[]{"arg1"});
        Assert.assertEquals(1, argsParser.getArgsCount());

        // Test case 3: When args has multiple elements
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        Assert.assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        // Test case 1: When args is null
        argsParser.setArgs(null);
        Assert.assertNull(argsParser.getArgs());

        // Test case 2: When args has elements
        String[] inputArgs = {"arg1", "arg2"};
        argsParser.setArgs(inputArgs);
        Assert.assertArrayEquals(inputArgs, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        // Test case 1: When args is null
        argsParser.setArgs(null);
        Assert.assertNull(argsParser.getArgs());

        // Test case 2: When args has elements
        String[] inputArgs = {"arg1", "arg2"};
        argsParser.setArgs(inputArgs);
        Assert.assertArrayEquals(inputArgs, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        // Test case 1: When innerArgs is empty
        argsParser.setArgs(new String[]{});
        Assert.assertEquals(0, argsParser.getArgsLeftCount());

        // Test case 2: When innerArgs has one element
        argsParser.setArgs(new String[]{"arg1"});
        Assert.assertEquals(1, argsParser.getArgsLeftCount());

        // Test case 3: When innerArgs has multiple elements
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        Assert.assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        // Test case 1: When key is a long key
        argsParser.setArgs(new String[]{"--key1"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key1");
        Assert.assertTrue(switchArgument.isLongKey());
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("key1", switchArgument.getKey());
        Assert.assertTrue(switchArgument.isSwitchOn());

        // Test case 2: When key is a short key
        argsParser.setArgs(new String[]{"-k"});
        switchArgument = argsParser.parseSwitchArgument("k");
        Assert.assertFalse(switchArgument.isLongKey());
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("k", switchArgument.getKey());
        Assert.assertTrue(switchArgument.isSwitchOn());

        // Test case 3: When key is not found
        argsParser.setArgs(new String[]{"-k"});
        switchArgument = argsParser.parseSwitchArgument("key");
        Assert.assertFalse(switchArgument.isLongKey());
        Assert.assertEquals(-1, switchArgument.getIndex());
        Assert.assertEquals("key", switchArgument.getKey());
        Assert.assertFalse(switchArgument.isSwitchOn());
    }

    @Test
    public void testParseParameterArgument() {
        // Test case 1: When key is a long key and has value
        argsParser.setArgs(new String[]{"--key1", "value1"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        Assert.assertTrue(parameterArgument.isLongKey());
        Assert.assertEquals(0, parameterArgument.getIndex());
        Assert.assertEquals("key1", parameterArgument.getKey());
        Assert.assertEquals("value1", parameterArgument.getValue());

        // Test case 2: When key is a long key but has no value
        argsParser.setArgs(new String[]{"--key1"});
        parameterArgument = argsParser.parseParameterArgument("key1");
        Assert.assertNull(parameterArgument);

        // Test case 3: When key is a short key and has value
        argsParser.setArgs(new String[]{"-k", "value1"});
        parameterArgument = argsParser.parseParameterArgument("k");
        Assert.assertFalse(parameterArgument.isLongKey());
        Assert.assertEquals(0, parameterArgument.getIndex());
        Assert.assertEquals("k", parameterArgument.getKey());
        Assert.assertEquals("value1", parameterArgument.getValue());

        // Test case 4: When key is a short key but has no value
        argsParser.setArgs(new String[]{"-k"});
        parameterArgument = argsParser.parseParameterArgument("k");
        Assert.assertNull(parameterArgument);

        // Test case 5: When key is not found
        argsParser.setArgs(new String[]{"-k"});
        parameterArgument = argsParser.parseParameterArgument("key");
        Assert.assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        // Test case 1: When innerArgs is empty
        argsParser.setArgs(new String[]{});
        List<StringArgument> result = argsParser.parseStringArgument();
        Assert.assertEquals(0, result.size());

        // Test case 2: When innerArgs has one element
        argsParser.setArgs(new String[]{"arg1"});
        result = argsParser.parseStringArgument();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(0, result.get(0).getIndex());
        Assert.assertEquals("arg1", result.get(0).getValue());

        // Test case 3: When innerArgs has multiple elements
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        result = argsParser.parseStringArgument();
        Assert.assertEquals(3, result.size());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(i, result.get(i).getIndex());
            Assert.assertEquals("arg" + (i + 1), result.get(i).getValue());
        }
    }

}