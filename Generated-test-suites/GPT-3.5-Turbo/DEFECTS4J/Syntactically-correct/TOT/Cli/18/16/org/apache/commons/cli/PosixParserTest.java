import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] args;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        args = new String[]{};
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(args, flattened);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");
        
        args = new String[]{"-a", "input.txt", "output.txt", "-b"};
        String[] expected = new String[]{"-a", "input.txt", "output.txt"};
        
        String[] flattened = parser.flatten(options, args, true);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_StartWithDoubleHyphen() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        args = new String[]{"--input", "input.txt", "--output", "output.txt"};
        String[] expected = new String[]{"--input", "input.txt", "--output", "output.txt"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_StartWithSingleHyphen() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        args = new String[]{"-input", "input.txt", "-output", "output.txt"};
        String[] expected = new String[]{"-input", "input.txt", "-output", "output.txt"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_SingleOption() {
        options.addOption("a", false, "option a");

        args = new String[]{"-a"};
        String[] expected = new String[]{"-a"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        args = new String[]{"-a", "-b", "-c"};
        String[] expected = new String[]{"-a", "-b", "-c"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument() {
        options.addOption("a", true, "option a");

        args = new String[]{"-a", "input.txt"};
        String[] expected = new String[]{"-a", "input.txt"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgument() {
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        args = new String[]{"-a", "input.txt", "-b", "output.txt", "-c", "config.txt"};
        String[] expected = new String[]{"-a", "input.txt", "-b", "output.txt", "-c", "config.txt"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_MultipleOptionsWithMultipleArguments() {
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        args = new String[]{"-a", "input.txt", "in2.txt", "-b", "output.txt", "out2.txt", "-c", "config.txt"};
        String[] expected = new String[]{"-a", "input.txt", "in2.txt", "-b", "output.txt", "out2.txt", "-c", "config.txt"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_OptionWithInvalidId_StopAtNonOption() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        args = new String[]{"-c", "-a"};
        String[] expected = new String[]{"-c"};

        String[] flattened = parser.flatten(options, args, true);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_OptionWithInvalidId_ContinueFlattening() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        args = new String[]{"-c", "-a"};
        String[] expected = new String[]{"-c", "-a"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

    @Test
    public void testFlatten_OptionWithInvalidId_ContinueFlattening_NoStopAtNonOption() {
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        args = new String[]{"-c", "-a"};
        String[] expected = new String[]{"-c", "-a"};

        String[] flattened = parser.flatten(options, args, false);
        assertEquals(expected, flattened);
    }

}