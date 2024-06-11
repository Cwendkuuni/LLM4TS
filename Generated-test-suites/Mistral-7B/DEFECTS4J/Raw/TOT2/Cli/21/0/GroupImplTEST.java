import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.GroupImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {
    @Test
    public void testGroupImplBasicFunctionality() {
        List <Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("Option 2")
                .withValueSeparator('=')
                .create();
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "Group", null, 0, 1, false);

        CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.parser().getArgumentIterator());

        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertEquals(0, cmd.getOptionValues("option2").length);
    }

    @Test
    public void testGroupImplEdgeCases() {
        List <Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("Option 2")
                .withValueSeparator('=')
                .create();
        Option option3 = new OptionBuilder()
                .withLongOpt("option3")
                .withDescription("Option 3")
                .isRequired()
                .create();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Group group = new GroupImpl(options, "Group", null, 1, 1, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();
        try {
            group.process(cmd, cmd.parser().getArgumentIterator());
            fail("Missing required option exception not thrown");
        } catch (OptionException e) {
            // Expected
        }

        cmd.parse(new String[]{"--group", "--option3", "--option1=value1", "--option2=value2"});
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value1", cmd.getOptionValues("option1")[0]);
        assertEquals("value2", cmd.getOptionValues("option2")[0]);
        assertEquals(2, cmd.getOptionValues("option2").length);
    }
}