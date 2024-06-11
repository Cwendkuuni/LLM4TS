package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new OptionBuilder("g1").longOpt("group1")
                .desc("This is the first group").build());
        groupOptions.add(new OptionBuilder("g2").longOpt("group2")
                .desc("This is the second group").hasArg(true)
                .argName("GROUP_ARG")
                .build());
        groupOptions.add(new OptionBuilder("g3").longOpt("group3")
                .desc("This is the third group").build());

        List<Option> argOptions = new ArrayList<>();
        argOptions.add(new OptionBuilder("a1").argName("ARG_1")
                .hasArg(true)
                .desc("This is an argument of the first group")
                .build());
        argOptions.add(new OptionBuilder("a2").argName("ARG_2")
                .hasArg(true)
                .desc("This is an argument of the second group")
                .build());

        List<Option> allOptions = new ArrayList<>();
        allOptions.add(new OptionBuilder("o1").longOpt("option1")
                .desc("This is an unrelated option")
                .build());
        allOptions.addAll(groupOptions);
        allOptions.addAll(argOptions);

        options = new Options();
        options.addGroup(new GroupBuilder().name("myGroup")
                .description("This is my group")
                .addOptions(groupOptions)
                .build());
        options.addOption(options.getOption("o1"));
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = new CommandLine("myCommand");
        cmd.parseOptions(options);

        assertTrue(options.getGroup("myGroup").canProcess(cmd, "g1"));
        assertTrue(options.getGroup("myGroup").canProcess(cmd, "g2"));
        assertTrue(options.getGroup("myGroup").canProcess(cmd, "g3"));
        assertFalse(options.getGroup("myGroup").canProcess(cmd, "g4"));

        cmd.addOption("--a1", "ARG_1_VALUE");
        assertTrue(options.getGroup("myGroup").canProcess(cmd, "g2"));
        assertTrue(options.getGroup("myGroup").canProcess(cmd, "a1"));
    }

    @Test
    public void testProcess() {
        CommandLine cmd = new CommandLine("myCommand");
        cmd.parseOptions(options);

        options.getGroup("myGroup").process(cmd, cmd.iterator());

        assertEquals(2, cmd.getArgs().size());
        assertEquals("ARG_1_VALUE", cmd.getArgs().get(0));
        assertEquals("ARG_2_VALUE", cmd.getArgs().get(1));
    }

    @Test(expected = OptionException.class)
    public void testInvalidOption() {
        CommandLine cmd = new CommandLine("myCommand");
        cmd.parseOptions(options);

        options.getGroup("myGroup").process(cmd, cmd.iterator());
        cmd.addOption("--i1", "INVALID_OPTION");

        options.getGroup("myGroup").process(cmd, cmd.iterator());
    }

    @Test(expected = OptionException.class)
    public void testInvalidArgument() {
        CommandLine cmd = new CommandLine("myCommand");
        cmd.parseOptions(options);

        cmd.addOption("--g2", "GROUP_ARG_VALUE");
        cmd.addOption("--a1", "INVALID_ARGUMENT");

        try {
            options.getGroup("myGroup").process(cmd, cmd.iterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testValidate() {
        CommandLine cmd = new CommandLine("myCommand");

        try {
            options.getGroup("myGroup").validate(cmd);
        } catch (OptionException e) {
            fail();
        }

        cmd.addOption("--g1");
        cmd.addOption("--g2");
        cmd.addOption("--g1");

        try {
            options.getGroup("myGroup").validate(cmd);
            fail();
        } catch (OptionException e) {
            // expected
        }

        cmd.addOption("--a1", "ARG_1_VALUE");

        try {
            options.getGroup("myGroup").validate(cmd);
            fail();
        } catch (OptionException e) {
            // expected
        }

        cmd.addOption(null);

        try {
            options.getGroup("myGroup").validate(cmd);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("myCommand", options);

        assertTrue(help.contains("This is my group"));
        assertTrue(help.contains("g1"));
        assertTrue(help.contains("g2 [GROUP_ARG]"));
        assertTrue(help.contains("g3"));
        assertTrue(help.contains("a1 [ARG_1]"));
        assertTrue(help.contains("a2 [ARG_2]"));
        assertFalse(help.contains("INVALID_OPTION"));
    }
}