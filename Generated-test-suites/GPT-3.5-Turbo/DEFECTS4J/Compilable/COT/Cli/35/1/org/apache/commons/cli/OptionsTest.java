package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertFalse(option.isRequired());
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(option.isRequired());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertTrue(options.hasLongOption("add"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "remove", false, "Remove option");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("rem").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertTrue(options.hasOption("add"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertTrue(options.hasLongOption("add"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "Add option");
        options.addOption(option);
        assertEquals("[ Options: [ short {a=Option{ opt=a, longOpt='add', hasArg=false, description='Add option' }} ] [ long {add=Option{ opt=a, longOpt='add', hasArg=false, description='Add option' }} ]", options.toString());
    }
}