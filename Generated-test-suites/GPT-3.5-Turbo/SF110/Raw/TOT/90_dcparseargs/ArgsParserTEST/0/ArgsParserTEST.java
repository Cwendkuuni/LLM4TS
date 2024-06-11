import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    private ArgsParser parser;

    @Before
    public void setUp() {
        parser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgsCount_EmptyArgs() {
        String[] args = {};
        parser.setArgs(args);
        assertEquals(0, parser.getArgsCount());
    }

    @Test (expected = NullPointerException.class)
    public void testSetArgs_NullArgs() {
        parser.setArgs(null);
    }

    @Test
    public void testSetArgs_NonNullArgs() {
        String[] args = {"arg1", "arg2"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgs_MultipleArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount_NullArgs() {
        String[] args = {};
        parser.setArgs(args);
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount_WithParsedArgs() {
        String[] args = {"--arg1", "value1", "arg2"};
        parser.setArgs(args);
        parser.parseParameterArgument("arg1");
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        String[] args = {"--key"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("key");
        assertEquals(0, switchArg.getIndex());
        assertEquals("key", switchArg.getKey());
        assertTrue(switchArg.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_LongKeyNotExists() {
        String[] args = {"--key"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("invalidKey");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("invalidKey", switchArg.getKey());
        assertFalse(switchArg.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyExists() {
        String[] args = {"-k"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("k");
        assertEquals(0, switchArg.getIndex());
        assertEquals("k", switchArg.getKey());
        assertTrue(switchArg.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyNotExists() {
        String[] args = {"-k"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("invalidKey");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("invalidKey", switchArg.getKey());
        assertFalse(switchArg.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_KeyValueCombined() {
        String[] args = {"-kvalue"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("k");
        assertEquals(0, switchArg.getIndex());
        assertEquals("k", switchArg.getKey());
        assertTrue(switchArg.isSwitch());
        assertNull(parser.parseParameterArgument("value"));
    }

    @Test
    public void testParseParameterArgument_LongKeyExists() {
        String[] args = {"--key", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("key");
        assertEquals(0, paramArg.getIndex());
        assertEquals("key", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyNotExists() {
        String[] args = {"--key", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("invalidKey");
        assertNull(paramArg);
    }

    @Test
    public void testParseParameterArgument_ShortKeyExists() {
        String[] args = {"-k", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("k");
        assertEquals(0, paramArg.getIndex());
        assertEquals("k", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyNotExists() {
        String[] args = {"-k", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("invalidKey");
        assertNull(paramArg);
    }

    @Test
    public void testParseParameterArgument_KeyValueSeparated() {
        String[] args = {"-k", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("k");
        assertEquals(0, paramArg.getIndex());
        assertEquals("k", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgument_KeyValueCombined() {
        String[] args = {"-kvalue"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("k");
        assertNull(paramArg);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2"};
        parser.setArgs(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(2, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getArgument());
        assertEquals(0, stringArgs.get(0).getIndex());
        assertEquals("arg2", stringArgs.get(1).getArgument());
        assertEquals(1, stringArgs.get(1).getIndex());
    }

    @Test
    public void testParseStringArgument_NullArgs() {
        String[] args = {};
        parser.setArgs(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(0, stringArgs.size());
    }

}