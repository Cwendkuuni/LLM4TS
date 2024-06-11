import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("o", "option", false, "Test option"));
        options.addOptionGroup(group);
        
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "Test option");
        
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        
        Option option = options.getOption("o");
        
        assertNotNull(option);
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
    }

    @Test
    public void testMatchingOptions() {
        Options options = new Options();
        options.addOption("one", "first", false, "First option");
        options.addOption("two", "second", false, "Second option");
        
        assertTrue(options.getMatchingOptions("one").contains("one"));
        assertTrue(options.getMatchingOptions("sec").contains("second"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("g", "group", false, "Test group option");
        group.addOption(option);
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("t", "test", false, "Test option");
        
        assertEquals("[ Options: [ short {t=Option{...}] [ long {test=t} ]", options.toString());
    }
}