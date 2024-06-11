import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = buildOptions();
    }

    @Test
    public void testFlatten_ValidArgumentsAndOptions_ReturnsFlattenedArguments() {
        String[] arguments = {"-a", "-b", "--foo", "--bar=value", "input.txt"};
        String[] expected = {"-a", "-b", "--foo", "--bar=value", "input.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ArgumentsWithDoubleDash_ReturnsFlattenedArgumentsIncludingDoubleDash() {
        String[] arguments = {"-a", "--", "-b"};
        String[] expected = {"-a", "--", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption_ReturnsFlattenedArgumentsWithInvalidOption() {
        String[] arguments = {"-a", "-b", "--invalid", "input.txt"};
        String[] expected = {"-a", "-b", "--invalid", "input.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionAbbreviation_ReturnsFlattenedArgumentsWithInvalidOptionAbbreviation() {
        String[] arguments = {"-a", "-b", "-f=value"};
        String[] expected = {"-a", "-b", "-f", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsFlattenedArgumentsUptoNonOption() {
        String[] arguments = {"-a", "-b", "input.txt", "--", "--foo"};
        String[] expected = {"-a", "-b", "input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    private Options buildOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("foo", false, "Option foo");
        options.addOption("bar", true, "Option bar");

        return options;
    }

}