import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        final List<Option> optionsList = new ArrayList<>();
        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("longOption1")
                .isRequired()
                .create(false);
        optionsList.add(option1);

        final Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("longOption2")
                .withMinValues(2)
                .withMaxValues(4)
                .isRequired()
                .create(int.class);
        optionsList.add(option2);

        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Group")
                .withLongOpt("longGroup")
                .create(GroupImpl.class);
        optionsList.add(groupOption);

        options = new Options();
        options.addOptions(optionsList);
    }

    @Test
    public void testCanProcessValid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        for (final Object option : group.getOptions().toArray()) {
            final Option testedOption = (Option) option;
            assertTrue(group.canProcess(commandLine, testedOption.getValue()));
        }
    }

    @Test(expected = OptionException.class)
    public void testCanProcessInvalid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        final String invalidOption = "invalidOption";

        assertFalse(group.canProcess(commandLine, invalidOption));
    }

    @Test
    public void testProcessValid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        for (final Object option : group.getOptions().toArray()) {
            final Option testedOption = (Option) option;

            group.process(commandLine, commandLine.iterator());

            assertTrue(commandLine.hasOption(testedOption.getValue()));
            assertTrue(testedOption.isSet(commandLine));
        }
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        final String invalidOption = "invalidOption";

        group.process(commandLine, commandLine.iterator());

        assertFalse(commandLine.hasOption(invalidOption));
    }

    @Test
    public void testValidateValid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        final Option option1 = (Option) options.findOption("option1");
        commandLine.parse(options, new String[]{"--longOption1", "--option1"});

        final GroupImpl group = (GroupImpl) options.findOption("group");

        group.validate(commandLine);

        fail();
    }

    @Test
    public void testGetName() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertEquals("Group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();

        final GroupImpl group = (GroupImpl) options.findOption("group");

        group.appendUsage(sb, Collections.emptySet(), null);

        assertEquals("group (Group)\n", sb.toString());
    }

    @Test
    public void testHelpLines() {
        final List<String> lines = new ArrayList<>();

        final GroupImpl group = (GroupImpl) options.findOption("group");

        lines.addAll(group.helpLines(0, Collections.emptySet(), null));

        assertEquals(1, lines.size());
        assertEquals("group (Group)\n", lines.get(0));

        lines.clear();
        lines.addAll(group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null));

        assertEquals(1, lines.size());
        assertEquals("group (Group)\n", lines.get(0));

        lines.clear();
        lines.addAll(group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null));

        assertEquals(4, lines.size());
        assertEquals("group (Group)\n", lines.get(0));
        assertEquals("option1\n", lines.get(1));
        assertEquals("  Option 1\n", lines.get(2));
        assertEquals("longOption1\n", lines.get(3));

        lines.clear();
        lines.addAll(group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), new ReverseStringComparator()));

        assertEquals(1, lines.size());
        assertEquals("group (Group)\n", lines.get(0));

        lines.clear();
        lines.addAll(group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), new ReverseStringComparator()));

        assertEquals(4, lines.size());
        assertEquals("longOption3 option2\n", lines.get(0));
        assertEquals("  option2\n", lines.get(1));
        assertEquals("    Option 2\n", lines.get(2));
        assertEquals("longOption2\n", lines.get(3));
    }

    @Test
    public void testFindOption() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertEquals(option2, group.findOption("option2"));
        assertEquals(option1, group.findOption("option1"));
        assertNull(group.findOption("invalidOption"));
    }

    @Test
    public void testGetMinimum() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertEquals(2, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertEquals(4, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final GroupImpl group = (GroupImpl) options.findOption("group");

        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        options.parse(commandLine);

        final GroupImpl group = (GroupImpl) options.findOption("group");

        group.defaults(commandLine);

        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("option3"));

        assertEquals(null, option1.getValue(commandLine));
        assertEquals(null, option2.getValue(commandLine));

        assertEquals(0, option2.getCount(commandLine));
    }
}