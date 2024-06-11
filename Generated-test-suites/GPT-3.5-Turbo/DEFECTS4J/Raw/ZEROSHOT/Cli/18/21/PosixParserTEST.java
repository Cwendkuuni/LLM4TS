import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private static PosixParser parser;

    @BeforeClass
    public static void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        String[] arguments = {"-a", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument() {
        Options options = new Options();
        Option option = new Option("a", "optionA", true, "Option A");
        options.addOption(option);

        String[] arguments = {"-a", "value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"-a", "arg1", "--", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_BurstToken() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        String[] arguments = {"-abc", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlatten_BurstTokenStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        String[] arguments = {"-abc", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlatten_SpecialToken() {
        Options options = new Options();

        String[] arguments = {"--option=value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }
}