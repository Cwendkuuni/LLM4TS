
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
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        Options options = new Options();

        String[] arguments = {"--option", "value"};

        String[] expected = {"--option", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();

        String[] arguments = {"-"};

        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-a"};

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-ab"};

        String[] expected = {"-a", "b"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-a", "value"};

        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-a", "value"};

        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}
