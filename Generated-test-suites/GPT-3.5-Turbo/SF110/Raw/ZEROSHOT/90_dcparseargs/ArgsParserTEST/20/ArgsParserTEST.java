import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.getArgsCount());
    }
    
    @Test(expected=NullPointerException.class)
    public void testGetArgsCount_NullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.getArgsCount();
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, argsParser.getArgs());
    }
    
    @Test(expected=NullPointerException.class)
    public void testGetArgs_NullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.getArgs();
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, argsParser.getArgs());
    }
    
    @Test(expected=NullPointerException.class)
    public void testSetArgs_NullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument switchArg = argsParser.parseSwitchArgument("switch");
        assertNotNull(switchArg);
        assertEquals(0, switchArg.getIndex());
        assertEquals("switch", switchArg.getKey());
        assertTrue(switchArg.isLongKey());
    }
    
    @Test
    public void testParseSwitchArgument_Invalid() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--invalid"});
        SwitchArgument switchArg = argsParser.parseSwitchArgument("switch");
        assertNotNull(switchArg);
        assertEquals(-1, switchArg.getIndex());
        assertEquals("switch", switchArg.getKey());
        assertFalse(switchArg.isLongKey());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--parameter", "value"});
        ParameterArgument paramArg = argsParser.parseParameterArgument("parameter");
        assertNotNull(paramArg);
        assertEquals(0, paramArg.getIndex());
        assertEquals("parameter", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_Invalid() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--invalid"});
        ParameterArgument paramArg = argsParser.parseParameterArgument("parameter");
        assertNull(paramArg);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(2, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
    }

    @Test
    public void testParseStringArgument_None() {
        ArgsParser argsParser = new ArgsParser();
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(0, stringArgs.size());
    }

}