import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
 
    private Options options;
 
    @Before
    public void setUp() {
        options = new Options();
    }
 
    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
 
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("input"));
 
        Option retrievedOption = options.getOption("f");
        assertEquals("f", retrievedOption.getOpt());
        assertEquals("file", retrievedOption.getLongOpt());
        assertTrue(retrievedOption.hasArg());
        assertEquals("input file", retrievedOption.getDescription());
    }
 
    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("f", "input file");
 
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
 
        Option retrievedOption = options.getOption("f");
        assertEquals("f", retrievedOption.getOpt());
        assertEquals(null, retrievedOption.getLongOpt());
        assertFalse(retrievedOption.hasArg());
        assertEquals("input file", retrievedOption.getDescription());
    }
 
    @Test
    public void testAddOptionWithShortNameAndHasArg() {
        options.addOption("f", true, "input file");
 
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
 
        Option retrievedOption = options.getOption("f");
        assertEquals("f", retrievedOption.getOpt());
        assertEquals(null, retrievedOption.getLongOpt());
        assertTrue(retrievedOption.hasArg());
        assertEquals("input file", retrievedOption.getDescription());
    }
 
    @Test
    public void testAddOptionWithShortAndLongName() {
        options.addOption("f", "file", true, "input file");
 
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
 
        Option retrievedOption = options.getOption("f");
        assertEquals("f", retrievedOption.getOpt());
        assertEquals("file", retrievedOption.getLongOpt());
        assertTrue(retrievedOption.hasArg());
        assertEquals("input file", retrievedOption.getDescription());
    }
 
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        List<Option> retrievedOptions = new ArrayList<Option>(options.getOptions());
        assertEquals(2, retrievedOptions.size());
        assertTrue(retrievedOptions.contains(option1));
        assertTrue(retrievedOptions.contains(option2));
    }
 
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        List<String> retrievedRequiredOptions = options.getRequiredOptions();
        assertTrue(retrievedRequiredOptions.isEmpty());
 
        option1.setRequired(true);
 
        retrievedRequiredOptions = options.getRequiredOptions();
        assertFalse(retrievedRequiredOptions.isEmpty());
        assertTrue(retrievedRequiredOptions.contains("f"));
    }
 
    @Test
    public void testGetOption() {
        Option option1 = new Option("f", "file", true, "input file");
        options.addOption(option1);
 
        Option retrievedOption = options.getOption("f");
        assertEquals(option1, retrievedOption);
 
        retrievedOption = options.getOption("file");
        assertEquals(option1, retrievedOption);
 
        retrievedOption = options.getOption("input");
        assertEquals(null, retrievedOption);
    }
 
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        Option option3 = new Option("r", "result", true, "result file");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
 
        List<String> matchingOptions = options.getMatchingOptions("f");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("file"));
 
        matchingOptions = options.getMatchingOptions("fi");
        assertTrue(matchingOptions.isEmpty());
 
        matchingOptions = options.getMatchingOptions("d");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("directory"));
 
        matchingOptions = options.getMatchingOptions("re");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("result"));
 
        matchingOptions = options.getMatchingOptions("x");
        assertTrue(matchingOptions.isEmpty());
    }
 
    @Test
    public void testHasOption() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("directory"));
        assertFalse(options.hasOption("input"));
        assertFalse(options.hasOption("test"));
    }
 
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        assertTrue(options.hasLongOption("file"));
        assertTrue(options.hasLongOption("directory"));
        assertFalse(options.hasLongOption("input"));
        assertFalse(options.hasLongOption("test"));
    }
 
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        assertTrue(options.hasShortOption("f"));
        assertTrue(options.hasShortOption("d"));
        assertFalse(options.hasShortOption("input"));
        assertFalse(options.hasShortOption("test"));
    }
 
    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        Option option3 = new Option("o", "output", true, "output file");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        options.addOption(option1);
        options.addOptionGroup(optionGroup);
 
        OptionGroup retrievedOptionGroup = options.getOptionGroup(option2);
        assertEquals(optionGroup, retrievedOptionGroup);
 
        retrievedOptionGroup = options.getOptionGroup(option3);
        assertEquals(optionGroup, retrievedOptionGroup);
 
        retrievedOptionGroup = options.getOptionGroup(option1);
        assertEquals(null, retrievedOptionGroup);
    }
 
    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        options.addOption(option1);
        options.addOption(option2);
 
        String toStringResult = options.toString();
        assertTrue(toStringResult.contains("short {f=option"));
        assertTrue(toStringResult.contains("long {file=option"));
    }
}