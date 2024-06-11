import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImplEmpty() throws ParseException {
        List options = new ArrayList();
        GroupImpl group = new GroupImpl(options, null, null, 0, 0, false);

        CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.parser().parse(group.getOptions().toArray(new Option[0])));

        assertTrue(cmd.hasNoArgs());
    }

    @Test
    public void testGroupImplBasic() throws ParseException {
        List options = new ArrayList();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);
        options.add(option1);

        GroupImpl group = new GroupImpl(options, "MyGroup", "My Group", 1, 5, false);

        CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.parser().parse(group.getOptions().toArray(new Option[0])));

        assertTrue(cmd.hasOption("option1"));
    }

    @Test(expected = OptionException.class)
    public void testGroupImplMinimum() throws ParseException {
        List options = new ArrayList();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create(true);
        options.add(option1);
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "MyGroup", "My Group", 2, 2, false);

        CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.parser().parse(new String[0]));

        fail("Should throw OptionException for minimum number of options.");
    }

    @Test(expected = OptionException.class)
    public void testGroupImplMaximum() throws ParseException {
        List options = new ArrayList();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create(true);
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("Option 3")
                .create(true);
        options.add(option1);
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "MyGroup", "My Group", 2, 2, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.getOptions().toArray(new Option[0]), new String[]{"option1", "option1", "option1"});

        fail("Should throw OptionException for maximum number of options.");
    }
}