package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("Test argument", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(' ', arg.getInitialSeparator());
        assertEquals(' ', arg.getSubsequentSeparator());
        assertFalse(arg.isRequired());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNull(arg.getDefaultValues());
    }

    @Test
    public void testArgumentProcessValues() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test");
        args.add("arg1");
        args.add("arg2");

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("arg1", commandLine.getValues(option).get(0));
        assertEquals("arg2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testArgumentProcessValuesWithQuotes() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test=\"arg1 arg2\"");

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(1, commandLine.getValues(option).size());
        assertEquals("arg1 arg2", commandLine.getValues(option).get(0));
    }

    @Test
    public void testArgumentProcessValuesWithConsumingOption() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--consume", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test");
        args.add("arg1");
        args.add("--consume");
        args.add("arg3");
        args.add("arg4");

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(3, commandLine.getValues(option).size());
        assertEquals("arg1", commandLine.getValues(option).get(0));
        assertEquals("arg3", commandLine.getValues(option).get(1));
        assertEquals("arg4", commandLine.getValues(option).get(2));
    }

    @Test
    public void testArgumentProcessValuesWithInitialSeparator() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ':', ' ', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test:arg1:arg2");

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("arg1", commandLine.getValues(option).get(0));
        assertEquals("arg2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testArgumentProcessValuesWithSubsequentSeparator() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ',', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test");
        args.add("arg1,arg2");

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(1, commandLine.getValues(option).size());
        assertEquals("arg1,arg2", commandLine.getValues(option).get(0));
    }

    @Test
    public void testArgumentProcessValuesWithValidator() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', new MyValidator(), "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> args = new ArrayList<>();
        args.add("--test");
        args.add("invalid");

        try {
            arg.processValues(commandLine, args.listIterator(), option);
            fail();
        } catch (OptionException e) {
            // expected
        }

        assertEquals(0, commandLine.getValues(option).size());
    }

    @Test
    public void testArgumentValidate() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> values = new ArrayList<>();
        values.add("arg1");
        values.add("arg2");

        commandLine.setValues(option, values);

        try {
            arg.validate(commandLine, option);
        } catch (OptionException e) {
            fail();
        }
    }

    @Test
    public void testArgumentValidateWithInvalidValue() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withArgName("arg")
                .withArgDescription("Test argument")
                .create();

        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        WriteableCommandLine commandLine = new CommandLine("");

        List<String> values = new ArrayList<>();
        values.add("arg1");
        values.add("arg2");
        values.add("invalid");

        commandLine.setValues(option, values);

        try {
            arg.validate(commandLine, option);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testArgumentAppendUsage() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        StringBuffer buffer = new StringBuffer();
        arg.appendUsage(buffer, null, null);

        assertEquals(" [arg]                        Test argument\n", buffer.toString());
    }

    @Test
    public void testArgumentAppendUsageWithOptional() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        StringBuffer buffer = new StringBuffer();
        arg.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL), null);

        assertEquals("[ [arg]                        Test argument ]\n", buffer.toString());
    }

    @Test
    public void testArgumentAppendUsageWithNumbered() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        StringBuffer buffer = new StringBuffer();
        arg.appendUsage(buffer, null, null);
        arg.appendUsage(buffer, null, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED));

        assertEquals("[ 1:arg                        Test argument ]\n", buffer.toString());
    }

    @Test
    public void testArgumentAppendUsageWithOptionalAndNumbered() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        StringBuffer buffer = new StringBuffer();
        arg.appendUsage(buffer,
                Collections.unmodifiableSet(Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL)), null);
        arg.appendUsage(buffer, null, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED));

        assertEquals("[ [1:arg]                        Test argument ]\n", buffer.toString());
    }

    @Test
    public void testArgumentHelpLines() {
        Argument arg = new ArgumentImpl("arg", "Test argument", 1, 1, ' ', ' ', null, "--", null, null, 0);

        List<HelpLine> helpLines = arg.helpLines(0, null, null);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("", "", "", helpLines, 80, null);

        assertEquals("arg                        Test argument\n", help.substring(0, help.indexOf("\n\n")).trim());
    }

    public static class MyValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            if (values.get(0).equals("invalid")) {
                throw new InvalidArgumentException("Invalid argument", "invalid");
            }
        }
    }
}