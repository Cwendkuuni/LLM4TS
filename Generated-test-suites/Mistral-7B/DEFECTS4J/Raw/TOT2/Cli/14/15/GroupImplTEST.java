import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupWithRequiredOptions() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with required options", 1, 1);

        final Options cmdline = new Options();
        cmdline.addOption(group.getOption("option1", true, "Option 1"));
        cmdline.addOption(group.getOption("option2", true, "Option 2"));

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group", "--option1", "--option2"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(2, parsed.getOptions().size());
        Assert.assertTrue(parsed.hasOption("option1"));
        Assert.assertTrue(parsed.hasOption("option2"));
    }

    @Test
    public void testGroupWithMinimumOptions() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with minimum options", 2, 2);

        final Options cmdline = new Options();
        cmdline.addOption(group.getOption("option1", true, "Option 1"));
        cmdline.addOption(group.getOption("option2", true, "Option 2"));

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group", "--option1", "--option2", "--option3"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(3, parsed.getOptions().size());
        Assert.assertTrue(parsed.hasOption("option1"));
        Assert.assertTrue(parsed.hasOption("option2"));
        Assert.assertTrue(parsed.hasOption("option3"));
    }

    @Test
    public void testGroupWithMaximumOptions() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with maximum options", 2, 3);

        final Options cmdline = new Options();
        cmdline.addOption(group.getOption("option1", true, "Option 1"));
        cmdline.addOption(group.getOption("option2", true, "Option 2"));
        cmdline.addOption(group.getOption("option3", true, "Option 3"));

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group", "--option1", "--option2", "--option3", "--option4"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(4, parsed.getOptions().size());
        Assert.assertTrue(parsed.hasOption("option1"));
        Assert.assertTrue(parsed.hasOption("option2"));
        Assert.assertTrue(parsed.hasOption("option3"));
        Assert.assertTrue(parsed.hasOption("option4"));

        Assert.assertEquals("Too many options for group 'group'", group.getMessage());
    }

    @Test
    public void testGroupWithMissingOptions() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with missing options", 1, 1);

        final Options cmdline = new Options();
        cmdline.addOption(group.getOption("option1", true, "Option 1"));

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(0, parsed.getOptions().size());
        Assert.assertFalse(parsed.hasOption("option1"));

        Assert.assertEquals("Missing required option 'option1' for group 'group'", group.getMessage());
    }

    @Test
    public void testGroupWithArgument() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with argument", 0, 1);

        final Option argument = new Option("arg", true, "Argument for the group");
        options.add(argument);

        final Options cmdline = new Options();
        cmdline.addOption(group);

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group=argValue"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(1, parsed.getOptions().size());
        Assert.assertTrue(parsed.hasOption("group"));
        Assert.assertEquals("argValue", parsed.getOptionValue("group"));
    }

    @Test
    public void testGroupWithAnonymousOptions() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, null, null, 0, 1);

        final Option option1 = new Option("option1", true, "Option 1");
        final Option option2 = new Option("option2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        final Options cmdline = new Options();
        cmdline.addOption(group);

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group=option1=value1,option2=value2"});
        group.parse(parsed, cmdline, null);

        Assert.assertEquals(2, parsed.getOptions().size());
        Assert.assertTrue(parsed.hasOption("option1"));
        Assert.assertEquals("value1", parsed.getOptionValue("option1"));
        Assert.assertTrue(parsed.hasOption("option2"));
        Assert.assertEquals("value2", parsed.getOptionValue("option2"));
    }

    @Test
    public void testHelp() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with help", 0, 1);

        final Option option1 = new Option("option1", true, "Option 1");
        options.add(option1);

        final Options cmdline = new Options();
        cmdline.addOption(group);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Tests for GroupImpl", cmdline.getOptions(), true);
    }

    @Test(expected = OptionException.class)
    public void testInvalidOption() throws Exception {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "Group with invalid option", 0, 1);

        final Options cmdline = new Options();
        cmdline.addOption(group);

        final CommandLine parsed = new CommandLine(new String[]{"test", "--group=invalid"});
        group.parse(parsed, cmdline, null);

        Assert.fail();
    }
}