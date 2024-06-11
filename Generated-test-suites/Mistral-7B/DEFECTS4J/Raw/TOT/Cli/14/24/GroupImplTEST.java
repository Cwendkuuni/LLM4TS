package org.apache.commons.cli2.option;

import org.apache.commons.cli2. ArgentumUniverse;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionsParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List<Option> options;

    @Before
    public void setup() throws Exception {
        options = List.of(
                OptionBuilder.withName("a").withDescription("Option A").withLongOpt("long-a").build(),
                OptionBuilder.withName("b").withDescription("Option B").withLongOpt("long-b").build(),
                OptionBuilder.withName("c").withDescription("Option C").withLongOpt("long-c").build(),
                OptionBuilder.withName("group").withDescription("Group").build(),
                OptionBuilder.withName("d").withDescription("Option D").withLongOpt("long-d").build()
        );

        final OptionGroup group = new OptionGroupImpl(
                "group-options",
                "Group Options:",
                Collections.unmodifiableList(options),
                null,
                1,
                3
        );

        group.setValue(ArgentumUniverse.getInstance().getParser());
    }

    @Test
    public void testCanProcessWithMinimum() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b", "--c"});
        assertTrue(group.canProcess(cmd, "--group"));
    }

    @Test
    public void testCanProcessWithMaximum() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b", "--c", "--d"});
        assertFalse(group.canProcess(cmd, "--group"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalid() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b"});
        group.canProcess(cmd, "--group");
    }

    @Test
    public void testCanProcessWithTrigger() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--long-a"});
        assertTrue(group.canProcess(cmd, "--long-a"));
    }

    @Test
    public void testCanProcessWithBurstedTrigger() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--long-a", "--long-a"});
        assertTrue(group.canProcess(cmd, "--long-a"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidTrigger() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--long-x"});
        group.canProcess(cmd, "--long-x");
    }

    @Test
    public void testCanProcessWithArg() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "abc"});
        assertTrue(group.canProcess(cmd, "--a"));
    }

    @Test
    public void testProcess() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b", "--c"});
        group.process(cmd, cmd.iterator());

        assertEquals("Option A", cmd.getValue("a"));
        assertEquals("Option B", cmd.getValue("b"));
        assertEquals("Option C", cmd.getValue("c"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group"});
        group.process(cmd, cmd.iterator());
    }

    @Test
    public void testValidateWithValid() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b", "--c"});
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMinimum() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group"});
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public
    void testValidateWithMaximum() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b", "--c", "--d"});
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissing() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a"});
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingArg() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a"});
        group.validate(cmd);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group Options:", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertNotNull(group.getOptions());
        assertEquals(3, group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertNotNull(group.getAnonymous());
        assertEquals(0, group.getAnonymous().size());
    }

    @Test
    public void testFindOption() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b"});
        final Option optionA = group.findOption("a");
        assertNotNull(optionA);
        assertEquals("Option A", optionA.getLongOpt());
    }

    @Test
    public void testFindOptionWithNoMatch() throws Exception {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser(), new String[]{"--group", "--a", "--b"});
        final Option optionX = group.findOption("x");
        assertNull(optionX);
    }

    @Test
    public void testHelpLines() throws Exception {
        final HelpFormatter formatter = new HelpFormatter();
        final String help = formatter.printHelp(
                0,
                group.getParser(),
                true,
                false,
                "",
                true
        );
        assertTrue(help.contains("Group Options:"));
        assertTrue(help.contains("--long-a"));
        assertTrue(help.contains("Groupp (optional, repeating)"));
    }

    @Test
    public void testUsage() throws Exception {
        final String usage = group.getParser().getUsage();
        assertTrue(usage.contains("Group Options:"));
        assertTrue(usage.contains("--long-a"));
        assertTrue(usage.contains("Groupp (optional, repeating)"));
    }
}