import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testConstructor() {
        assertNotNull(argsParser);
    }

    @Test
    public void testSetArgs_NullArgs() {
        try {
            argsParser.setArgs(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testSetArgs_EmptyArgs() {
        String[] args = new String[]{};
        argsParser.setArgs(args);
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testSetArgs_NonEmptyArgs() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        assertEquals(args.length, argsParser.getArgsCount());
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsCount_EmptyArgs() {
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsCount_NonEmptyArgs() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        assertEquals(args.length, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs_EmptyArgs() {
        String[] expectedArgs = new String[]{};
        argsParser.setArgs(expectedArgs);
        assertArrayEquals(expectedArgs, argsParser.getArgs());
    }

    @Test
    public void testGetArgs_NonEmptyArgs() {
        String[] expectedArgs = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(expectedArgs);
        assertArrayEquals(expectedArgs, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount_AllArgsUsed() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount_UnusedArgsExist() {
        String[] args = new String[]{"-arg1", "--arg2", "-arg3"};
        argsParser.setArgs(args);
        assertEquals(1, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("arg2");
        assertNotNull(result);
        assertEquals(1, result.getIndex());
        assertEquals("arg2", result.getKey());
        assertTrue(result.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_LongKeyNotFound() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("arg3");
        assertNotNull(result);
        assertEquals(-1, result.getIndex());
        assertEquals("arg3", result.getKey());
        assertFalse(result.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyExists() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("arg1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("arg1", result.getKey());
        assertTrue(result.isSwitch());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyNotFound() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        SwitchArgument result = argsParser.parseSwitchArgument("arg3");
        assertNotNull(result);
        assertEquals(-1, result.getIndex());
        assertEquals("arg3", result.getKey());
        assertFalse(result.isSwitch());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithValue() {
        String[] args = new String[]{"-arg1", "value1", "--arg2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg2");
        assertNotNull(result);
        assertEquals(2, result.getIndex());
        assertEquals("arg2", result.getKey());
        assertEquals("value2", result.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyExistsWithoutValue() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg2");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_LongKeyNotFound() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg3");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithValue() {
        String[] args = new String[]{"-arg1", "value1", "--arg2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg1");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("arg1", result.getKey());
        assertEquals("value1", result.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyExistsWithoutValue() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg1");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_ShortKeyNotFound() {
        String[] args = new String[]{"-arg1", "--arg2"};
        argsParser.setArgs(args);
        ParameterArgument result = argsParser.parseParameterArgument("arg3");
        assertNull(result);
    }

    @Test
    public void testParseStringArgument_AllArgsUsed() {
        String[] args = new String[]{"-arg1", "value1", "--arg2"};
        argsParser.setArgs(args);
        assertEquals(0, argsParser.parseStringArgument().size());
    }

    @Test
    public void testParseStringArgument_UnusedArgsExist() {
        String[] args = new String[]{"-arg1", "value1", "--arg2", "-arg3"};
        argsParser.setArgs(args);
        assertEquals(1, argsParser.parseStringArgument().size());
    }
}