package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.getRequiredOptions().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "option");
        options.addOption(option);
        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("o").equals(option));
        assertTrue(options.getOption("option").equals(option));
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testAddOption_onlyShortName() {
        Options options = new Options();
        options.addOption("o", "option");
        assertTrue(options.getOption("o").equals(options.getOption("option")));
    }

    @Test
    public void testAddOption_onlyShortName_hasArg() {
        Options options = new Options();
        options.addOption("o", true, "option");
        assertTrue(options.getOption("o").hasArg());
    }

    @Test
    public void testAddOption_shortAndLongName_hasArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "option");
        assertTrue(options.getOption("o").hasArg());
        assertTrue(options.getOption("option").hasArg());
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("o", "option1");
        Option option2 = new Option("o", "option2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option");
        option.setRequired(true);
        options.addOption(option);
        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getMatchingOptions("o").contains("option1"));
        assertTrue(options.getMatchingOptions("o").contains("option2"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertTrue(options.hasShortOption("o"));
    }
}