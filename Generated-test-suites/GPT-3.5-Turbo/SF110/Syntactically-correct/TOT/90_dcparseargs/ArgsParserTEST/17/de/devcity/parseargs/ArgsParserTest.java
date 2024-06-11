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
        int result = argsParser.getArgsCount();
        assertEquals(3, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgument_ThrowsNullPointerException() {
        argsParser.setArgs(null);
    }

    @Test
    public void testSetArgs_ValidArgs_CorrectlySetsArgs() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        String[] result = argsParser.getArgs();
        assertArrayEquals(args, result);
    }

    @Test
    public void testGetArgsLeftCount_NoArgs_LeftCountIsZero() {
        int result = argsParser.getArgsLeftCount();
        assertEquals(0, result);
    }

    @Test
    public void testGetArgsLeftCount_SomeArgs_LeftCountIsCorrect() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        int result = argsParser.getArgsLeftCount();
        assertEquals(2, result);
    }

    @Test
    public void testParseSwitchArgument_LongKeyExists_ReturnsSwitchArgument() {
        String[] args = {"--key1", "--key2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("key1");
        assertNotNull(result);
        assertTrue(result.isSwitch());
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_LongKeyDoesNotExist_ReturnsSwitchArgument() {
        String[] args = {"--key1", "--key2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("key3");
        assertNotNull(result);
        assertFalse(result.isSwitch());
        assertEquals(-1, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyExists_ReturnsSwitchArgument() {
        String[] args = {"-k1", "-k2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("k1");
        assertNotNull(result);
        assertTrue(result.isSwitch());
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyDoesNotExist_ReturnsSwitchArgument() {
        String[] args = {"-k1", "-k2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("k3");
        assertNotNull(result);
        assertFalse(result.isSwitch());
        assertEquals(-1, result.getIndex());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithValue_ReturnsParameterArgumentWithValue() {
        String[] args = {"--key1", "value1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("key1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value1", result.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithoutValue_ReturnsNull() {
        String[] args = {"--key1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("key1");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_LongKeyDoesNotExist_ReturnsNull() {
        String[] args = {"--key1", "value1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("key3");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithValue_ReturnsParameterArgumentWithValue() {
        String[] args = {"-k1value1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("k1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value1", result.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithoutValue_ReturnsNull() {
        String[] args = {"-k1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("k1");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_ShortKeyDoesNotExist_ReturnsNull() {
        String[] args = {"-k1value1"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("k3");
        assertNull(result);
    }

    @Test
    public void testParseStringArgument_NoArgs_ReturnsEmptyList() {
        List<StringArgument> result = argsParser.parseStringArgument();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseStringArgument_SomeArgs_ReturnsStringArguments() {
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        List<StringArgument> result = argsParser.parseStringArgument();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
    }

}