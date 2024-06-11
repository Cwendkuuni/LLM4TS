import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImpl_ValidUsage() throws OptionException {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .valueSeparator('=')
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Argument Option")
                .create(false);
        options.addOption(argOption);

        GroupImpl group = new GroupImpl(
                Collections.singletonList(argOption),
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, new String[]{"--group", "test", "--arg", "test"});

        Assert.assertEquals("test", group.getName());
        Assert.assertEquals("Test Group", group.getDescription());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertEquals("test", cmd.getOptionValues("group")[0]);
        Assert.assertEquals(new String[0], cmd.getOptionValues("arg"));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_MissingRequiredOption() throws OptionException {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .isRequired(true)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        GroupImpl group = new GroupImpl(
                Collections.singletonList(new Argument("arg")),
                "Group",
                "Test Group",
                1,
                1);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, new String[0]);

        // this test should throw an exception
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_InvalidArgumentCount() throws OptionException {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        GroupImpl group = new GroupImpl(
                Collections.singletonList(new Argument("arg")),
                "Group",
                "Test Group",
                0,
                0);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, new String[]{"--group", "test"});

        // this test should throw an exception
    }

    @Test
    public void testGroupImpl_ArgumentOption() throws OptionException {
        final Options options = new Options();
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Argument Option")
                .create(false);
        options.addOption(argOption);

        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .valueSeparator('=')
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        GroupImpl group = new GroupImpl(
                Collections.singletonList(argOption),
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, new String[]{"--group", "test", "--arg", "test"});

        Assert.assertEquals("test", cmd.getOptionValues("group")[0]);
        Assert.assertEquals(new String[]{"test"}, cmd.getOptionValues("arg"));
    }

    @Test
    public void testGroupImpl_BurstedOption() throws OptionException {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Argument Option")
                .create(false);

        options.addOption(groupOption);
        options.addOption(argOption);

        GroupImpl group = new GroupImpl(
                new ArrayList<Option>(){{
                    add(argOption);
                    add(groupOption);
                }},
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, new String[]{"--arg", "test", "--group", "test"});

        Assert.assertEquals("test", cmd.getOptionValues("group")[0]);
        Assert.assertEquals(new String[0], cmd.getOptionValues("arg"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImpl_InvalidArgumentSeparator() {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .valueSeparator(';') // invalid separator
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        GroupImpl group = new GroupImpl(
                Collections.singletonList(new Argument("arg")),
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final CommandLine cmd = new CommandLine();
        try {
            cmd.parse(group, new String[]{"--group", "test;arg=value"});
        } catch (OptionException e) {
            throw new IllegalArgumentException("Parsing should not throw OptionException, " +
                    "only IllegalArgumentException for this test case", e);
        }

        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testGroupImpl_Help() {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .valueSeparator('=')
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Argument Option")
                .create(false);
        options.addOption(argOption);

        GroupImpl group = new GroupImpl(
                new ArrayList<Option>(){{
                    add(argOption);
                    add(groupOption);
                }},
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("TestGroup", group, true);
    }

    @Test
    public void testGroupImpl_HelpWithOptions() {
        final Options options = new Options();
        final Option groupOption = OptionBuilder.withLongOpt("group")
                .hasArg()
                .valueSeparator('=')
                .isRequired(false)
                .withDescription("Test Group Option")
                .create(false);
        options.addOption(groupOption);

        final Option argOption = OptionBuilder.withLongOpt("arg")
                .hasArg()
                .isRequired(false)
                .withDescription("Test Argument Option")
                .create(false);
        options.addOption(argOption);

        GroupImpl group = new GroupImpl(
                new ArrayList<Option>(){{
                    add(argOption);
                    add(groupOption);
                }},
                "Group",
                "Test Group",
                0,
                1);
        group.setOptions(options.getOptions());

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("TestGroup", group, true, true);
    }
}