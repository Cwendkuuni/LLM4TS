package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithMinimumAndMaximum() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .isRequired()
                .create(false));
        options.add(OptionBuilder.withLongOpt("b")
                .withDescription("Option B")
                .withValueOptional(true)
                .create(null));
        options.add(OptionBuilder.withLongOpt("c")
                .withDescription("Option C")
                .create(null));

        final String[] args = {"--a", "--b", "--c", "--a"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 1, 3).parse(cmd, args);

        Assert.assertEquals("Option A and Option B should be present", 2, cmd.getNumArgs());
        Assert.assertFalse("Option C should not be present", cmd.hasOption("c"));
    }

    @Test
    public void testGroupWithMinimumAndMaximumWithMoreOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .isRequired()
                .create(false));
        options.add(OptionBuilder.withLongOpt("b")
                .withDescription("Option B")
                .withValueOptional(true)
                .create(null));
        options.add(OptionBuilder.withLongOpt("c")
                .withDescription("Option C")
                .create(null));

        final String[] args = {"--a", "--b", "--c", "--a", "--c"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 1, 3).parse(cmd, args);

        Assert.assertEquals("Option A and Option B should be present", 2, cmd.getNumArgs());
        Assert.assertTrue("Option C should be present", cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMinimumButNoOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .isRequired()
                .create(false));

        final String[] args = {};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 1, 1).parse(cmd, args);

        Assert.fail("A ParseException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMoreOptionsThanMaximum() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .isRequired()
                .create(false));
        options.add(OptionBuilder.withLongOpt("b")
                .withDescription("Option B")
                .withValueOptional(true)
                .create(null));

        final String[] args = {"--a", "--b", "--b"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 1, 2).parse(cmd, args);

        Assert.fail("A ParseException should have been thrown");
    }

    @Test
    public void testGroupWithAnonymousArg() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .create(false));
        options.add(OptionBuilder.withLongOpt("b")
                .withDescription("Option B")
                .create(null));
        options.add(OptionBuilder.withValueOpt("c")
                .withDescription("Anonymous argument C")
                .create());

        final String[] args = {"--a", "--b", "C"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 0, Integer.MAX_VALUE).parse(cmd, args);

        Assert.assertEquals("Option A and Option B should be present", 2, cmd.getNumArgs());
        Assert.assertEquals("Anonymous argument C should be present", "C", cmd.getOptionValues("c"));
    }

    @Test
    public void testGroupWithTriggers() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .create(false));
        options.add(OptionBuilder.withLongOpt("b")
                .withDescription("Option B")
                .withValueOptional(true)
                .create(null));
        options.add(OptionBuilder.withLongOpt("c")
                .withDescription("Option C")
                .create(null));
        options.add(OptionBuilder.withLongOpt("d")
                .withDescription("Option D")
                .create(null));

        final String[] args = {"--a", "--c", "--d"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 0, Integer.MAX_VALUE).parse(cmd, args);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", "", true);

        Assert.assertEquals("The group should have the correct options",
                "--a\n" +
                "--b <value>\n" +
                "--c\n" +
                "--d\n", formatter.getHelpMessage());
    }

    @Test
    public void testGroupWithNamedAndAnonymousArgs() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("a")
                .withDescription("Option A")
                .withValueOptional(false)
                .create(false));
        options.add(OptionBuilder.withValueOpt("b")
                .withDescription("Anonymous argument B")
                .create());
        options.add(OptionBuilder.withLongOpt("c")
                .withDescription("Option C")
                .create(null));

        final String[] args = {"--a", "B", "--c"};

        final CommandLine cmd = new CommandLine();
        new GroupImpl(options, "MyGroup", "My Group", 0, Integer.MAX_VALUE).parse(cmd, args);

        Assert.assertEquals("Option A should be present", true, cmd.hasOption("a"));
        Assert.assertEquals("Anonymous argument B should be present", "B", cmd.getOptionValues("b")[0]);
        Assert.assertEquals("Option C should be present", true, cmd.hasOption("c"));
    }
}