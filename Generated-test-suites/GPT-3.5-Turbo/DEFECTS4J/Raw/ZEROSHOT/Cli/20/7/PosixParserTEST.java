
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "-b", "value", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "-b", "value", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"--b=value"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--b", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"--a"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"-bvalue"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-b", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }
}

