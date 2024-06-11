import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group and checking if it is in the list of option groups
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("v", "verbose", false, "Verbose mode"));
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddShortOption() {
        // Test adding a short option without an argument and checking if it is in the list of options
        Options options = new Options();
        options.addOption("h", "help");
        
        assertTrue(options.getOptions().stream().anyMatch(option -> option.getOpt().equals("h")));
    }

    @Test
    public void testAddLongOption() {
        // Test adding a long option with an argument and checking if it is in the list of options
        Options options = new Options();
        options.addOption("f", "file", true, "File path");
        
        assertTrue(options.getOptions().stream().anyMatch(option -> option.getLongOpt().equals("file")));
    }

    @Test
    public void testGetOptionByShortName() {
        // Test getting an option by short name and checking if it is the correct option
        Options options = new Options();
        Option expectedOption = new Option("q", "quiet", false, "Quiet mode");
        options.addOption(expectedOption);
        
        Option actualOption = options.getOption(expectedOption.getOpt());
        
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testGetOptionByLongName() {
        // Test getting an option by long name and checking if it is the correct option
        Options options = new Options();
        Option expectedOption = new Option("l", "log", true, "Log file path");
        options.addOption(expectedOption);
        
        Option actualOption = options.getOption(expectedOption.getLongOpt());
        
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists by both short and long names
        Options options = new Options();
        Option option = new Option("a", "all", false, "All items");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("all"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting the option group of an option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "output", true, "Output directory");
        group.addOption(option);
        options.addOptionGroup(group);
        
        OptionGroup actualGroup = options.getOptionGroup(option);
        
        assertEquals(group, actualGroup);
    }
}