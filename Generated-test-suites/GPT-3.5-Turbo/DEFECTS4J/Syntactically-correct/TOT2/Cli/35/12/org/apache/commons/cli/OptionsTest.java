import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Print extra information");
        options.addOption(option);
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("verbose"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "directory", true, "Specify a directory");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("file"));
        assertTrue(options.hasOption("directory"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Specify output option");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option));
    }

    // Add more test cases for other methods in the Options class

}