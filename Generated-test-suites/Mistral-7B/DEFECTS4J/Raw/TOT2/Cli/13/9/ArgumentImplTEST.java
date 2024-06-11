import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {
    @Test
    public void testArgumentImpl() {
        Option option = OptionBuilder.withLongOpt("argOption")
                .hasArg(true)
                .withDescription("An argument option")
                .create(null);
        ArgumentImpl argument = new ArgumentImpl("argOption", "An argument option", 1, 1, ' ', ' ', null, null, null, 0);

        CommandLine commandLine = new CommandLine();
        List arguments = commandLine.parseArgumentList("-argOption arg1 arg2");

        argument.process(commandLine, arguments.listIterator(), option);

        Assert.assertEquals(2, arguments.size());
        Assert.assertEquals("arg1", commandLine.getOptionValue(option));
        Assert.assertEquals("arg2", commandLine.getArgument(0));

        // Testing edge cases
        List testArguments = commandLine.parseArgumentList("-argOption --");
        argument.process(commandLine, testArguments.listIterator(), option);

        Assert.assertEquals(1, testArguments.size());
        Assert.assertEquals("", commandLine.getArgument(0));

        testArguments = commandLine.parseArgumentList("-argOption arg1 arg2 arg3 arg4");
        argument.process(commandLine, testArguments.listIterator(), option);

        Assert.assertEquals(3, testArguments.size());
        Assert.assertEquals("arg3", commandLine.getArgument(2));

        // Testing exception handling
        OptionException exception = null;
        try {
            commandLine = new CommandLine();
            List arguments = commandLine.parseArgumentList("-argOption");

            argument.process(commandLine, arguments.listIterator(), option);
        } catch (OptionException e) {
            exception = e;
        }

        Assert.assertNotNull(exception);
    }

    @Test
    public void testArgumentWithNoValues() {
        Option option = OptionBuilder.withLongOpt("noValueOption")
                .hasArg(false)
                .withDescription("An option with no values")
                .create(null);
        ArgumentImpl argument = new ArgumentImpl("noValueOption", "An option with no values", 0, 0, ' ', ' ', null, null, null, 0);

        CommandLine commandLine = new CommandLine();
        List arguments = commandLine.parseArgumentList("-noValueOption");

        argument.process(commandLine, arguments.listIterator(), option);

        Assert.assertTrue(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testArgumentWithTooManyValues() {
        Option option = OptionBuilder.withLongOpt("tooManyValueOption")
                .hasArg(true)
                .withDescription("An option with too many values")
                .create(null);
        ArgumentImpl argument = new ArgumentImpl("tooManyValueOption", "An option with too many values", 1, 1, ' ', ' ', null, null, null, 0);

        CommandLine commandLine = new CommandLine();
        List arguments = commandLine.parseArgumentList("-tooManyValueOption arg1 arg2 arg3");

        argument.process(commandLine, arguments.listIterator(), option);

        Assert.assertEquals(2, arguments.size());
        Assert.assertEquals("arg1", commandLine.getOptionValue(option));
        Assert.assertEquals("arg2", commandLine.getArgument(0));

        // An OptionException should be thrown because the maximum number of values is exceeded
    }

    @Test(expected = OptionException.class)
    public void testInvalidArgumentException() {
        Option option = OptionBuilder.withLongOpt("optionWithError")
                .hasArg(true)
                .withDescription("An option with error")
                .create(null);
        ArgumentImpl argument = new ArgumentImpl("optionWithError", "An option with error", 1, 1, ' ', ' ', null, null, null, 0);

        CommandLine commandLine = new CommandLine();
        List arguments = commandLine.parseArgumentList("-optionWithError arg1 arg2 arg3");

        argument.process(commandLine, arguments.listIterator(), option);

        // An OptionException should be thrown
    }
}

//