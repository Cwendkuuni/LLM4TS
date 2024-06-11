
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ArgsParserTest {

    // Test setup
    private ArgsParser parser;
    private String[] args;

    // Initialize ArgsParser before each test case
    @Before
    public void setUp() {
        args = new String[]{"-a", "--long", "-b", "param1", "param2"};
        parser = new ArgsParser(args);
    }

    // Test cases for getArgsCount() method
    @Test
    public void testGetArgsCount() {
        assertEquals(5, parser.getArgsCount());
    }

    // Test cases for getArgs() method
    @Test
    public void testGetArgs() {
        assertArrayEquals(args, parser.getArgs());
    }

    // Test cases for setArgs() method
    @Test
    public void testSetArgs() {
        String[] newArgs = new String[]{"-x", "--long"};
        parser.setArgs(newArgs);
        assertArrayEquals(newArgs, parser.getArgs());
    }

    // Test cases for getArgsLeftCount() method
    @Test
    public void testGetArgsLeftCount() {
        assertEquals(5, parser.getArgsLeftCount());
    }

    // Test cases for parseSwitchArgument() method
    @Test
    public void testParseSwitchArgument() {
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertEquals(0, switchArg.getIndex());
        assertEquals("a", switchArg.getKey());
        assertTrue(switchArg.getValue());

        switchArg = parser.parseSwitchArgument("long");
        assertEquals(1, switchArg.getIndex());
        assertEquals("long", switchArg.getKey());
        assertTrue(switchArg.getValue());

        switchArg = parser.parseSwitchArgument("b");
        assertEquals(2, switchArg.getIndex());
        assertEquals("b", switchArg.getKey());
        assertTrue(switchArg.getValue());

        switchArg = parser.parseSwitchArgument("c");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("c", switchArg.getKey());
        assertFalse(switchArg.getValue());
    }

    // Test cases for parseParameterArgument() method
    @Test
    public void testParseParameterArgument() {
        ParameterArgument paramArg = parser.parseParameterArgument("b");
        assertEquals(2, paramArg.getIndex());
        assertEquals("b", paramArg.getKey());
        assertEquals("param1", paramArg.getValue());

        paramArg = parser.parseParameterArgument("d");
        assertNull(paramArg);
    }

    // Test cases for parseStringArgument() method
    @Test
    public void testParseStringArgument() {
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
        assertEquals("param1", stringArgs.get(0).getValue());
        assertEquals("param2", stringArgs.get(1).getValue());
    }

}
