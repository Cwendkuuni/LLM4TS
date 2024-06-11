import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", true, "add option");
        Option option2 = new Option("r", "remove", true, "remove option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertTrue(options.hasOption("r"));
        assertTrue(options.hasOption("remove"));
        assertTrue(options.hasLongOption("add"));
        assertTrue(options.hasLongOption("remove"));
        assertTrue(options.getOptionGroup(option1) != null);
        assertTrue(options.getOptionGroup(option2) != null);
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", true, "option with argument");
        Option option2 = new Option("b", false, "option without argument");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertTrue(options.getRequiredOptions().isEmpty());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", true, "option with argument");
        Option option2 = new Option("b", false, "option without argument");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("option with argument"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        Option option1 = new Option("a", true, "option with argument");
        Option option2 = new Option("arg", false, "option without argument");
        Option option3 = new Option("argument", false, "option without argument");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("arg").size());
        assertEquals(1, options.getMatchingOptions("argument").size());
        assertEquals(0, options.getMatchingOptions("b").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", true, "option with argument");
        Option option2 = new Option("b", false, "option without argument");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option with argument"));
        assertTrue(options.hasOption("option without argument"));
        assertFalse(options.hasOption("c"));
    }
}