import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("h", "help", false, "display help message");
        options.addOption(option);
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose mode");
        options.addOption(option);
        assertEquals(option, options.getOption("v"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "specify input file");
        options.addOption(option);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("r", "recursive", false, "enable recursive mode");
        Option option2 = new Option("d", "directory", true, "specify directory");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "output", true, "specify output file");
        Option option2 = new Option("l", "log", false, "enable logging");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        options.addOption("c", "clean", false, "clean up operation");
        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();
        options.addOption("o", "output", true, "specify output file");
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("o", "output", true, "specify output file");
        options.addOption("v", "verbose", false, "enable verbose mode");

        assertEquals(1, options.getMatchingOptions("o").size());
        assertEquals(1, options.getMatchingOptions("output").size());
        assertEquals(0, options.getMatchingOptions("x").size());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "specify input file");
        option.setRequired(true);
        options.addOption(option);

        assertTrue(options.getRequiredOptions().contains(option.getKey()));
    }
}