import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();

        Option option1 = new Option("a", "option A");
        option1.setRequired(true);
        option1.setArgs(2);
        options.addOption(option1);

        Option option2 = new Option("b", "option B");
        option2.setArgs(Option.UNLIMITED_VALUES);
        option2.setOptionalArg(true);
        options.addOption(option2);

        Option option3 = new Option("c", "option C");
        option3.setRequired(true);
        options.addOption(option3);

        arguments = new String[]{"-a", "value1", "value2", "-b", "value3", "value4", "-c"};
    }

    @Test
    public void testFlatten() {
        String[] tokens = parser.flatten(options, arguments, true);

        assertEquals("-a", tokens[0]);
        assertEquals("value1", tokens[1]);
        assertEquals("value2", tokens[2]);
        assertEquals("-b", tokens[3]);
        assertEquals("value3", tokens[4]);
        assertEquals("value4", tokens[5]);
        assertEquals("-c", tokens[6]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] tokens = parser.flatten(options, arguments, false);

        assertEquals("-a", tokens[0]);
        assertEquals("value1", tokens[1]);
        assertEquals("value2", tokens[2]);
        assertEquals("-b", tokens[3]);
        assertEquals("value3", tokens[4]);
        assertEquals("value4", tokens[5]);
        assertEquals("-c", tokens[6]);
    }

    @Test
    public void testFlatten_MissingRequiredOptions() {
        String[] arguments = new String[]{"-b", "value3", "value4"};

        try {
            parser.flatten(options, arguments, true);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing required option: c", e.getMessage());
        }
    }

    @Test
    public void testFlatten_InvalidOption() {
        String[] arguments = new String[]{"-d"};

        try {
            parser.flatten(options, arguments, true);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unrecognized option: -d", e.getMessage());
        }
    }
}