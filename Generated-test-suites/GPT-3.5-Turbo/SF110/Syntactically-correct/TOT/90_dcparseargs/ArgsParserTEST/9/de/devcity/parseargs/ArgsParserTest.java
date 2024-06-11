import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {

    private ArgsParser argsParser;
    private String[] args;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
        args = new String[] {"--verbose", "-l", "info"};
        argsParser.setArgs(args);
    }
    
    @Test
    public void testGetArgsCount() {
        int expectedCount = 3;
        int actualCount = argsParser.getArgsCount();
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testGetArgs() {
        String[] expectedArgs = {"--verbose", "-l", "info"};
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgs() {
        String[] nullArgs = null;
        argsParser.setArgs(nullArgs);
    }
    
    @Test
    public void testSetArgs_ValidArgs() {
        String[] newArgs = {"-d", "debug"};
        argsParser.setArgs(newArgs);
        String[] expectedArgs = {"-d", "debug"};
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        int expectedCount = 3;
        int actualCount = argsParser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        String switchKey = "verbose";
        SwitchArgument expectedArgument = new SwitchArgument(0, "verbose", true);
        SwitchArgument actualArgument = argsParser.parseSwitchArgument(switchKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyExists() {
        String switchKey = "l";
        SwitchArgument expectedArgument = new SwitchArgument(1, "l", true);
        SwitchArgument actualArgument = argsParser.parseSwitchArgument(switchKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyDoesNotExist() {
        String switchKey = "invalid";
        SwitchArgument expectedArgument = new SwitchArgument(-1, "invalid", false);
        SwitchArgument actualArgument = argsParser.parseSwitchArgument(switchKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyDoesNotExist() {
        String switchKey = "x";
        SwitchArgument expectedArgument = new SwitchArgument(-1, "x", false);
        SwitchArgument actualArgument = argsParser.parseSwitchArgument(switchKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyExistsWithValue() {
        String paramKey = "verbose";
        ParameterArgument expectedArgument = new ParameterArgument(0, "verbose", "true");
        ParameterArgument actualArgument = argsParser.parseParameterArgument(paramKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyExistsWithValue() {
        String paramKey = "l";
        ParameterArgument expectedArgument = new ParameterArgument(1, "l", "info");
        ParameterArgument actualArgument = argsParser.parseParameterArgument(paramKey);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyExistsWithoutValue() {
        String paramKey = "version";
        ParameterArgument expectedArgument = null;
        ParameterArgument actualArgument = argsParser.parseParameterArgument(paramKey);
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyExistsWithoutValue() {
        String paramKey = "d";
        ParameterArgument expectedArgument = null;
        ParameterArgument actualArgument = argsParser.parseParameterArgument(paramKey);
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        StringArgument expectedArgument = new StringArgument(0, "--verbose");
        StringArgument actualArgument = argsParser.parseStringArgument().get(0);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }
}