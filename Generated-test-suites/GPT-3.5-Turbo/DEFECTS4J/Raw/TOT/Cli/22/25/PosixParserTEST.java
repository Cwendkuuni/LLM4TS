import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void flatten_shouldReturnEmptyArray_whenNoArgumentsPassed() {
        Options options = new Options();
        String[] arguments = {};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertEquals(0, flattenedArguments.length);
    }

    @Test
    public void flatten_shouldReturnArgumentsUnchanged_whenStopAtNonOptionIsFalse() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--foo", "bar"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldStopFlattening_whenStopAtNonOptionIsTrue() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "nonOptionArg"};
        String[] expectedArguments = {"-a", "-b", "nonOptionArg"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldAddTokensToFlattenedArguments_whenParsingValidOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a"};
        String[] expectedArguments = {"-a"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldAddOptionValueToFlattenedArguments_whenParsingOptionWithArgument() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());

        String[] arguments = {"-f", "file.txt"};
        String[] expectedArguments = {"-f", "file.txt"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldAddSpecialTokenAndNonOptionTokensToFlattenedArguments_whenParsingNonOptionTokenWhileStopAtNonOptionIsTrue() {
        Options options = new Options();

        String[] arguments = {"arg1", "--", "arg2", "arg3"};
        String[] expectedArguments = {"--", "arg1", "arg2", "arg3"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldAddNonOptionTokenToFlattenedArguments_whenParsingNonOptionTokenWhileStopAtNonOptionIsFalse() {
        Options options = new Options();

        String[] arguments = {"nonOptionArg"};
        String[] expectedArguments = {"nonOptionArg"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void flatten_shouldBurstTokenAndAddOptionsToFlattenedArguments_whenParsingOptionTokenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-abc"};
        String[] expectedArguments = {"-a", "-b", "-c"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(expectedArguments, flattenedArguments);
    }

}