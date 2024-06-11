package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        assertEquals(option, options.getOption("i"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroup(option1) == group);
    }

    @Test
    public void testAddOptionWithDescription() {
        Options options = new Options();
        options.addOption("h", "help", false, "display help message");
        assertTrue(options.hasOption("h"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("v", "version", false, "display version");
        options.addOption(option);
        assertEquals(option, options.getOption("v"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("v", "verbose", false, "enable verbose mode");
        assertEquals(2, options.getMatchingOptions("o").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("h", "help", false, "display help message");
        assertTrue(options.hasOption("h"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("h", "help", false, "display help message");
        String expected = "[ Options: [ short {h=Option: h }, [ long {help=Option: help } ]";
        assertEquals(expected, options.toString());
    }
}