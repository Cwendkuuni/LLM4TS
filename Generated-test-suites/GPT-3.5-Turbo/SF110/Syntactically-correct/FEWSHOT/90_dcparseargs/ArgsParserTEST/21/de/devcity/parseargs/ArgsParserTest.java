import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testSetArgs() {
        String[] args = {"-f", "--longArg", "argValue"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsCount());
        assertEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-f", "--longArg", "argValue"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("-f");
        assertEquals(2, argsParser.getArgsLeftCount());

        argsParser.parseParameterArgument("--longArg");
        assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-f", "--longArg", "argValue"};
        argsParser.setArgs(args);

        SwitchArgument switchArg1 = argsParser.parseSwitchArgument("-f");
        assertEquals(0, switchArg1.getIndex());
        assertEquals("-f", switchArg1.getKey());
        assertEquals(true, switchArg1.isSwitchPresent());

        SwitchArgument switchArg2 = argsParser.parseSwitchArgument("--longArg");
        assertEquals(1, switchArg2.getIndex());
        assertEquals("--longArg", switchArg2.getKey());
        assertEquals(true, switchArg2.isSwitchPresent());

        SwitchArgument switchArg3 = argsParser.parseSwitchArgument("-p");
        assertEquals(-1, switchArg3.getIndex());
        assertEquals("-p", switchArg3.getKey());
        assertEquals(false, switchArg3.isSwitchPresent());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"-f", "--longArg", "argValue"};
        argsParser.setArgs(args);

        ParameterArgument paramArg1 = argsParser.parseParameterArgument("--longArg");
        assertEquals(1, paramArg1.getIndex());
        assertEquals("--longArg", paramArg1.getKey());
        assertEquals("argValue", paramArg1.getValue());

        ParameterArgument paramArg2 = argsParser.parseParameterArgument("-f");
        assertNull(paramArg2);

        ParameterArgument paramArg3 = argsParser.parseParameterArgument("-p");
        assertNull(paramArg3);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"-f", "--longArg", "argValue"};
        argsParser.setArgs(args);

        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertEquals(3, stringArgs.size());
        assertEquals("argValue", stringArgs.get(2).getValue());

        assertEquals(0, argsParser.getArgsLeftCount());
    }

}