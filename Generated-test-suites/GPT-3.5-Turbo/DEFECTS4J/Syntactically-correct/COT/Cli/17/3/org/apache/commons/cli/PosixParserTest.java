
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "value1", "--option2", "value2", "-b", "value3"};
        String[] expected = {"-a", "value1", "--option2", "value2", "-b", "value3"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "value1", "--option2", "value2", "-b", "value3", "nonOption"};
        String[] expected = {"-a", "value1", "--option2", "value2", "-b", "value3", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionToken() {
        String[] arguments = {"-d"};
        String[] expected = {"-d"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstToken() {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNonOption() {
        String[] arguments = {"nonOption"};
        String[] expected = {"nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithNonOption() {
        String[] arguments = {"-a", "value1", "--option2", "value2", "-b", "value3", "nonOption"};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}

