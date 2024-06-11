package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2. Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private Options options;

    @Before
    public void init() {
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withLongOpt("group1")
                .withDescription("This is group 1 option")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group1-arg")
                .withDescription("This is argument for group 1 option")
                .isRequired(false)
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2")
                .withDescription("This is group 2 option")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2-arg")
                .withDescription("This is argument for group 2 option")
                .isRequired(false)
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group3")
                .withDescription("This is group 3 option")
                .create());

        options = new Options();
        options.addGroup(new GroupImpl(groupOptions, "MyGroup", "This is my group"));
    }

    @Test
    public void testGroupWithValidArguments() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1=value1 --group1-arg=value2 --group2=value3");

        Assert.assertEquals("value1", cmd.getOptionValue("group1"));
        Assert.assertEquals("value2", cmd.getOptionValue("group1-arg"));
        Assert.assertEquals("value3", cmd.getOptionValue("group2"));
        Assert.assertNull(cmd.getOptionValue("group3"));
    }

    @Test(expected = ParseException.class)
    public void testGroupWithInvalidArguments() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1=value1 --group3=value3");
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMissingArguments() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1=value1");
    }

    @Test
    public void testGroupWithMissingGroupOption() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1-arg=value2");
    }

    @Test
    public void testGroupWithAnonymousArguments() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1=value1 -- --anonymous-arg=value");

        Assert.assertEquals("value", cmd.getOptionValue("anonymous-arg"));
    }

    @Test
    public void testGroupWithDuplicateOptions() throws ParseException {
        final CommandLine cmd = new CommandLine("-g", options);
        cmd.parse("--group1=value1 --group1=value2");
    }

    @Test
    public void testPrintHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        final String help = formatter.printHelp("MyApp", options, false, false, false);
        System.out.println(help);
    }
}