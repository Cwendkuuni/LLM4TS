import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.group.OptionGroupBuilder;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws OptionException {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withArgName("arg1")
                .withDescription("Description for option1")
                .isRequired()
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withArgName("arg2")
                .withDescription("Description for option2")
                .create());
        options.add(OptionBuilder.withLongOpt("option3")
                .withArgName("arg3")
                .withDescription("Description for option3")
                .create());
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("Description for the group")
                .create());
        options.add(OptionBuilder.withLongOpt("group-arg")
                .withDescription("Description for the group argument")
                .isRequired(false)
                .create());

        final OptionGroup group = new GroupImpl(options, "group", "Description for the group", 1, 3);

        // When
        final CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.listIterator());

        // Then
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("group", commandLine.getOptions().get(0).getName());
        assertEquals(3, commandLine.getOptions(0).getOptions().size());
        assertEquals("option1", commandLine.getOptionValue("option1"));
        assertEquals("arg1", commandLine.getOptionValues("option1")[0]);
        assertEquals("option2", commandLine.getOptionValue("option2"));
        assertEquals("arg2", commandLine.getOptionValues("option2")[0]);
        assertEquals("option3", commandLine.getOptionValue("option3"));
        assertEquals("arg3", commandLine.getOptionValues("option3")[0]);
        assertNull(commandLine.getOptionValue("group-arg"));

        // When printing help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyApp", group, true);
    }
}