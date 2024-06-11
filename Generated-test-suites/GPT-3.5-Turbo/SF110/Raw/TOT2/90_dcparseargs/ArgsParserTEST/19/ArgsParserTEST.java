import de.devcity.parseargs.arguments.*;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(args.length, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        argsParser.parseSwitchArgument("-arg1");
        assertEquals(args.length - 1, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_LongKey() {
        String[] args = {"--key"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key");
        assertTrue(switchArgument.isSwitched());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKey() {
        String[] args = {"-k"};
        argsParser.setArgs(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k");
        assertTrue(switchArgument.isSwitched());
    }
    
    @Test
    public void testParseParameterArgument_LongKey() {
        String[] args = {"--key", "value"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key");
        assertNotNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgument_ShortKey() {
        String[] args = {"-kvalue"};
        argsParser.setArgs(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertNotNull(parameterArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(args.length, stringArguments.size());
    }
}