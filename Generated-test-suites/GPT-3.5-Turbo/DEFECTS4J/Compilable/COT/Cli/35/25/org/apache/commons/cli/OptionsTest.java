package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test case to check the functionality of addOptionGroup method
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "add", false, "Add an item"));
        group.addOption(new Option("r", "remove", true, "Remove an item"));
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Test case to check the functionality of addOption method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");

        assertTrue(options.hasOption("x"));
        assertTrue(options.hasLongOption("execute"));
    }

    @Test
    public void testGetOptions() {
        // Test case to check the functionality of getOptions method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        // Test case to check the functionality of getRequiredOptions method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        assertEquals(0, options.getRequiredOptions().size());

        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "add", false, "Add an item"));
        group.addOption(new Option("r", "remove", true, "Remove an item"));
        options.addOptionGroup(group);

        assertEquals(1, options.getRequiredOptions().size());
    }

    @Test
    public void testGetOption() {
        // Test case to check the functionality of getOption method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        Option option1 = options.getOption("x");
        assertNotNull(option1);
        assertEquals("x", option1.getOpt());
        assertEquals("execute", option1.getLongOpt());

        Option option2 = options.getOption("help");
        assertNotNull(option2);
        assertEquals("h", option2.getOpt());
        assertEquals("help", option2.getLongOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        // Test case to check the functionality of getMatchingOptions method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        assertEquals(2, options.getMatchingOptions("e").size());
        assertEquals(1, options.getMatchingOptions("help").size());
    }

    @Test
    public void testHasOption() {
        // Test case to check the functionality of hasOption method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        assertTrue(options.hasOption("x"));
        assertTrue(options.hasLongOption("execute"));

        assertFalse(options.hasOption("test"));
        assertFalse(options.hasShortOption("print"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test case to check the functionality of getOptionGroup method
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "add", false, "Add an item"));
        group.addOption(new Option("r", "remove", true, "Remove an item"));
        options.addOptionGroup(group);

        Option option1 = options.getOption("a");
        assertEquals(group, options.getOptionGroup(option1));

        Option option2 = options.getOption("r");
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        // Test case to check the functionality of toString method
        Options options = new Options();
        options.addOption("x", "execute", false, "Execute a command");
        options.addOption("h", "help", false, "Print help");

        String expected = "[ Options: [ short {x=Option(data='null', args=0, optionalArg=false, required=false, longOpt='execute', description='Execute a command')} ] [ long {execute=Option(data='null', args=0, optionalArg=false, required=false, longOpt='execute', description='Execute a command')} ]]";
        assertEquals(expected, options.toString());
    }

}