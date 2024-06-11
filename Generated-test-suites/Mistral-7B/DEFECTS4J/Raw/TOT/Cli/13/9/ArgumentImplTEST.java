import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.strategies.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testArgumentWithoutValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        options.addArgument(arg);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{});
        assertNull(cmd.getValues(arg));
    }

    @Test
    public void testArgumentWithSingleValue() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("value"), 0);
        options.addArgument(arg);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{"arg", "value"});
        List values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testArgumentWithMultipleValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        options.addArgument(arg);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{"arg", "value1", "value2", "value3"});
        List values = cmd.getValues(arg);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithTooManyValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        options.addArgument(arg);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{"arg", "value1", "value2", "value3"});
    }

    @Test
    public void testArgumentWithConsumingOption() {
        Argument arg1 = new ArgumentImpl("arg1", "an argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "an argument 2", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, "--consume", null, 0);
        options.addArgument(arg1);
        options.addArgument(arg2);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{"--consume", "value1", "--consume", "value2"});
        List arg1Values = cmd.getValues(arg1);
        List arg2Values = cmd.getValues(arg2);

        assertEquals(2, arg1Values.size());
        assertEquals("value1", arg1Values.get(0));
        assertEquals("value2", arg1Values.get(1));
        assertEquals(2, arg2Values.size());
        assertEquals("value1", arg2Values.get(0));
        assertEquals("value2", arg2Values.get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidValue() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        options.addArgument(arg);

        CommandLine cmd = parser.parse(options.getOptions(), new String[]{"arg", "invalid-value"});
    }
}