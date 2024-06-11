import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {
    private static ArgsParser parser;

    @BeforeClass
    public static void setUp() {
        parser = new ArgsParser();
    }

    @AfterClass
    public static void tearDown() {
        parser = null;
    }

    // Test getArgsCount()
    @Test
    public void testGetArgsCount_typical() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        int actual = parser.getArgsCount();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgsCount_emptyArgs() {
        String[] args = {};
        parser.setArgs(args);
        int actual = parser.getArgsCount();
        int expected = 0;
        assertEquals(expected, actual);
    }

    // Test getArgs()
    @Test
    public void testGetArgs_typical() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        String[] actual = parser.getArgs();
        assertArrayEquals(args, actual);
    }

    @Test
    public void testGetArgs_emptyArgs() {
        String[] args = {};
        parser.setArgs(args);
        String[] actual = parser.getArgs();
        assertArrayEquals(args, actual);
    }

    // Test setArgs()
    @Test
    public void testSetArgs_typical() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        String[] actual = parser.getArgs();
        assertArrayEquals(args, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgs_nullArgs() {
        String[] args = null;
        parser.setArgs(args);
    }

    // Test getArgsLeftCount()
    @Test
    public void testGetArgsLeftCount_typical() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        int actual = parser.getArgsLeftCount();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgsLeftCount_oneArgLeft() {
        String[] args = {"-a"};
        parser.setArgs(args);
        int actual = parser.getArgsLeftCount();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgsLeftCount_noArgsLeft() {
        String[] args = {};
        parser.setArgs(args);
        int actual = parser.getArgsLeftCount();
        int expected = 0;
        assertEquals(expected, actual);
    }

    // Test parseSwitchArgument()
    @Test
    public void testParseSwitchArgument_typicalShortKey() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        SwitchArgument actual = parser.parseSwitchArgument("a");
        assertTrue(actual.isFound());
        assertEquals(0, actual.getIndex());
        assertEquals("a", actual.getKey());
    }

    @Test
    public void testParseSwitchArgument_typicalLongKey() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        SwitchArgument actual = parser.parseSwitchArgument("foo");
        assertTrue(actual.isFound());
        assertEquals(1, actual.getIndex());
        assertEquals("foo", actual.getKey());
    }

    @Test
    public void testParseSwitchArgument_keyNotFound() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        SwitchArgument actual = parser.parseSwitchArgument("c");
        assertFalse(actual.isFound());
        assertEquals(-1, actual.getIndex());
        assertEquals("c", actual.getKey());
    }

    // Test parseParameterArgument()
    @Test
    public void testParseParameterArgument_typicalShortKey() {
        String[] args = {"-a", "param", "-b"};
        parser.setArgs(args);
        ParameterArgument actual = parser.parseParameterArgument("a");
        assertNotNull(actual);
        assertEquals(0, actual.getIndex());
        assertEquals("a", actual.getKey());
        assertEquals("param", actual.getValue());
    }

    @Test
    public void testParseParameterArgument_typicalLongKey() {
        String[] args = {"-a", "--foo", "param"};
        parser.setArgs(args);
        ParameterArgument actual = parser.parseParameterArgument("foo");
        assertNotNull(actual);
        assertEquals(1, actual.getIndex());
        assertEquals("foo", actual.getKey());
        assertEquals("param", actual.getValue());
    }

    @Test
    public void testParseParameterArgument_keyNotFound() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        ParameterArgument actual = parser.parseParameterArgument("c");
        assertNull(actual);
    }

    @Test
    public void testParseParameterArgument_missingValue() {
        String[] args = {"-a"};
        parser.setArgs(args);
        ParameterArgument actual = parser.parseParameterArgument("a");
        assertNull(actual);
    }

    // Test parseStringArgument()
    @Test
    public void testParseStringArgument_typical() {
        String[] args = {"-a", "param1", "--foo", "param2", "-b"};
        parser.setArgs(args);
        List<StringArgument> actual = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "-a"));
        expected.add(new StringArgument(2, "--foo"));
        expected.add(new StringArgument(4, "-b"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseStringArgument_noStringArguments() {
        String[] args = {"-a", "--foo", "-b"};
        parser.setArgs(args);
        List<StringArgument> actual = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

}