import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testSetArgs_NullInput_ThrowsException() {
        ArgsParser parser = new ArgsParser();
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1"});
        SwitchArgument switchArg = parser.parseSwitchArgument("key1");
        assertTrue(switchArg.isValid());
        assertEquals(0, switchArg.getIndex());
        assertEquals("key1", switchArg.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1"});
        ParameterArgument paramArg = parser.parseParameterArgument("key1");
        assertNotNull(paramArg);
        assertEquals(0, paramArg.getIndex());
        assertEquals("key1", paramArg.getKey());
        assertEquals("value1", paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
    }
}