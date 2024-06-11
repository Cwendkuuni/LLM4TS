import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParserBuilder;
import org.apache.commons.cli2.group.GroupImpl;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for optionA"));
        options.add(new Option("b", "optionB", false, "Description for optionB"));
        options.add(new Option("c", "optionC", false, "Description for optionC"));
        options.add(new Option("d", "optionD", true, "Description for optionD"));
        options.add(new Option("e", "optionE", false, "Description for optionE"));
        options.add(new Option("f", "optionF", false, "Description for optionF"));
        options.add(new Option("g", "optionG", false, "Description for optionG"));
        options.add(new Option("h", "optionH", false, "Description for optionH"));

        final GroupImpl group = new GroupImpl(options, "MyGroup", "Description for MyGroup", 1, 5);

        // When
        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        // Then
        assertEquals(5, cmd.getNumArgs());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("h"));

        // When
        final String usage = group.usage();

        // Then
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", usage);

        // Then
        final String expectedUsage = "MyGroup:\n" +
                "  description for MyGroup\n" +
                "Options:\n" +
                "  -a, --optionA       Description for optionA\n" +
                "  -b, --optionB       Description for optionB\n" +
                "  -c, --optionC       Description for optionC\n" +
                "  -d, --optionD       Description for optionD\n" +
                "  -e, --optionE       Description for optionE\n" +
                "  -f, --optionF       Description for optionF\n" +
                "  -g, --optionG       Description for optionG\n" +
                "  -h, --optionH       Description for optionH\n" +
                "\n" +
                "Usage: MyGroup [OPTIONS] [ARGUMENTS...]\n";

        assertEquals(expectedUsage, formatter.getHelpText());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplInvalidOptionCount() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for optionA"));
        options.add(new Option("b", "optionB", false, "Description for optionB"));
        options.add(new Option("c", "optionC", false, "Description for optionC"));
        options.add(new Option("d", "optionD", true, "Description for optionD"));
        options.add(new Option("e", "optionE", false, "Description for optionE"));
        options.add(new Option("f", "optionF", false, "Description for optionF"));
        options.add(new Option("g", "optionG", false, "Description for optionG"));

        final GroupImpl group = new GroupImpl(options, "MyGroup", "Description for MyGroup", 1, 3);

        // When
        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        // Then (should throw an exception)
    }

    @Test(expected = OptionException.class)
    public void testGroupImplInvalidOption() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for optionA"));
        options.add(new Option("b", "optionB", false, "Description for optionB"));
        options.add(new Option("c", "optionC", false, "Description for optionC"));
        options.add(new Option("d", "optionD", true, "Description for optionD"));
        options.add(new Option("e", "optionE", false, "Description for optionE"));
        options.add(new Option("f", "optionF", false, "Description for optionF"));
        options.add(new Option("h", "optionH", false, "Description for optionH")); // should be g

        final GroupImpl group = new GroupImpl(options, "MyGroup", "Description for MyGroup", 1, 5);

        // When
        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        // Then (should throw an exception)
    }

    @Test
    public void testGroupImplAnonymousArguments() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for optionA"));
        options.add(new Option("g", "group", false, "Description for group"));
        options.add(new Option("1", "arg1", true, "Description for arg1"));
        options.add(new Option("2", "arg2", true, "Description for arg2"));
        options.add(new Option("3", "arg3", true, "Description for arg3"));

        final GroupImpl group = new GroupImpl(options, "MyGroup", "Description for MyGroup", 1, 3);

        // When
        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        // Then
        assertEquals(3, cmd.getNumArgs());
        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.getArgument(0).getValue().equals("arg1"));
        assertTrue(cmd.getArgument(1).getValue().equals("arg2"));
        assertTrue(cmd.getArgument(2).getValue().equals("arg3"));
    }

    @Test
    public void testGroupImplDefaultValues() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for optionA", "defaultValueA"));
        options.add(new Option("b", "optionB", false, "Description for optionB", "defaultValueB"));
        options.add(new Option("c", "optionC", false, "Description for optionC", "defaultValueC"));
        options.add(new Option("d", "optionD", true, "Description for optionD", "defaultValueD"));

        final GroupImpl group = new GroupImpl(options, "MyGroup", "Description for MyGroup", 1, 5);

        // When
        final CommandLine cmd = new CommandLine();
        group.defaults(cmd);

        // Then
        assertTrue(cmd.hasOption("a", "defaultValueA"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c", "defaultValueC"));
        assertTrue(cmd.hasOption("d", "defaultValueD"));
    }

    @Test
    public void testGroupImplNestedGroups() {
        // Given
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", false, "Description for group"));
        groupOptions.add(new Option("a", "optionA", true, "Description for optionA"));
        groupOptions.add(new Option("b", "optionB", false, "Description for optionB"));
        groupOptions.add(new Option("c", "optionC", false, "Description for optionC"));
        groupOptions.add(new Option("d", "optionD", true, "Description for optionD", new GroupImpl(Collections.emptyList(), "groupD", "Description for groupD", 1, 5)));

        final List<Option> outerOptions = new ArrayList<>();
        outerOptions.add(new Option("o", "outer", false, "Description for outer"));
        outerOptions.add(new Option("x", "optionX", false, "Description for optionX"));
        outerOptions.add(new Option("y", "optionY", false, "Description for optionY"));
        outerOptions.add(new Option("z", "optionZ", false, "Description for optionZ"));
        outerOptions.add(new Option("group", "group", false, "Description for group", new GroupImpl(groupOptions, "group", "Description for group", 1, 5)));

        final GroupImpl outerGroup = new GroupImpl(outerOptions, "outer", "Description for outer", 1, 10);

        // When
        final CommandLine cmd = new CommandLine();
        outerGroup.process(cmd, cmd.iterator());

        // Then
        assertEquals(5, cmd.getNumArgs());
        assertFalse(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("z"));
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));

        // When
        final String usage = outerGroup.usage();

        // Then
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("outer", usage);

        // Then
        final String expectedUsage = "outer:\n" +
                "  Description for outer\n" +
                "Options:\n" +
                "  -g, --group      Description for group\n" +
                "      Options:\n" +
                "        -a, --optionA  Description for optionA\n" +
                "        -b, --optionB  Description for optionB\n" +
                "        -c, --optionC  Description for optionC\n" +
                "        -d, --groupD  Description for groupD\n" +
                "\n" +
                "Usage: outer [OPTIONS] [ARGUMENTS...]\n";

        assertEquals(expectedUsage, formatter.getHelpText());
    }
}