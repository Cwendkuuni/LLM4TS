import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Options options = new Options();
        options.addOptionGroup(group);
        
        Collection<OptionGroup> result = options.getOptionGroups();
        assertEquals(1, result.size());
        assertTrue(result.contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        assertTrue(result.contains(option));
    }
    
    @Test
    public void testAddOption_shortNameAndDescription() {
        String opt = "o";
        String description = "option description";
        
        Options options = new Options();
        options.addOption(opt, description);
        
        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        Option option = result.iterator().next();
        assertEquals(opt, option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals(description, option.getDescription());
    }
    
    @Test
    public void testAddOption_shortNameHasArgAndDescription() {
        String opt = "o";
        boolean hasArg = true;
        String description = "option description";
        
        Options options = new Options();
        options.addOption(opt, hasArg, description);
        
        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        Option option = result.iterator().next();
        assertEquals(opt, option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(hasArg, option.hasArg());
        assertEquals(description, option.getDescription());
    }
    
    @Test
    public void testAddOption_shortNameLongNameHasArgAndDescription() {
        String opt = "o";
        String longOpt = "option";
        boolean hasArg = true;
        String description = "option description";
        
        Options options = new Options();
        options.addOption(opt, longOpt, hasArg, description);
        
        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        Option option = result.iterator().next();
        assertEquals(opt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(hasArg, option.hasArg());
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        options.addOptionGroup(new OptionGroup().addOption(option));
        
        List<Object> result = options.getRequiredOptions();
        assertEquals(2, result.size());
        assertTrue(result.contains(option.getOpt()));
        assertTrue(result.contains(option.getLongOpt()));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        Option result = options.getOption("o");
        assertEquals(option, result);
        
        result = options.getOption("option");
        assertEquals(option, result);
        
        result = options.getOption("invalid");
        assertNull(result);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "option1", true, "option1 description");
        Option option2 = new Option("o2", "option2", true, "option2 description");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> result = options.getMatchingOptions("o");
        assertEquals(2, result.size());
        assertTrue(result.contains("o1"));
        assertTrue(result.contains("o2"));
        
        result = options.getMatchingOptions("option");
        assertEquals(2, result.size());
        assertTrue(result.contains("option1"));
        assertTrue(result.contains("option2"));
        
        result = options.getMatchingOptions("invalid");
        assertEquals(0, result.size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("o1", "option1", true, "option1 description");
        Option option2 = new Option("o2", "option2", true, "option2 description");
        OptionGroup group = new OptionGroup().addOption(option1).addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        OptionGroup result = options.getOptionGroup(option1);
        assertEquals(group, result);
        
        result = options.getOptionGroup(option2);
        assertEquals(group, result);
        
        result = options.getOptionGroup(new Option("invalid"));
        assertNull(result);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "option description");
        Options options = new Options();
        options.addOption(option);
        
        String result = options.toString();
        String expected = "[ Options: [ short {o=option} ] [ long {} ]";
        assertEquals(expected, result);
    }
}