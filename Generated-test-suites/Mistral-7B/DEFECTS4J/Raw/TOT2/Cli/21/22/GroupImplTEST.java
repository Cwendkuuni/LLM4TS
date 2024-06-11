import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImplWithValidOptions() {
        Options options = new Options();
        Option groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("A test group option")
                .isRequired()
                .create(true);
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("A test option 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("A test option 2")
                .create(true);
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("A test option 3")
                .create(true);

        groupOption.setValue("myGroup");
        options.addOptionGroup(new OptionGroup("myGroup", "My Group", groupOption, option1, option2, option3));

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd.parse(options, new String[]{"--group", "myGroup", "--option1", "value1", "--option2", "value2", "--option3", "value3"});
            assertEquals("group", cmd.getOptionValue("group"));
            assertEquals("value1", cmd.getOptionValue("option1"));
            assertEquals("value2", cmd.getOptionValue("option2"));
            assertEquals("value3", cmd.getOptionValue("option3"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String help = formatter.printHelp("GroupImplTest", options);
        System.out.println(help);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplMissingRequiredOption() {
        Options options = new Options();
        Option groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("A test group option")
                .isRequired()
                .create(true);
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("A test option 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("A test option 2")
                .create(true);

        groupOption.setValue("myGroup");
        options.addOptionGroup(new OptionGroup("myGroup", "My Group", groupOption, option1, option2));

        CommandLine cmd = new CommandLine();

        try {
            cmd.parse(options, new String[]{"--option1", "value1", "--option2", "value2"});
            fail("An OptionException should be thrown due to missing required option.");
        } catch (OptionException e) {
            assertEquals("group", e.getOption().getLongOpt());
        }
    }

    @Test
    public void testGroupImplExtraOptions() {
        Options options = new Options();
        Option groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("A test group option")
                .isRequired()
                .create(true);
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("A test option 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("A test option 2")
                .create(true);

        groupOption.setValue("myGroup");
        options.addOptionGroup(new OptionGroup("myGroup", "My Group", groupOption, option1, option2));

        CommandLine cmd = new CommandLine();

        try {
            cmd.parse(options, new String[]{"--group", "myGroup", "--option1", "value1", "--option2", "value2", "--extraOption", "valueExtra"});
            fail("An OptionException should be thrown due to extra options not allowed.");
        } catch (OptionException e) {
            assertEquals("extraOption", e.getOption().getLongOpt());
        }
    }
}