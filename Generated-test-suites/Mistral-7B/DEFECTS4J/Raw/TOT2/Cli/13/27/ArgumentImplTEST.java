import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithValidArguments() {
        Option option = OptionBuilder.withLongOpt("test")
                .isRequired(false)
                .hasArg()
                .withDescription("Test option")
                .withValidator(StringValidator.notNull())
                .create();

        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("test", "Test option", 1, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, TestUtils.args("--test arg1"));

        assertEquals("arg1", cmd.getValue(option));
    }

    @Test
    public void testArgumentWithMinimumArguments() {
        Option option = OptionBuilder.withLongOpt("test")
                .isRequired(false)
                .hasArg()
                .withDescription("Test option")
                .withValidator(StringValidator.notNull())
                .create();

        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("test", "Test option", 1, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, TestUtils.args("--test"));

        assertEquals(null, cmd.getValue(option));
        assertFalse(cmd.hasOption("test"));
    }

    @Test
    public void testArgumentWithMaximumArguments() {
        Option option = OptionBuilder.withLongOpt("test")
                .isRequired(false)
                .hasArg()
                .withDescription("Test option")
                .withValidator(StringValidator.notNull())
                .create();

        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("test", "Test option", 1, 3, ' ', ' ', null, null, null, 0);
        argument.process(cmd, TestUtils.args("--test", "--test", "--test"));

        assertEquals("arg1", cmd.getValue(option));
        assertEquals("arg2", cmd.getValue((Option) cmd.getOption("test"), 1));
        assertEquals("arg3", cmd.getValue((Option) cmd.getOption("test"), 2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidArgument() {
        Option option = OptionBuilder.withLongOpt("test")
                .isRequired(false)
                .hasArg()
                .withDescription("Test option")
                .withValidator(StringValidator.notNull())
                .create();

        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("test", "Test option", 1, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, TestUtils.args("--test invalid"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithTooManyArguments() {
        Option option = OptionBuilder.withLongOpt("test")
                .isRequired(false)
                .hasArg()
                .withDescription("Test option")
                .withValidator(StringValidator.notNull())
                .create();

        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("test", "Test option", 1, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, TestUtils.args("--test", "--test", "--test", "--test"));
    }
}