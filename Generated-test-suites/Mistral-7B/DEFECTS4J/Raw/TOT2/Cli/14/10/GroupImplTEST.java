import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.group.GroupImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImplWithBasicOptions() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createOption("option2", "opt2", "This is option 2", false));

        final Group group = new GroupImpl("MyGroup", null, options, 1, 1);
        final CommandLine cmd = new CommandLine();

        cmd.parse(group.parse());
        Assert.assertEquals("option1", cmd.getOptionValue("option1"));
        Assert.assertFalse(cmd.hasOption("option2"));
    }

    @Test
    public void testGroupImplWithMinimumAndMaximum() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createOption("option2", "opt2", "This is option 2", false));

        final Group group = new GroupImpl("MyGroup", null, options, 1, 2);
        final CommandLine cmd = new CommandLine();

        cmd.parse(group.parse());
        Assert.assertEquals("option1", cmd.getOptionValue("option1"));
        Assert.assertEquals("option2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testGroupImplWithAnonymousArguments() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createArgument("myArg", "This is my argument"));

        final Group group = new GroupImpl("MyGroup", null, options, 1, 1);
        final CommandLine cmd = new CommandLine();

        cmd.parse(group.parse());
        Assert.assertEquals("This is my argument", cmd.getOptionValue("myArg"));
    }

    @Test
    public void testGroupImplWithEdgeCases() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createOption("option2", "opt2", "This is option 2", false));

        final Group group = new GroupImpl("MyGroup", null, options, 0, 2);
        final CommandLine cmd = new CommandLine();

        cmd.parse(group.parse());
        Assert.assertNull(cmd.getOptionValue("option1"));
        Assert.assertNull(cmd.getOptionValue("option2"));

        // test with 3 options
        cmd.parse(group.parse());
        Assert.assertEquals(3, cmd.getNumOptions());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplWithInvalidArgument() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createArgument("myArg", "This is my argument"));

        final Group group = new GroupImpl("MyGroup", null, options, 1, 1);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());
        cmd.parse(group.parse());
    }

    @Test
    public void testGroupImplHelp() {
        final List options = new ArrayList();
        options.add(createOption("option1", "opt1", "This is option 1", true));
        options.add(createOption("option2", "opt2", "This is option 2", false));

        final Group group = new GroupImpl("MyGroup", null, options, 1, 1);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", "Usage: MyGroup [OPTIONS]", true);
    }

    private Option createOption(String longName, String shortName, String description, boolean required) {
        return new OptionBuilder()
                .withLongOpt(longName)
                .withShortOpt(shortName)
                .withDescription(description)
                .isRequired(required)
                .create();
    }

    private Option createArgument(String longName, String description) {
        return new OptionBuilder()
                .withLongOpt(longName)
                .withDescription(description)
                .isRequired(false)
                .hasArg(true)
                .createArg();
    }
}