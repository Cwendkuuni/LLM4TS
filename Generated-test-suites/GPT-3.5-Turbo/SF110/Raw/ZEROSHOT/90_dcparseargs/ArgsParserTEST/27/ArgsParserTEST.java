package de.devcity.parseargs;

import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        Assert.assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        Assert.assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        Assert.assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", null, "arg3"});
        Assert.assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        argsParser.setArgs(new String[]{"--key"});
        SwitchArgument result = argsParser.parseSwitchArgument("key");
        Assert.assertEquals(new SwitchArgument(0, "key", true), result);
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        argsParser.setArgs(new String[]{"-k"});
        SwitchArgument result = argsParser.parseSwitchArgument("k");
        Assert.assertEquals(new SwitchArgument(0, "k", true), result);
    }

    @Test
    public void testParseSwitchArgumentInvalidKey() {
        argsParser.setArgs(new String[]{"--key"});
        SwitchArgument result = argsParser.parseSwitchArgument("invalid");
        Assert.assertEquals(new SwitchArgument(-1, "invalid", false), result);
    }

    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        argsParser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        Assert.assertEquals(new ParameterArgument(0, "key", "value"), result);
    }

    @Test
    public void testParseParameterArgumentShortKeyWithValue() {
        argsParser.setArgs(new String[]{"-k", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        Assert.assertEquals(new ParameterArgument(0, "k", "value"), result);
    }

    @Test
    public void testParseParameterArgumentInvalidKey() {
        argsParser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("invalid");
        Assert.assertNull(result);
    }

    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        argsParser.setArgs(new String[]{"--key"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        Assert.assertNull(result);
    }

    @Test
    public void testParseParameterArgumentShortKeyWithoutValue() {
        argsParser.setArgs(new String[]{"-k"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        Assert.assertNull(result);
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> result = argsParser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));
        Assert.assertEquals(expected, result);
    }
}