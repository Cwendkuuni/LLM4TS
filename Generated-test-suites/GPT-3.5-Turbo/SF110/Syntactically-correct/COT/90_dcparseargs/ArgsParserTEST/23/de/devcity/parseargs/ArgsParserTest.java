
import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertEquals(0, result.getIndex());
        assertTrue(result.isExists());
    }

    @Test
    public void testParseSwitchArgument_LongKeyNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"--otherkey"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isExists());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"-k"});
        SwitchArgument result = parser.parseSwitchArgument("k");
        assertEquals(0, result.getIndex());
        assertTrue(result.isExists());
    }

    @Test
    public void testParseSwitchArgument_ShortKeyNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"-o"});
        SwitchArgument result = parser.parseSwitchArgument("k");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isExists());
    }

    @Test
    public void testParseParameterArgument_LongKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKeyNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"--otherkey", "value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_ShortKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"-k", "value"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKeyNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"-o", "value"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertNull(result);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).getIndex());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals(1, result.get(1).getIndex());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals(2, result.get(2).getIndex());
        assertEquals("arg3", result.get(2).getValue());
    }

}
