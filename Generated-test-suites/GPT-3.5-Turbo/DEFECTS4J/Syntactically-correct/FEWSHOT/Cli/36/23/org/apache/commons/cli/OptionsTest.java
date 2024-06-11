package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", true, "add option");
        Option option2 = new Option("r", "remove", true, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOption_onlyShortName() {
        Options options = new Options();
        options.addOption("h", "help", false, "show help");
        
        assertTrue(options.hasOption("h"));
        assertFalse(options.hasOption("help"));
    }

    @Test
    public void testAddOption_withHasArg() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        options.addOption("d", false, "default option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
    }

    @Test
    public void testAddOption_withLongName() {
        Options options = new Options();
        options.addOption("v", "version", true, "show version");
        
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("version"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        
        assertTrue(options.getOptions().isEmpty());
        
        options.addOption("f", true, "file option");
        options.addOption("d", false, "default option");
        
        assertTrue(options.getOptions().size() == 2);
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        
        assertTrue(options.getRequiredOptions().isEmpty());
        
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", true, "add option");
        option.setRequired(true);
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        
        assertTrue(options.getRequiredOptions().size() == 1);
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("h", "help", true, "show help");
        
        assertEquals(options.getOption("h"), options.getOption("help"));

        assertNull(options.getOption("v"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", true, "add option");
        Option option2 = new Option("add", true, "add option");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.getMatchingOptions(""), 0);
        assertEquals(options.getMatchingOptions("a").size(), 2);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("h", "help", true, "show help");
        
        assertTrue(options.hasOption("h"));
        assertTrue(options.hasOption("help"));
        assertFalse(options.hasOption("v"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("h", "help", true, "show help");
        
        assertTrue(options.hasLongOption("help"));
        assertFalse(options.hasLongOption("h"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("h", "help", true, "show help");
        
        assertTrue(options.hasShortOption("h"));
        assertFalse(options.hasShortOption("help"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", true, "add option");
        Option option2 = new Option("r", "remove", true, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        
        assertEquals(options.getOptionGroup(option1), optionGroup);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("h", "help", true, "show help");
        
        assertEquals(options.toString(), "[ Options: [ short {h=Option{opt=h, longOpt=help, hasArg=true, description='show help', required=false}} ] [ long {help=Option{opt=h, longOpt=help, hasArg=true, description='show help', required=false}} ]");
    }
}