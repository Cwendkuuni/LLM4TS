import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Validate;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.test.ArgumentImplTestData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.cli2.test.ArgumentImplTestData.getArgumentWithMinMax;
import static org.apache.commons.cli2.test.ArgumentImplTestData.getArgumentWithNoDefaults;
import static org.apache.commons.cli2.test.ArgumentImplTestData.getArgumentWithSingleDefault;
import static org.apache.commons.cli2.test.ArgumentImplTestData.getArgumentWithValues;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl_MinimumAndMaximum() throws OptionException {
        Option option = getArgumentWithMinMax();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("test1", "test2", "test3").iterator());
        Assert.assertEquals(2, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
    }

    @Test
    public void testArgumentImpl_Minimum() throws OptionException {
        Option option = getArgumentWithMinMax();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("test1", "test2", "test3", "test4").iterator());
        Assert.assertEquals(3, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
        Assert.assertEquals("test3", cmd.getValues(option).get(2));
    }

    @Test
    public void testArgumentImpl_Maximum() throws OptionException {
        Option option = getArgumentWithMinMax();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("test1", "test2", "test3").iterator());
        Assert.assertEquals(2, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
    }

    @Test
    public void testArgumentImpl_NoDefaults() throws OptionException {
        Option option = getArgumentWithNoDefaults();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("test1", "test2", "test3").iterator());
        Assert.assertEquals(3, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
        Assert.assertEquals("test3", cmd.getValues(option).get(2));
    }

    @Test
    public void testArgumentImpl_SingleDefault() throws OptionException {
        Option option = getArgumentWithSingleDefault();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, Arrays.asList("default"), null, 0);
        argument.process(cmd, Arrays.asList("test1", "test2").iterator());
        Assert.assertEquals(2, cmd.getValues(option).size());
        Assert.assertEquals("test1", cmd.getValues(option).get(0));
        Assert.assertEquals("test2", cmd.getValues(option).get(1));
        Assert.assertArrayEquals(new String[]{"default"}, cmd.getOptionValues("default"));
    }

    @Test
    public void testArgumentImpl_Values() throws OptionException {
        Option option = getArgumentWithValues();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("--arg", "arg1", "arg2", "arg3").iterator());
        Assert.assertEquals(3, cmd.getValues(option).size());
        Assert.assertEquals("arg1", cmd.getValues(option).get(0));
        Assert.assertEquals("arg2", cmd.getValues(option).get(1));
        Assert.assertEquals("arg3", cmd.getValues(option).get(2));
    }

    @Test
    public void testArgumentImpl_Validator() throws OptionException {
        Option option = OptionBuilder.withLongOpt("arg")
                .withDescription("test argument")
                .withValidator(new Validate() {
                    @Override
                    public void validate(String value) throws InvalidArgumentException {
                        if (!value.matches("[A-Z]")) {
                            throw new InvalidArgumentException("Invalid value for arg: " + value);
                        }
                    }
                })
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.process(cmd, Arrays.asList("--arg", "test").iterator());
        ArgumentImplTestData.assertValidationErrorMessage(cmd, option, "Invalid value for arg: t");
    }

    @Test
    public void testArgumentImpl_ConsumeRemaining() throws OptionException {
        Option option = OptionBuilder.withLongOpt("arg")
                .withDescription("test argument")
                .create();
        CommandLine cmd = new CommandLine();
        ArgumentImpl argument = new ArgumentImpl("arg", "test", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.processValues(cmd, Arrays.asList("--arg", "--consume", "test1", "test2", "test3").iterator(), option);
        Assert.assertEquals(1, cmd.getValues(option).size());
        Assert.assertEquals("test1 test2 test3", cmd.getValues(option).get(0));
    }
}