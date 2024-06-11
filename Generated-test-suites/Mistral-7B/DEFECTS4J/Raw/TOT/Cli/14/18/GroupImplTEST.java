package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionsParser;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withName("a").withDescription("option A").build());
        options.add(OptionBuilder.withName("b").withDescription("option B").build());
        final OptionGroup group = new OptionGroupImpl(options, "group", "group options");

        final OptionsParser parser = new OptionsParser();
        parser.parse(new String[]{"-a", "-b", "--help"}, group);

        final CommandLine commandLine = parser.getCommandLine();
        assertNotNull(commandLine);
        assertFalse(commandLine.hasOption("help"));
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", group, true);

        final String help = formatter.getHelpText();
        assertTrue(help.contains("group options"));
        assertTrue(help.contains("option A"));
        assertTrue(help.contains("option B"));

        final String usage = formatter.getHomeUsage();
        assertTrue(usage.contains("-a"));
        assertTrue(usage.contains("-b"));
    }

    @Test
    public void testGroupWithMinMax() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withName("a").withDescription("option A").build());
        options.add(OptionBuilder.withName("b").withDescription("option B").build());
        final OptionGroup group = new OptionGroupImpl(options, "group", "group options", 1, 2);

        final OptionsParser parser = new OptionsParser();
        parser.parse(new String[]{"-a", "-b"}, group);

        final CommandLine commandLine = parser.getCommandLine();
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", group, true);

        final String help = formatter.getHelpText();
        assertTrue(help.contains("group options"));
        assertTrue(help.contains("option A"));
        assertTrue(help.contains("option B"));

        try {
            parser.parse(new String[]{"-a", "-b", "-a"}, group);
        } catch (final OptionException e) {
            assertEquals("Invalid number of options.", e.getMessage());
        }

        try {
            parser.parse(new String[]{"-a"}, group);
        } catch (final OptionException e) {
            assertEquals("Option 'a' is missing.", e.getMessage());
        }
    }

    @Test
    public void testGroupWithAnonymousArg() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withName("a").withDescription("option A").build());
        options.add(OptionBuilder.withName("arg").withDescription("anonymous argument").build().isRequired(false));
        final OptionGroup group = new OptionGroupImpl(options, "group", "group options");

        final OptionsParser parser = new OptionsParser();
        parser.parse(new String[]{"-a", "arg1"}, group);

        final CommandLine commandLine = parser.getCommandLine();
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertNotNull(commandLine.getOptionValues("arg"));
        assertEquals(1, commandLine.getOptionValues("arg").length);
        assertEquals("arg1", commandLine.getOptionValues("arg")[0]);

        try {
            parser.parse(new String[]{"-a"}, group);
        } catch (final OptionException e) {
            assertEquals("Option 'arg' is missing.", e.getMessage());
        }
    }

    @Test
    public void testGroupWithNestedOptions() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withName("a").withDescription("option A").build());
        options.add(OptionBuilder.withName("b").withDescription("option B").build());
        options.add(OptionBuilder.withName("c").withDescription("option C").build());
        options.add(OptionBuilder.withName("d").withDescription("option D").build());

        final OptionGroup groupA = new OptionGroupImpl(Collections.singletonList(OptionBuilder.withName("g").withDescription("group A").build()), "groupA", "group A options");
        groupA.addOption(OptionBuilder.withName("e").withDescription("option E").build());
        final OptionGroup groupB = new OptionGroupImpl(Collections.singletonList(OptionBuilder.withName("h").withDescription("group B").build()), "groupB", "group B options");
        groupB.addOption(OptionBuilder.withName("f").withDescription("option F").isRequired(false).build());
        groupA.addSubOption(groupB);
        options.add(groupA);

        final OptionsParser parser = new OptionsParser();
        parser.parse(new String[]{"-g", "-h", "-f", "val"}, options);

        final CommandLine commandLine = parser.getCommandLine();
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("g"));
        assertTrue(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("f"));
        assertNotNull(commandLine.getOptionValues("f"));
        assertEquals(1, commandLine.getOptionValues("f").length);
        assertEquals("val", commandLine.getOptionValues("f")[0]);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options, true);

        final String help = formatter.getHelpText();
        assertTrue(help.contains("group A options"));
        assertTrue(help.contains("option A"));
        assertTrue(help.contains("option B"));
        assertTrue(help.contains("option C"));
        assertTrue(help.contains("option D"));
        assertTrue(help.contains("group B options"));
        assertTrue(help.contains("option E"));
        assertTrue(help.contains("option F"));
    }
}