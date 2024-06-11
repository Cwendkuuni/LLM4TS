import static org.junit.Assert.*;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {

    // Test case for ArgsParser constructor
    @Test
    public void testArgsParserConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }

    // Test case for setting arguments
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    // Test case for parsing switch argument
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument switchArg = parser.parseSwitchArgument("switch");
        assertNotNull(switchArg);
        assertTrue(switchArg.isFound());
    }

    // Test case for parsing parameter argument
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("param");
        assertNotNull(paramArg);
        assertEquals("value", paramArg.getValue());
    }

    // Test case for parsing string argument
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertNotNull(stringArgs);
        assertEquals(2, stringArgs.size());
    }

    // Test case for handling null args in setArgs method
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    // Test case for parsing non-existent switch argument
    @Test
    public void testParseNonExistentSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument switchArg = parser.parseSwitchArgument("nonexistent");
        assertNotNull(switchArg);
        assertFalse(switchArg.isFound());
    }

    // Test case for handling invalid parameter argument
    @Test
    public void testParseInvalidParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param"});
        ParameterArgument paramArg = parser.parseParameterArgument("param");
        assertNull(paramArg);
    }

    // Test case for handling edge case in parseParameterArgument method
    @Test
    public void testEdgeCaseParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-pvalue"});
        ParameterArgument paramArg = parser.parseParameterArgument("p");
        assertNotNull(paramArg);
        assertEquals("value", paramArg.getValue());
    }

}