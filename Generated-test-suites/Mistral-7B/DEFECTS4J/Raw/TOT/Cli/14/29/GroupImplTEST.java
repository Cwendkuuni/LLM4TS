package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import org.junit.Assert;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("test option 1")
                .create(Option.TYPE_NONE);
        group.addOption(option1);

        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("test option 2")
                .withValueSeparator('=')
                .withValues("value1", "value2")
                .create(Option.TYPE_VALUE_LIST);
        group.addOption(option2);

        final Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("test option 3")
                .create(Option.TYPE_NONE);
        group.addOption(option3);

        group.setMinOccurs(1);
        group.setMaxOccurs(2);

        final String[] args = new String[]{"test", "-g", "option1", "value1", "-g", "option3"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        parser.parse(group, args, commandLine, true);

        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(1, option1.getValues(commandLine).size());
        Assert.assertEquals("value1", option1.getValues(commandLine).get(0));
        Assert.assertEquals(0, option2.getValues(commandLine).size());

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test GroupImpl", group.getOptions(), true);
    }

    @Test(expected = ParseException.class)
    public void testGroupWithInvalidOptions() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("test option 1")
                .create(Option.TYPE_NONE);
        group.addOption(option1);

        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("test option 2")
                .withValueSeparator('=')
                .withValues("value1", "value2")
                .create(Option.TYPE_VALUE_LIST);
        group.addOption(option2);

        final Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("test option 3")
                .create(Option.TYPE_NONE);
        group.addOption(option3);

        group.setMinOccurs(1);
        group.setMaxOccurs(1); // invalid maxOccurs

        final String[] args = new String[]{"test", "-g"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        parser.parse(group, args, commandLine, true);
    }

    @Test
    public void testGroupWithAnonymousArguments() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("test option 1")
                .create(Option.TYPE_NONE);
        group.addOption(option1);

        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("test option 2")
                .create(Option.TYPE_ARG_LIST, "arg1", "arg2")
                .withArgName("arg")
                .isRequired(true);
        group.addOption(option2);

        group.setMinOccurs(1);
        group.setMaxOccurs(1);

        final String[] args = new String[]{"test", "-g", "option1", "arg1", "arg2"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        parser.parse(group, args, commandLine, true);

        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(2, option2.getValues(commandLine).size());
        Assert.assertEquals("arg1", option2.getValues(commandLine).get(0));
        Assert.assertEquals("arg2", option2.getValues(commandLine).get(1));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test GroupImpl", group.getOptions(), true);
    }

    @Test(expected = ParseException.class)
    public void testGroupWithInvalidAnonymousArguments() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("test option 1")
                .create(Option.TYPE_NONE);
        group.addOption(option1);

        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("test option 2")
                .create(Option.TYPE_ARG_LIST, "arg1", "arg2")
                .withArgName("arg")
                .isRequired(true);
        group.addOption(option2);

        group.setMinOccurs(1);
        group.setMaxOccurs(0); // invalid minOccurs

        final String[] args = new String[]{"test", "-g"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        parser.parse(group, args, commandLine, true);
    }
}