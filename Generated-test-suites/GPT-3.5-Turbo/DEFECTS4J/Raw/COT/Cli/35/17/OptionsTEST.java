import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "aa", true, "Description 1");
        Option option2 = new Option("b", "bb", true, "Description 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "aa", true, "Description");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "aa", true, "Description 1");
        Option option2 = new Option("b", "bb", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        
        Collection<Option> optionList = options.getOptions();
        
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "aa", true, "Description 1");
        Option option2 = new Option("b", "bb", true, "Description 2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        
        List<Option> requiredOptionsList = options.getRequiredOptions();
        
        assertEquals(1, requiredOptionsList.size());
        assertTrue(requiredOptionsList.contains(option1));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "aa", true, "Description");
        options.addOption(option);
        
        Option result = options.getOption("a");
        
        assertEquals(option, result);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "aa", true, "Description 1");
        Option option2 = new Option("b", "bc", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> matchingOptions = options.getMatchingOptions("a");
        
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("aa"));
        assertTrue(matchingOptions.contains("bc"));
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "aa", true, "Description");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "aa", true, "Description");
        options.addOption(option);
        
        assertTrue(options.hasLongOption("aa"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "aa", true, "Description");
        options.addOption(option);
        
        assertTrue(options.hasShortOption("a"));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "aa", true, "Description 1");
        Option option2 = new Option("b", "bb", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        
        String result = options.toString();
        
        String expected = "[ Options: [ short {a=Option: a; long-opts: aa; has-arg: true, description: Description 1}, {b=Option: b; long-opts: bb; has-arg: true, description: Description 2} ] [ long {aa=Option: a; long-opts: aa; has-arg: true, description: Description 1}, {bb=Option: b; long-opts: bb; has-arg: true, description: Description 2} ]]";
        assertEquals(expected, result);
    }
}