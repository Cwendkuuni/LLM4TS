package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        List<OptionGroup> optionGroups = new ArrayList<OptionGroup>(options.getOptionGroups());
        
        assertEquals(1, optionGroups.size());
        assertEquals(group, optionGroups.get(0));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "option 1");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        
        Option retrievedOption = options.getOption("a");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "option 1");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        
        Option retrievedOption = options.getOption("a");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("a", "longoption", false, "option with long name");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("longoption"));
        
        Option retrievedOption = options.getOption("longoption");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        options.addOption(option1);
        options.addOption(option2);
        
        List<Option> optionList = new ArrayList<Option>(options.getOptions());
        
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        
        List requiredOptions = options.getRequiredOptions();
        
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "option 1");
        options.addOption(option);
        
        Option retrievedOption1 = options.getOption("a");
        Option retrievedOption2 = options.getOption("option1");
        
        assertEquals(option, retrievedOption1);
        assertEquals(option, retrievedOption2);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        Option option3 = new Option("c", "option3", false, "option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        List<String> matchingOptions1 = options.getMatchingOptions("op");
        List<String> matchingOptions2 = options.getMatchingOptions("option");
        List<String> matchingOptions3 = options.getMatchingOptions("a");
        
        assertEquals(3, matchingOptions1.size());
        assertTrue(matchingOptions1.contains("option1"));
        assertTrue(matchingOptions1.contains("option2"));
        assertTrue(matchingOptions1.contains("option3"));
        
        assertEquals(3, matchingOptions2.size());
        assertTrue(matchingOptions2.contains("option1"));
        assertTrue(matchingOptions2.contains("option2"));
        assertTrue(matchingOptions2.contains("option3"));
        
        assertEquals(1, matchingOptions3.size());
        assertTrue(matchingOptions3.contains("option1"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "option 1");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "option 1");
        options.addOption(option);
        
        assertTrue(options.hasLongOption("option1"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "option 1");
        options.addOption(option);
        
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option1"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        OptionGroup retrievedGroup1 = options.getOptionGroup(option1);
        OptionGroup retrievedGroup2 = options.getOptionGroup(option2);
        
        assertEquals(group, retrievedGroup1);
        assertEquals(group, retrievedGroup2);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", true, "option 2");
        options.addOption(option1);
        options.addOption(option2);
        
        String expected = "[ Options: [ short {a=Option: a, option1, false, option 1, requiresArg=false, optionalArg=false} ] [ long {option1=Option: a, option1, false, option 1, requiresArg=false, optionalArg=false, longOpt=option1}, b=Option: b, option2, true, option 2, requiresArg=true, optionalArg=false} ]";
        
        assertEquals(expected, options.toString());
    }
}