import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgs_ThrowsNullPointerException() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs_ValidArgs_SetsArgsAndInnerArgs() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        Assert.assertArrayEquals(args, argsParser.getArgs());
        Assert.assertArrayEquals(args, argsParser.innerArgs);
    }

    @Test
    public void testGetArgsCount_EmptyArgs_ReturnsZero() {
        argsParser.setArgs(new String[0]);
        Assert.assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsCount_NonEmptyArgs_ReturnsCount() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        Assert.assertEquals(args.length, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsLeftCount_AllArgsParsed_ReturnsZero() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        Assert.assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount_UnparsedArgs_ReturnsCount() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        Assert.assertEquals(args.length, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_LongKeyExists_ReturnsSwitchArgument() {
        String[] args = {"--key"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key");
        Assert.assertNotNull(switchArgument);
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("key", switchArgument.getKey());
        Assert.assertTrue(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgument_LongKeyNotExists_ReturnsSwitchArgument() {
        String[] args = {"--other"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key");
        Assert.assertNotNull(switchArgument);
        Assert.assertEquals(-1, switchArgument.getIndex());
        Assert.assertEquals("key", switchArgument.getKey());
        Assert.assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyExists_ReturnsSwitchArgument() {
        String[] args = {"-k"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k");
        Assert.assertNotNull(switchArgument);
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("k", switchArgument.getKey());
        Assert.assertTrue(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyNotExists_ReturnsSwitchArgument() {
        String[] args = {"-x"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k");
        Assert.assertNotNull(switchArgument);
        Assert.assertEquals(-1, switchArgument.getIndex());
        Assert.assertEquals("k", switchArgument.getKey());
        Assert.assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithValue_ReturnsParameterArgument() {
        String[] args = {"--key", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key");
        Assert.assertNotNull(parameterArgument);
        Assert.assertEquals(0, parameterArgument.getIndex());
        Assert.assertEquals("key", parameterArgument.getKey());
        Assert.assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithoutValue_ReturnsNull() {
        String[] args = {"--key"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key");
        Assert.assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgument_LongKeyNotExists_ReturnsNull() {
        String[] args = {"--other"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key");
        Assert.assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithValue_ReturnsParameterArgument() {
        String[] args = {"-k", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        Assert.assertNotNull(parameterArgument);
        Assert.assertEquals(0, parameterArgument.getIndex());
        Assert.assertEquals("k", parameterArgument.getKey());
        Assert.assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithoutValue_ReturnsNull() {
        String[] args = {"-k"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        Assert.assertNull(parameterArgument);
    }

    @Test
    public void testParseParameterArgument_ShortKeyNotExists_ReturnsNull() {
        String[] args = {"-x"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        Assert.assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument_AllArgs_ReturnsAllStringArguments() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        Assert.assertNotNull(stringArguments);
        Assert.assertEquals(args.length, stringArguments.size());
        for (int i = 0; i < args.length; i++) {
            Assert.assertEquals(i, stringArguments.get(i).getIndex());
            Assert.assertEquals(args[i], stringArguments.get(i).getValue());
        }
    }
}