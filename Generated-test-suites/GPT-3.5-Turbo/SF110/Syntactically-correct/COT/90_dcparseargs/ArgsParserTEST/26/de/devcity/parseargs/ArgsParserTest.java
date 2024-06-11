import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    private ArgsParser argsParser;
    private String[] args;

    @Before
    public void setup() {
        argsParser = new ArgsParser();
        args = new String[]{"--switch", "-p", "parameter", "arg1", "arg2"};
        argsParser.setArgs(args);
    }

    @Test
    public void testGetArgsCount() {
        assertEquals(args.length, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] newArgs = new String[]{"--new-switch", "new-arg"};
        argsParser.setArgs(newArgs);
        assertArrayEquals(newArgs, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        assertEquals(args.length, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch");
        assertNotNull(switchArgument);
        assertTrue(switchArgument.isExists());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("switch", switchArgument.getKey());

        SwitchArgument nonExistingSwitch = argsParser.parseSwitchArgument("non-existing");
        assertNotNull(nonExistingSwitch);
        assertFalse(nonExistingSwitch.isExists());
        assertEquals(-1, nonExistingSwitch.getIndex());
        assertEquals("non-existing", nonExistingSwitch.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("p");
        assertNotNull(parameterArgument);
        assertEquals(1, parameterArgument.getIndex());
        assertEquals("p", parameterArgument.getKey());
        assertEquals("parameter", parameterArgument.getValue());

        ParameterArgument nonExistingParameter = argsParser.parseParameterArgument("non-existing");
        assertNull(nonExistingParameter);
    }

    @Test
    public void testParseStringArgument() {
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertNotNull(stringArguments);
        assertEquals(2, stringArguments.size());

        StringArgument arg1 = stringArguments.get(0);
        assertNotNull(arg1);
        assertEquals(3, arg1.getIndex());
        assertEquals("arg1", arg1.getValue());

        StringArgument arg2 = stringArguments.get(1);
        assertNotNull(arg2);
        assertEquals(4, arg2.getIndex());
        assertEquals("arg2", arg2.getValue());
    }
}

