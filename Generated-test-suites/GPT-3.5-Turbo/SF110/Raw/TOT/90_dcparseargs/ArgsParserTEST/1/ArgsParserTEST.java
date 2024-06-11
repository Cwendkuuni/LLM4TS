import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {
    
    ArgsParser parser;
    String[] args;
    
    // Initialization before each test case
    @Before
    public void setUp() {
        parser = new ArgsParser();
        args = new String[]{"--arg1", "-arg2", "--arg3", "value3"};
        parser.setArgs(args);
    }

    // Test case for getArgsCount
    @Test
    public void testGetArgsCount() {
        int expectedCount = args.length;
        int actualCount = parser.getArgsCount();
        assertEquals(expectedCount, actualCount);
    }

    // Test case for getArgs
    @Test
    public void testGetArgs() {
        String[] expectedArgs = args;
        String[] actualArgs = parser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }

    // Test case for setArgs with null args
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        String[] nullArgs = null;
        parser.setArgs(nullArgs);
    }

    // Test case for setArgs with non-null args
    @Test
    public void testSetArgsNonNull() {
        String[] newArgs = new String[]{"--newArg1"};
        parser.setArgs(newArgs);
        assertArrayEquals(newArgs, parser.getArgs());
    }

    // Test case for getArgsLeftCount
    @Test
    public void testGetArgsLeftCount() {
        int expectedCount = args.length;
        int actualCount = parser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);
    }

    // Test case for parseSwitchArgument with valid long key
    @Test
    public void testParseSwitchArgumentValidLongKey() {
        String key = "arg1";
        SwitchArgument expectedArgument = new SwitchArgument(0, key, true);
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.isLongKey(), actualArgument.isLongKey());
    }

    // Test case for parseSwitchArgument with invalid long key
    @Test
    public void testParseSwitchArgumentInvalidLongKey() {
        String key = "arg5";
        SwitchArgument expectedArgument = new SwitchArgument(-1, key, false);
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.isLongKey(), actualArgument.isLongKey());
    }

    // Test case for parseSwitchArgument with valid short key
    @Test
    public void testParseSwitchArgumentValidShortKey() {
        String key = "arg2";
        SwitchArgument expectedArgument = new SwitchArgument(1, key, true);
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.isLongKey(), actualArgument.isLongKey());
    }

    // Test case for parseSwitchArgument with invalid short key
    @Test
    public void testParseSwitchArgumentInvalidShortKey() {
        String key = "arg5";
        SwitchArgument expectedArgument = new SwitchArgument(-1, key, false);
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.isLongKey(), actualArgument.isLongKey());
    }

    // Test case for parseParameterArgument with valid long key and value
    @Test
    public void testParseParameterArgumentValidLongKeyAndValue() {
        String key = "arg3";
        ParameterArgument expectedArgument = new ParameterArgument(2, key, "value3");
        ParameterArgument actualArgument = parser.parseParameterArgument(key);
        assertEquals(expectedArgument.getIndex(), actualArgument.getIndex());
        assertEquals(expectedArgument.getKey(), actualArgument.getKey());
        assertEquals(expectedArgument.getValue(), actualArgument.getValue());
    }

    // Test case for parseParameterArgument with invalid long key
    @Test
    public void testParseParameterArgumentInvalidLongKey() {
        String key = "arg5";
        ParameterArgument expectedArgument = null;
        ParameterArgument actualArgument = parser.parseParameterArgument(key);
        assertEquals(expectedArgument, actualArgument);
    }
    
    // Test case for parseStringArgument
    @Test
    public void testParseStringArgument() {
        StringArgument expectedArgument1 = new StringArgument(0, "--arg1");
        StringArgument expectedArgument2 = new StringArgument(1, "-arg2");
        StringArgument expectedArgument3 = new StringArgument(2, "--arg3");
        StringArgument[] expectedArguments = new StringArgument[]{expectedArgument1, expectedArgument2, expectedArgument3};
        
        List<StringArgument> actualArguments = parser.parseStringArgument();
        assertEquals(expectedArguments.length, actualArguments.size());
        
        for (int i = 0; i < expectedArguments.length; i++) {
            assertEquals(expectedArguments[i].getIndex(), actualArguments.get(i).getIndex());
            assertEquals(expectedArguments[i].getArgument(), actualArguments.get(i).getArgument());
        }
    }
}