package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GroupImpl}.
 */
public class GroupImplTest {

    private List<Option> options;
    private Group group;

    @Before
    public void setUp() {
        options = List.of(
                OptionBuilder.newOption("g", "group")
                        .required(false)
                        .longOpt("group-long")
                        .build(),
                OptionBuilder.newOption("a", "anonymous")
                        .required(false)
                        .build(),
                OptionBuilder.newOption("i", "integer")
                        .required(true)
                        .argName("integer")
                        .build(),
                OptionBuilder.newOption("s", "string")
                        .required(true)
                        .argName("string")
                        .build(),
                OptionBuilder.newOption("d", "default")
                        .required(false)
                        .longOpt("default-long")
                        .build()
        );

        group = new GroupImpl(options, "MyGroup", "This group has options g, a, i and s.", 1, 5);
    }

    @Test
    public void testValid() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parseOptions(group.parser(), new String[]{"-g", "1", "-a", "2", "-i", "3"});

        final Group parsedGroup = (Group) cmd.parseGroup(group);
        assertEquals("MyGroup", parsedGroup.getPreferredName());

        final List<Option> parsedOptions = parsedGroup.getOptions();
        assertEquals(3, parsedOptions.size());
        assertTrue(parsedOptions.get(0).isSet());
        assertTrue(parsedOptions.get(1).isSet());
        assertTrue(parsedOptions.get(2).getValue().equals("3"));
    }

    @Test
    public void testInvalid() throws ParseException {
        final CommandLine cmd = new CommandLine();
        try {
            cmd.parseOptions(group.parser(), new String[]{"-g", "1", "-a"});
            assertFalse("Should have thrown an exception.", true);
        } catch (final ParseException e) {
            assertEquals("Missing required option: i.", e.getMessage());
        }
    }

    @Test
    public void testProcess() {
        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        final boolean hasGroup = cmd.hasOption("group");
        assertTrue(hasGroup);
        assertEquals("1", cmd.getOptionValue("group"));

        final boolean hasAnonymous = cmd.hasOption("anonymous");
        assertTrue(hasAnonymous);

        final WriteableCommandLine parsedCmd = new WriteableCommandLine();
        group.process(parsedCmd, parsedCmd.iterator());

        final boolean parsedHasGroup = parsedCmd.hasOption("group");
        assertTrue(parsedHasGroup);
        assertEquals("1", parsedCmd.getOptionValue("group"));

        final boolean parsedHasAnonymous = parsedCmd.hasOption("anonymous");
        assertTrue(parsedHasAnonymous);
    }

    @Test
    public void testValidate() {
        final CommandLine cmd = new CommandLine();

        try {
            group.validate(cmd);
        } catch (final OptionException e) {
            assertEquals("Missing required option: i.", e.getMessage());
        }

        final CommandLine cmdWithAll = new CommandLine();
        cmdWithAll.parseOptions(group.parser(), new String[]{"-g", "1", "-a", "2", "-i", "3", "-s", "string"});
        group.validate(cmdWithAll);
    }

    @Test
    public void testHelp() {
        final HelpFormatter formatter = new HelpFormatter();

        final String help = formatter.printHelp("java MyClass", group.getUsageLine());
        assertNotNull(help);
        assertTrue(help.contains("MyGroup"));
        assertTrue(help.contains("This group has options g, a, i and s."));
    }
}