import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        String[] testArgs = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(testArgs);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] testArgs = {"arg1", "arg2", "arg3", null};
        argsParser.setArgs(testArgs);
        assertEquals(3, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] testArgs = {"--switch1", "-switch2", "--switch3"};
        argsParser.setArgs(testArgs);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("switch1");
        assertNotNull(switchArgument);
    }

    @Test
    public void testParseParameterArgument() {
        String[] testArgs = {"--param=value", "-param2=value2"};
        argsParser.setArgs(testArgs);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("param");
        assertNotNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] testArgs = {"string1", "string2", "string3"};
        argsParser.setArgs(testArgs);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
    }
}