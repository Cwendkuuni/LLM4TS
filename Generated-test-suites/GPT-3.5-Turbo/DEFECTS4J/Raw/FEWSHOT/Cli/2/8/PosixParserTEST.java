import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[] {"-f", "--foo", "bar"};

        options.addOption("f", false, "option f");
        options.addOption("foo", true, "option foo");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        String[] arguments = new String[] {"-", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenWithOptionTokenLength2() {
        String[] arguments = new String[] {"-f"};

        options.addOption("f", false, "option f");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenWithOptionTokenLengthBurst() {
        String[] arguments = new String[] {"-abc"};

        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlattenWithUnknownOptionStopAtNonOption() {
        String[] arguments = new String[] {"-d", "arg"};

        options.addOption("f", false, "option f");
        options.addOption("foo", true, "option foo");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("arg", result[2]);
    }

    @Test
    public void testFlattenWithUnknownOption() {
        String[] arguments = new String[] {"-d"};

        options.addOption("f", false, "option f");
        options.addOption("foo", true, "option foo");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-d", result[0]);
    }
}