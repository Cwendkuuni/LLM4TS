import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;

import java.util.List;

public class ArgsParserTest {

    private ArgsParser parser;

    @Before
    public void setup() {
        parser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int expectedCount = 3;
        int actualCount = parser.getArgsCount();
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testGetArgs() {
        String[] expectedArgs = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(expectedArgs);
        String[] actualArgs = parser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNullInput() {
        String[] args = null;
        parser.setArgs(args);
    }

    @Test
    public void testGetArgsLeftCount() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int expectedCount = 3;
        int actualCount = parser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyFound() {
        parser.setArgs(new String[] {"--key1"});
        SwitchArgument expectedArgument = new SwitchArgument(0, "key1", true);
        SwitchArgument actualArgument = parser.parseSwitchArgument("key1");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyNotFound() {
        parser.setArgs(new String[]{"--key1"});
        SwitchArgument expectedArgument = new SwitchArgument(-1, "key2", false);
        SwitchArgument actualArgument = parser.parseSwitchArgument("key2");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyFound() {
        parser.setArgs(new String[] {"-k1"});
        SwitchArgument expectedArgument = new SwitchArgument(0, "k1", true);
        SwitchArgument actualArgument = parser.parseSwitchArgument("k1");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyNotFound() {
        parser.setArgs(new String[]{"-k1"});
        SwitchArgument expectedArgument = new SwitchArgument(-1, "k2", false);
        SwitchArgument actualArgument = parser.parseSwitchArgument("k2");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyFound() {
        parser.setArgs(new String[]{"--key1", "value"});
        ParameterArgument expectedArgument = new ParameterArgument(0, "key1", "value");
        ParameterArgument actualArgument = parser.parseParameterArgument("key1");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyNotFound() {
        parser.setArgs(new String[]{"--key1", "value"});
        ParameterArgument expectedArgument = null;
        ParameterArgument actualArgument = parser.parseParameterArgument("key2");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyFound() {
        parser.setArgs(new String[]{"-k1", "value"});
        ParameterArgument expectedArgument = new ParameterArgument(0, "k1", "value");
        ParameterArgument actualArgument = parser.parseParameterArgument("k1");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyNotFound() {
        parser.setArgs(new String[]{"-k1", "value"});
        ParameterArgument expectedArgument = null;
        ParameterArgument actualArgument = parser.parseParameterArgument("k2");
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> expectedArguments = List.of(
            new StringArgument(0, "arg1"),
            new StringArgument(1, "arg2"),
            new StringArgument(2, "arg3")
        );
        List<StringArgument> actualArguments = parser.parseStringArgument();
        assertEquals(expectedArguments, actualArguments);
    }
}