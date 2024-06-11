import static org.junit.Assert.*;
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
        String[] testArgs = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(testArgs);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testSetArgsWithNullArray() {
        try {
            argsParser.setArgs(null);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] testArgs = {"arg1", null, "arg3"};
        argsParser.setArgs(testArgs);
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] testArgs = {"-v", "--help"};
        argsParser.setArgs(testArgs);
        SwitchArgument switchArg = argsParser.parseSwitchArgument("v");
        assertTrue(switchArg.isActive());
    }

    @Test
    public void testParseParameterArgument() {
        String[] testArgs = {"--name", "John"};
        argsParser.setArgs(testArgs);
        ParameterArgument paramArg = argsParser.parseParameterArgument("name");
        assertNotNull(paramArg);
        assertEquals("John", paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] testArgs = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(testArgs);
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertEquals(3, stringArgs.size());
    }
}