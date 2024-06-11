package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.Group;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Group group;
    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        final List optionList = new ArrayList();
        optionList.add(new Option("a", "option A", false, "a"));
        optionList.add(new Option("b", "option B", true, "b"));
        optionList.add(new Option("c", "option C", false, "c"));
        optionList.add(new Option("d", "option D", false, "d"));
        optionList.add(new OptionGroup("group1")
                .requiredOption(false)
                .maxOccurs(1)
                .options(optionList)
                .name("Group1")
                .description("This is a test Group1"));

        options = new Options();
        options.addOptionGroup(group = (Group) optionList.get(4));

        formatter = new HelpFormatter();
    }

    @Test
    public void testGroupImpl_ValidInvocation() {
        final CommandLine cmd = new CommandLine();
        cmd.parseOptions(group, new String[]{"-a", "-b", "-c"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(group.canProcess(cmd, null));
        assertEquals(3, cmd.getOptionValues("*").length);
    }

    @Test
    public void testGroupImpl_InvalidInvocation() {
        final CommandLine cmd = new CommandLine();
        cmd.parseOptions(group, new String[]{"-a", "-d"});

        assertFalse(cmd.hasOption("d"));
        assertTrue(group.canProcess(cmd, null));
        assertEquals(2, cmd.getOptionValues("*").length);

        cmd.parseOptions(group, new String[]{"-d"});
        assertFalse(cmd.hasOption("d"));
        assertFalse(group.canProcess(cmd, null));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_MinOccurs() {
        final CommandLine cmd = new CommandLine();
        group.setMinimum(1);
        cmd.parseOptions(group, new String[]{"-a"});
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_MaxOccurs() {
        final CommandLine cmd = new CommandLine();
        group.setMaximum(1);
        cmd.parseOptions(group, new String[]{"-a", "-a"});
    }

    @Test
    public void testGroupImpl_Help() {
        final String usage = formatter.printHelp("usage", group, true);
        assertTrue(usage.contains("Group1"));
        assertEquals(1, usage.split("\n").length);
        assertTrue(usage.contains("This is a test Group1"));
    }
}