package de.devcity.parseargs;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int expected = 3;
        int actual = argsParser.getArgsCount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        String[] expected = args;
        String[] actual = argsParser.getArgs();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        String[] expected = args;
        String[] actual = argsParser.getArgs();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", null, "arg3"});
        int expected = 2;
        int actual = argsParser.getArgsLeftCount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument_longKey_found() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--key1", "--key2", "--key3"});
        SwitchArgument expected = new SwitchArgument(0, "key1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument_longKey_notFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--key1", "--key2", "--key3"});
        SwitchArgument expected = new SwitchArgument(-1, "key4", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("key4");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument_shortKey_found() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-k1", "--k2", "-k3"});
        SwitchArgument expected = new SwitchArgument(0, "k1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("k1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument_shortKey_notFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-k1", "--k2", "-k3"});
        SwitchArgument expected = new SwitchArgument(-1, "k4", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("k4");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_longKey_found() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--key1", "value1", "--key2", "value2"});
        ParameterArgument expected = new ParameterArgument(0, "key1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_longKey_noValue() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--key1", "--key2", "value2"});
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_longKey_notFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--key1", "value1", "--key2", "value2"});
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("key3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_shortKey_found() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-k1", "value1", "--k2", "value2"});
        ParameterArgument expected = new ParameterArgument(0, "k1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("k1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_shortKey_noValue() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-k1", "-k2", "value2"});
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("k1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseParameterArgument_shortKey_notFound() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-k1", "value1", "--k2", "value2"});
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("k3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        ArrayList<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));
        ArrayList<StringArgument> actual = argsParser.parseStringArgument();
        Assert.assertEquals(expected, actual);
    }
}