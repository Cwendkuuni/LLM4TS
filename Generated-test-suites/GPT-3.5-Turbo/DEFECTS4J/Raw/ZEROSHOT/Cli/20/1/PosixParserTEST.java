import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", "cOption", false, "Option c");
        options.addOption("d", "dOption", true, "Option d");
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[] {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[] {"arg1", "-a", "arg2"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[] {"arg1", "--", "-a", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = new String[] {"--cOption", "--dOption=value"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[] {"--cOption", "--dOption", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = new String[] {"-"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_ShortOption() {
        String[] args = new String[] {"-a", "-b=value"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[] {"-a", "-b", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOption_Invalid() {
        String[] args = new String[] {"-x"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_ShortOption_StopAtNonOption() {
        String[] args = new String[] {"-a", "-x", "arg1"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[] {"-a", "--", "-x", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_RequiredOption() {
        options.getOption("b").setRequired(true);
        String[] args = new String[] {"-a"};
        try {
            parser.flatten(options, args, false);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }
    }

    @Test
    public void testFlatten_RequiredOption_StopAtNonOption() {
        options.getOption("b").setRequired(true);
        String[] args = new String[] {"-a", "-x", "arg1"};
        try {
            parser.flatten(options, args, true);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleArgsOption() {
        String[] args = new String[] {"-d", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[] {"-d", "value1", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleArgsOption_StopAtNonOption() {
        String[] args = new String[] {"-d", "value1", "value2", "arg1"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[] {"-d", "value1", "value2", "--", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption() {
        String[] args = new String[] {"-a", "-x", "arg1"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[] {"-a", "--", "-x", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_UnknownOption() {
        String[] args = new String[] {"-a", "-x"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[] {"-a", "-x"};
        assertArrayEquals(expected, result);
    }
}