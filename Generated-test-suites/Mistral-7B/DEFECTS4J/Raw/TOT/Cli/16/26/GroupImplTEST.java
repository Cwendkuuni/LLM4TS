package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setup() {
        options = new Options();

        options.addGroup(new GroupImpl(
                asList(
                        new Option("g1", "group1", true, "g1 description"),
                        new Option("o1", "option1", true, "o1 description"),
                        new Option("o2", "option2", true, "o2 description"),
                        new Argument("a1", "argument1", true, "a1 description")
                ),
                "group1",
                "group1 description"
        ));

        options.addGroup(new GroupImpl(
                asList(
                        new Option("g2", "group2", true, "g2 description"),
                        new Option("o3", "option3", true, "o3 description"),
                        new Option("o4", "option4", true, "o4 description"),
                        new Argument("a2", "argument2", true, "a2 description")
                ),
                "group2",
                "group2 description"
        ));

        formatter = new HelpFormatter();
    }

    @Test
    public void testGroupWithOneOption() {
        CommandLine cmd = new CommandLine("-g1");
        try {
            options.parse(cmd);
            fail("Should throw an OptionException for unrecognized option -g1");
        } catch (OptionException e) {
            // Expected
        }

        cmd = new CommandLine();
        cmd.parse(options, new String[]{"-g1"});
        assertEquals("group1 description", cmd.getOptionValue("g1"));
        assertNull(cmd.getOptionValue("o1"));
        assertNull(cmd.getOptionValue("o2"));
        assertNull(cmd.getOptionValue("a1"));
    }

    @Test
    public void testGroupWithMultipleOptions() {
        CommandLine cmd = new CommandLine("-g1 -o1 -o2 a1");
        try {
            options.parse(cmd);
            assertEquals("true", cmd.getOptionValue("g1"));
            assertEquals("true", cmd.getOptionValue("o1"));
            assertEquals("true", cmd.getOptionValue("o2"));
            assertEquals("a1 description", cmd.getOptionValue("a1"));
        } catch (OptionException e) {
            fail("Should parse command line correctly");
        }

        cmd = new CommandLine("-g1 -o1 -o2 a1 a2");
        try {
            options.parse(cmd);
            fail("Should throw an OptionException for too many arguments for group1");
        } catch (OptionException e) {
            // Expected
        }

        cmd = new CommandLine("-g1 -o1 a1");
        try {
            options.parse(cmd);
            fail("Should throw an OptionException for missing option -o2 for group1");
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testGroupWithMultipleGroups() {
        CommandLine cmd = new CommandLine("-g1 -o1 -g2 -o3 a1 a2");
        try {
            options.parse(cmd);
            assertEquals("group1 description", cmd.getOptionValue("g1"));
            assertEquals("true", cmd.getOptionValue("o1"));
            assertNull(cmd.getOptionValue("o2"));
            assertNull(cmd.getOptionValue("a1"));

            assertEquals("group2 description", cmd.getOptionValue("g2"));
            assertEquals("true", cmd.getOptionValue("o3"));
            assertEquals("a2 description", cmd.getOptionValue("a2"));
            assertNull(cmd.getOptionValue("g2.o3"));
            assertNull(cmd.getOptionValue("g2.o4"));
            assertNull(cmd.getOptionValue("a2.a"));
        } catch (OptionException e) {
            fail("Should parse command line correctly");
        }

        cmd = new CommandLine("-g1 -o1 -g2 -o3 a1 a2 a3");
        try {
            options.parse(cmd);
            fail("Should throw an OptionException for too many arguments for group2");
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testHelp() {
        String help = formatter.printHelp("test", options, true);
        assertTrue(help.contains("group1 description"));
        assertTrue(help.contains("group2 description"));
        assertTrue(help.contains("option1 description"));
        assertTrue(help.contains("option2 description"));
        assertTrue(help.contains("option3 description"));
        assertTrue(help.contains("option4 description"));
        assertTrue(help.contains("argument1 description"));
        assertTrue(help.contains("argument2 description"));
    }

    private List<Object> asList(Object... objects) {
        return new ArrayList<Object>(List.of(objects));
    }
}