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
    public void testFlatten_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("d", true, "Option d");
        String[] arguments = {"-a", "-b", "value1", "-c=value2", "-dvalue3"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "value1", "-c", "value2", "-dvalue3"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("d", true, "Option d");
        String[] arguments = {"-a", "-b", "value1", "-c=value2", "-dvalue3", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "-b", "value1", "-c", "value2", "-dvalue3", "nonOption"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialToken() {
        Options options = new Options();
        String[] arguments = {"--special"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"--special"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_optionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "-c"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = {"-abcd"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "cd"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_notHandledToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = {"other"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"--", "other"};

        assertArrayEquals(expected, result);
    }

}