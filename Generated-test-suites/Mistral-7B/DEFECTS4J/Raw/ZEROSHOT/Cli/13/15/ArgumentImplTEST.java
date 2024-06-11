import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        // Test case 1: Minimum and maximum are 1
        Argument arg1 = new ArgumentImpl("arg1", "Test Argument 1", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 1);

        // Test case 2: Minimum is 1 and maximum is 3
        Argument arg2 = new ArgumentImpl("arg2", "Test Argument 2", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 2);

        // Test case 3: Minimum is 0 and maximum is unlimited
        Argument arg3 = new ArgumentImpl("arg3", "Test Argument 3", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 3);

        // Test processing values
        testProcessValues(arg1, new String[]{"test1"});
        testProcessValues(arg1, new String[]{"test1", "test2"});
        testProcessValues(arg1, new String[]{"test1", "test2", "test3"});
        testProcessValues(arg1, new String[]{"test1", "test2", "test3", "test4"});

        testProcessValues(arg2, new String[]{"test1", "test2"});
        testProcessValues(arg2, new String[]{"test1", "test2", "test3"});
        testProcessValues(arg2, new String[]{"test1", "test2", "test3", "test4"});
        testProcessValues(arg2, new String[]{"test1", "test2", "test3", "test4", "test5"});

        testProcessValues(arg3, new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"});

        // Test validate
        testValidate(arg1, new String[]{"test1", "test2"});
        testValidate(arg1, new String[]{"test1", "test2", "test3"});
        testValidate(arg1, new String[]{"test1", "test2", "test3", "test4"});
        testValidate(arg1, new String[]{"test1", "test2", "test3", "test4", "test5"});
        testValidate(arg1, new String[]{"test1", "test2", "test3", "test4", "test5", "test6"});

        testValidate(arg2, new String[]{"test1", "test2"});
        testValidate(arg2, new String[]{"test1", "test2", "test3"});
        testValidate(arg2, new String[]{"test1", "test2", "test3", "test4"});
        testValidate(arg2, new String[]{"test1", "test2", "test3", "test4", "test5"});
        testValidate(arg2, new String[]{"test1", "test2", "test3", "test4", "test5", "test6"});
        testValidate(arg2, new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"});

        testValidate(arg3, new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"});
        testValidate(arg3, new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8"});

        // Test consumeRemaining
        testConsumeRemaining(arg1, new String[]{"--consume-remaining"});
        testConsumeRemaining(arg1, new String[]{"--consume-remaining", "arg"});
        testConsumeRemaining(arg1, new String[]{"--consume-remaining", "arg", "arg2"});
        testConsumeRemaining(arg1, new String[]{"--consume-remaining", "arg", "arg2", "arg3"});

        testConsumeRemaining(arg2, new String[]{"--consume-remaining"});
        testConsumeRemaining(arg2, new String[]{"--consume-remaining", "arg"});
        testConsumeRemaining(arg2, new String[]{"--consume-remaining", "arg", "arg2"});
        testConsumeRemaining(arg2, new String[]{"--consume-remaining", "arg", "arg2", "arg3"});

        testConsumeRemaining(arg3, new String[]{"--consume-remaining"});
        testConsumeRemaining(arg3, new String[]{"--consume-remaining", "arg"});
        testConsumeRemaining(arg3, new String[]{"--consume-remaining", "arg", "arg2"});
        testConsumeRemaining(arg3, new String[]{"--consume-remaining", "arg", "arg2", "arg3"});
        testConsumeRemaining(arg3, new String[]{"--consume-remaining", "arg", "arg2", "arg3", "arg4"});

        // Test defaultValues
        Argument argWithDefaultValues = new ArgumentImpl("argWithDefaultValues", "Test Argument with Default Values", 0, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default1", "default2"), 0);

        CommandLine cmd = new CommandLine();
        testDefaultValues(argWithDefaultValues, cmd);
        testDefaultValues(argWithDefaultValues, cmd, "arg1");
        testDefaultValues(argWithDefaultValues, cmd, "arg1", "arg2");
        testDefaultValues(argWithDefaultValues, cmd, "arg1", "arg2", "arg3");
    }

    private void testProcessValues(Argument arg, String[] args) {
        ParseContext parseContext = new ParseContext();
        Option option = OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .withArgName(arg.getName())
                .create(arg);

        OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg).process(parseContext, args);

        WriteableCommandLine commandLine = parseContext.getCommandLine();
        List values = commandLine.getValues(option);

        Assert.assertEquals(values.size(), arg.getMinimum());
        for (Object value : values) {
            Assert.assertEquals(value.toString(), args[0]);
        }

        // Process the rest of arguments
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg);
        arg.process(commandLine, parseContext.getArguments().listIterator());

        List newValues = commandLine.getValues(option2);
        Assert.assertEquals(newValues.size(), arg.getMaximum() - arg.getMinimum());
        for (Object value : newValues) {
            Assert.assertEquals(value.toString(), args[args.length - arg.getMaximum() + arg.getMinimum()]);
        }
    }

    private void testValidate(Argument arg, String[] args) {
        ParseContext parseContext = new ParseContext();
        Option option = OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg);

        OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg).process(parseContext, args);

        try {
            arg.validate(parseContext.getCommandLine(), option);
        } catch (OptionException e) {
            return;
        }

        Assert.fail("Expected OptionException not thrown.");
    }

    private void testConsumeRemaining(Argument arg, String[] args) {
        ParseContext parseContext = new ParseContext();
        Option option = OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg);

        OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg).process(parseContext, args);

        Option option2 = OptionBuilder.withLongOpt("consume-remaining")
                .create(new OptionImpl(1, false, false, null, null, null, null, null, null, null));

        OptionBuilder.withLongOpt("testOption")
                .withValueSeparator(arg.getInitialSeparator())
                .create(arg).process(parseContext, args);

        OptionBuilder.withLongOpt("consume-remaining")
                .create(option2).process(parseContext, parseContext.getArguments().listIterator());

        Assert.assertEquals(parseContext.getArguments().size(), args.length - arg.getMaximum() + arg.getMinimum());
        for (int i = 0; i < args.length - arg.getMaximum() + arg.getMinimum(); ++i) {
            Assert.assertEquals(args[i], parseContext.getArguments().get(i));
        }
    }

    private void testDefaultValues(Argument arg, WriteableCommandLine commandLine) {
        arg.defaults(commandLine);

        List values = commandLine.getValues("testOption");
        Assert.assertEquals(values.size(), arg.getMinimum());
        for (Object value : values) {
            Assert.assertEquals(value.toString(), arg.getDefaultValues().get(0));
        }
    }

    private void testDefaultValues(Argument arg, WriteableCommandLine commandLine, String... prefixes) {
        arg.defaults(commandLine);

        String argName = arg.getName();
        for (String prefix : prefixes) {
            argName = prefix + ":" + argName;
        }

        List values = commandLine.getValues(argName);
        Assert.assertEquals(values.size(), arg.getMinimum());
        for (Object value : values) {
            Assert.assertEquals(value.toString(), arg.getDefaultValues().get(0));
        }
    }
}

class HelpLineImpl extends HelpLine {
    public HelpLineImpl(Option option, int depth) {
        super(option, depth);
    }

    @Override
    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        if (getDepth() > 0) {
            sb.append("\n");
            for (int i = 0; i < getDepth(); ++i) {
                sb.append("  ");
            }
        }
        sb.append(super.getHelp());
        return sb.toString();
    }
}