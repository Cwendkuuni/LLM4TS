import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplWithNoValues() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with no values.")
                .withType(Argument.class)
                .withMinValues(0)
                .withMaxValues(1)
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with no values.", 0, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, cmd.parser().parse(new String[]{"--arg"}));
        Assert.assertEquals(0, cmd.getValues(option).size());
    }

    @Test
    public void testArgumentImplWithOneValue() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with one value.")
                .withType(Argument.class)
                .withMinValues(1)
                .withMaxValues(1)
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with one value.", 1, 1, ' ', ' ', null, null, null, 0);
        argument.process(cmd, cmd.parser().parse(new String[]{"--arg", "test"}));
        Assert.assertEquals("test", cmd.getValues(option).get(0));
    }

    @Test
    public void testArgumentImplWithMultipleValues() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with multiple values.")
                .withType(Argument.class)
                .withMinValues(1)
                .withMaxValues(3)
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with multiple values.", 1, 3, ' ', ' ', null, null, null, 0);
        argument.process(cmd, cmd.parser().parse(new String[]{"--arg", "test1", "test2", "test3"}));
        Assert.assertEquals(3, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
        Assert.assertEquals("test3", cmd.getValues(option).get(2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithMoreValuesThanAllowed() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with more values than allowed.")
                .withType(Argument.class)
                .withMinValues(1)
                .withMaxValues(3)
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with more values than allowed.", 1, 3, ' ', ' ', null, null, null, 0);
        argument.process(cmd, cmd.parser().parse(new String[]{"--arg", "test1", "test2", "test3", "test4"}));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithInvalidValue() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with an invalid value.")
                .withType(Argument.class)
                .withMinValues(1)
                .withMaxValues(1)
                .withValueSeparator(' ')
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with an invalid value.", 1, 1, ' ', ' ', null, null, null, 0);
        ArgumentBuilder argBuilder = ArgumentBuilder.withName("arg")
                .withDescription("A test argument with an invalid value.")
                .withType(String.class)
                .withMinValues(1)
                .withMaxValues(1)
                .withValueSeparator(' ')
                .create();
        try {
            argument.processValues(cmd, cmd.parser().parse("--arg test --arg invalid").listIterator(), option);
        } catch (OptionException e) {
            Assert.assertEquals(e.getMessage(), "arg: invalid is an unexpected option");
            throw e;
        }
    }

    @Test
    public void testArgumentImplWithDefaultValues() throws ParseException {
        Option option = OptionBuilder.withName("arg")
                .withDescription("A test argument with default values.")
                .withType(Argument.class)
                .withMinValues(0)
                .withMaxValues(1)
                .withValueSeparator(' ')
                .withDefaultValues(new String[]{"test1", "test2"})
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "A test argument with default values.", 0, 1, ' ', ' ', null, null, null, 0);
        cmd.setDefaultValues(option, ArgumentBuilder.create(String.class).withValues("test1", "test2").build().getDefaultValues());
        argument.process(cmd, cmd.parser().parse("--arg"));
        Assert.assertEquals(2, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
    }
}