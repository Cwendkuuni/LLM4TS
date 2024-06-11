import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.cli.*;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "opt1", false, "description");
        Option option2 = new Option("b", "opt2", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(2, options.getOptionGroups().size());
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "description");
        options.addOption("b", true, "description");
        
        assertEquals(2, options.getOptions().size());
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "opt1", false, "description");
        options.addOption(option);
        
        assertNotNull(options.getOption("a"));
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "opt1", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "opt1", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasLongOption("opt1"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "opt1", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasShortOption("a"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "opt1", false, "description");
        group.addOption(option);
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option));
    }
    
}