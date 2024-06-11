import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {

    @Before
    public void setUp() {
        // Prepare for the tests
    }

    @After
    public void tearDown() {
        // Clean up after the tests
    }

    // Test case for the constructor ArgsParser()
    @Test
    public void testConstructorWithNoArgs() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
        assertEquals(0, parser.getArgsCount());
        assertNotNull(parser.getArgs());
        assertEquals(0, parser.getArgs().length);
    }

    // Test case for the constructor ArgsParser(String[] args)
    @Test
    public void testConstructorWithArgs() {
        String[] args = {"-a", "--b"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertEquals(2, parser.getArgsCount());
        assertNotNull(parser.getArgs());
        assertEquals(args.length, parser.getArgs().length);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for getArgsCount()
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "--b", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    // Test case for getArgs()
    @Test
    public void testGetArgs() {
        String[] args = {"-a", "--b", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for setArgs(String[] args)
    @Test
    public void testSetArgs() {
        String[] args = {"-a", "--b", "value"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertNotNull(parser.getArgs());
        assertEquals(args.length, parser.getArgs().length);
        assertArrayEquals(args, parser.getArgs());
    }

    // Test case for getArgsLeftCount()
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "--b", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsLeftCount());
    }

    // Test case for parseSwitchArgument(String key) - found long key with value
    @Test
    public void testParseSwitchArgumentWithLongKeyWithValue() {
        String[] args = {"--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key");
        assertNotNull(switchArgument);
        assertEquals(0, switchArgument.getIndex());
        assertEquals("key", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    // Test case for parseSwitchArgument(String key) - found short key with value
    @Test
    public void testParseSwitchArgumentWithShortKeyWithValue() {
        String[] args = {"-k", "value"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("k");
        assertNotNull(switchArgument);
        assertEquals(0, switchArgument.getIndex());
        assertEquals("k", switchArgument.getKey());
        assertTrue(switchArgument.getValue());
    }

    // Test case for parseSwitchArgument(String key) - not found
    @Test
    public void testParseSwitchArgumentNotFound() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("nonexistent");
        assertNotNull(switchArgument);
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("nonexistent", switchArgument.getKey());
        assertFalse(switchArgument.getValue());
    }

    // Test case for parseParameterArgument(String key) - found long key with value
    @Test
    public void testParseParameterArgumentWithLongKeyWithValue() {
        String[] args = {"--key", "value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("key", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    // Test case for parseParameterArgument(String key) - found short key with value
    @Test
    public void testParseParameterArgumentWithShortKeyWithValue() {
        String[] args = {"-k", "value"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("k");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("k", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    // Test case for parseParameterArgument(String key) - not found
    @Test
    public void testParseParameterArgumentNotFound() {
        String[] args = {"--key"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("nonexistent");
        assertNull(parameterArgument);
    }

    // Test case for parseStringArgument()
    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "--b", "value", "text"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertNotNull(stringArguments);
        assertEquals(4, stringArguments.size());
        assertEquals("a", stringArguments.get(0).getArgument());
        assertEquals("--b", stringArguments.get(1).getArgument());
        assertEquals("value", stringArguments.get(2).getArgument());
        assertEquals("text", stringArguments.get(3).getArgument());
    }
}