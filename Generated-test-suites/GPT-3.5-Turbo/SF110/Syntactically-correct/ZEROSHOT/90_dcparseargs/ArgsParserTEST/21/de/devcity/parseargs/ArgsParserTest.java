import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

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
        int expected = 3;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] actual = argsParser.getArgs();
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullInput() {
        argsParser.setArgs(null);
    }
    
    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
        
        String[] expectedInnerArgs = {"arg1", "arg2", "arg3"};
        String[] actualInnerArgs = argsParser.getInnerArgs();
        assertArrayEquals(expectedInnerArgs, actualInnerArgs);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        int expected = 3;
        int actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
        
        argsParser.parseSwitchArgument("arg1");
        expected = 2;
        actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyFound() {
        String[] args = {"--key1", "--key2", "value1", "--key3"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "key1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("key1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyNotFound() {
        String[] args = {"--key1", "--key2", "value1", "--key3"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "key4", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("key4");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyFound() {
        String[] args = {"-k1", "-k2", "value1", "-k3"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "k1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("k1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyNotFound() {
        String[] args = {"-k1", "-k2", "value1", "-k3"};
        argsParser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "k4", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("k4");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyFoundWithValue() {
        String[] args = {"--key1", "value1", "--key2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "key1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("key1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyFoundWithoutValue() {
        String[] args = {"--key1", "--key2", "value1"};
        argsParser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("key1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyNotFound() {
        String[] args = {"--key1", "value1", "--key2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("key3");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyFoundWithValue() {
        String[] args = {"-k1", "value1", "-k2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "k1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("k1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyFoundWithoutValue() {
        String[] args = {"-k1", "-k2", "value1"};
        argsParser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("k1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyNotFound() {
        String[] args = {"-k1", "value1", "-k2", "value2"};
        argsParser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = argsParser.parseParameterArgument("k3");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        List<StringArgument> expected = Arrays.asList(new StringArgument(0, "arg1"), new StringArgument(1, "arg2"), new StringArgument(2, "arg3"));
        List<StringArgument> actual = argsParser.parseStringArgument();
        assertEquals(expected, actual);
        
        String[] innerArgs = argsParser.getInnerArgs();
        assertNull(innerArgs[0]);
        assertNull(innerArgs[1]);
        assertNull(innerArgs[2]);
    }
}