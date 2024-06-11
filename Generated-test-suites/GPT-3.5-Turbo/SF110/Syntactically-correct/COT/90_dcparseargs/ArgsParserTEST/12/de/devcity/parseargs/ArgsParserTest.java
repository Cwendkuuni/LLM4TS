import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        int expectedCount = 3;
        int actualCount = argsParser.getArgsCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNull() {
        String[] args = null;
        argsParser.setArgs(args);
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        argsParser.parseParameterArgument("key1");
        argsParser.parseSwitchArgument("key2");
        int expectedCount = 1;
        int actualCount = argsParser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testParseSwitchArgumentWithLongKey() {
        String[] args = {"--key1", "--key2"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key1");
        assertTrue(switchArgument.isFound());
        assertEquals(0, switchArgument.getIndex());
    }

    @Test
    public void testParseSwitchArgumentWithShortKey() {
        String[] args = {"-a1", "-b2"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertTrue(switchArgument.isFound());
        assertEquals(0, switchArgument.getIndex());
    }

    @Test
    public void testParseNonExistingSwitchArgument() {
        String[] args = {"-a1", "-b2"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("c");
        assertFalse(switchArgument.isFound());
        assertEquals(-1, switchArgument.getIndex());
    }

    @Test
    public void testParseParameterArgumentWithLongKey() {
        String[] args = {"--key1", "value1"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithShortKey() {
        String[] args = {"-a1", "value1"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("a");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithMissingValue() {
        String[] args = {"--key1"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseNonExistingParameterArgument() {
        String[] args = {"-a1", "value1"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("b");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        List<StringArgument> expectedArguments = new ArrayList<StringArgument>();
        expectedArguments.add(new StringArgument(0, "arg1"));
        expectedArguments.add(new StringArgument(1, "arg2"));
        expectedArguments.add(new StringArgument(2, "arg3"));
        List<StringArgument> actualArguments = argsParser.parseStringArgument();
        assertEquals(expectedArguments.size(), actualArguments.size());
        for (int i = 0; i < expectedArguments.size(); i++) {
            StringArgument expectedArg = expectedArguments.get(i);
            StringArgument actualArg = actualArguments.get(i);
            assertEquals(expectedArg.getIndex(), actualArg.getIndex());
            assertEquals(expectedArg.getValue(), actualArg.getValue());
        }
    }

    // Add additional test cases for edge cases and exception handling scenarios 

}

