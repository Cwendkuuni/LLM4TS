import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testArgumentImplWithMinimumMaximumAndNoDefaults() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        options.addArgument(argument);

        CommandLine cmd = TestUtils.createCommandLine("java MyClass arg1 value1 arg2 value2");
        Assert.assertTrue(argument.canProcess(cmd, "value1"));
        Assert.assertTrue(argument.canProcess(cmd, "value2"));
        argument.process(cmd, cmd.getArgIterator());
        Assert.assertEquals("value1", cmd.getValues(argument).get(0));
        Assert.assertEquals("value2", cmd.getValues(argument).get(1));
        Assert.assertEquals(2, cmd.getValues(argument).size());
    }

    @Test
    public void testArgumentImplWithMinimumMaximumAndDefaults() {
        List<String> defaultValues = new ArrayList<>(Arrays.asList("default1", "default2"));
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, defaultValues, 0);
        options.addArgument(argument);

        CommandLine cmd = TestUtils.createCommandLine("java MyClass arg1");
        Assert.assertTrue(argument.canProcess(cmd, "default1"));
        Assert.assertTrue(argument.canProcess(cmd, "default2"));
        argument.process(cmd, cmd.getArgIterator());
        Assert.assertEquals("default1", cmd.getValues(argument).get(0));
        Assert.assertEquals(1, cmd.getValues(argument).size());

        cmd = TestUtils.createCommandLine("java MyClass arg1 value1 value2");
        argument.process(cmd, cmd.getArgIterator());
        Assert.assertEquals("value1", cmd.getValues(argument).get(0));
        Assert.assertEquals("value2", cmd.getValues(argument).get(1));
        Assert.assertEquals(2, cmd.getValues(argument).size());
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithMinimumGreaterThanMaximum() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 2, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        options.addArgument(argument);

        CommandLine cmd = TestUtils.createCommandLine("java MyClass arg1 value1 value2");
        argument.process(cmd, cmd.getArgIterator());
    }

    @Test
    public void testArgumentImplWithSubsequentSplit() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        argument.setSubsequentSplit(true);
        options.addArgument(argument);

        CommandLine cmd = TestUtils.createCommandLine("java MyClass arg1 value1 value2 value3");
        argument.process(cmd, cmd.getArgIterator());
        Assert.assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(argument));
    }

    @Test
    public void testArgumentImplWithValidator() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);
        options.addArgument(argument);

        CommandLine cmd = TestUtils.createCommandLine("java MyClass arg1 value1 value2");
        try {
            argument.process(cmd, cmd.getArgIterator());
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testArgumenImplWithUsage() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        options.addArgument(argument);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java MyClass", options);
        Assert.assertTrue(formatter.getMessage().contains("arg1"));
        Assert.assertTrue(formatter.getMessage().contains("argument 1"));
        Assert.assertTrue(formatter.getMessage().contains("(1)"));
    }
}

class MyValidator implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        if (values.get(0).equals("invalid")) {
            throw new InvalidArgumentException("Invalid argument provided.", new Exception());
        }
    }
}