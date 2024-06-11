import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithMinimumValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(
                "arg1",
                "Description for arg1",
                1, 1,
                ':', ':',
                null,
                "--",
                null,
                1);

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--arg1", "value1"});

        assertEquals("value1", commandLine.getOptionValues("arg1")[0]);
        assertEquals(1, commandLine.getOptionValues("arg1").length);
    }

    @Test
    public void testArgumentWithMaximumValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(
                "arg1",
                "Description for arg1",
                1, 2,
                ':', ':',
                null,
                "--",
                null,
                1);

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--arg1", "value1", "value2"});

        assertEquals("value1", commandLine.getOptionValues("arg1")[0]);
        assertEquals("value2", commandLine.getOptionValues("arg1")[1]);
        assertEquals(2, commandLine.getOptionValues("arg1").length);
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithLessValuesThanMinimum() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(
                "arg1",
                "Description for arg1",
                2, 2,
                ':', ':',
                null,
                "--",
                null,
                1);

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--arg1", "value1"});

        fail("An OptionException should be thrown because the minimum number of values is not provided.");
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMoreValuesThanMaximum() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(
                "arg1",
                "Description for arg1",
                1, 2,
                ':', ':',
                null,
                "--",
                null,
                1);

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--arg1", "value1", "value2", "value3"});

        fail("An OptionException should be thrown because the maximum number of values is exceeded.");
    }

    @Test
    public void testArgumentWithDefaultValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(
                "arg1",
                "Description for arg1",
                1, 1,
                ':', ':',
                null,
                "--",
                Arrays.asList("value1", "value2"),
                1);

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[0]);

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getOptionValues("arg1"));
    }
}