import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_withValidShortOption_shouldReturnTrue() {
        commandLine.addOption(new Option("a", "apple", false, "Apple option"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_withValidLongOption_shouldReturnTrue() {
        commandLine.addOption(new Option("a", "apple", false, "Apple option"));
        assertTrue(commandLine.hasOption("apple"));
    }

    @Test
    public void testHasOption_withInvalidOption_shouldReturnFalse() {
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOption_withValidShortOption_shouldReturnFalse() {
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testGetOptionObject_withValidOption_shouldReturnObject() {
        Option option = new Option("c", "car", true, "Car option");
        option.setType(String.class);
        option.addValue("bmw");
        commandLine.addOption(option);
        
        assertEquals("bmw", commandLine.getOptionObject("c"));
    }

    @Test
    public void testGetOptionObject_withInvalidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionObject("d"));
    }

    @Test
    public void testGetOptionObject_withValidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionObject("c"));
    }

    @Test
    public void testGetOptionValue_withValidOption_shouldReturnValue() {
        Option option = new Option("e", "elephant", true, "Elephant option");
        option.addValue("gray");
        commandLine.addOption(option);
        
        assertEquals("gray", commandLine.getOptionValue("e"));
    }

    @Test
    public void testGetOptionValue_withInvalidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValue_withValidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionValue("e"));
    }

    @Test
    public void testGetOptionValues_withValidOption_shouldReturnValues() {
        Option option = new Option("g", "giraffe", true, "Giraffe option");
        option.addValue("male");
        option.addValue("female");
        commandLine.addOption(option);
        
        assertArrayEquals(new String[]{"male", "female"}, commandLine.getOptionValues("g"));
    }

    @Test
    public void testGetOptionValues_withInvalidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionValues("h"));
    }

    @Test
    public void testGetOptionValues_withValidOption_shouldReturnNull() {
        assertNull(commandLine.getOptionValues("g"));
    }

    @Test
    public void testGetOptionValue_withDefaultValue_whenOptionNotSet_shouldReturnDefaultValue() {
        assertEquals("default", commandLine.getOptionValue("i", "default"));
    }

    @Test
    public void testGetOptionValue_withDefaultValue_whenOptionSet_shouldReturnValue() {
        Option option = new Option("j", "john", true, "John option");
        option.addValue("doe");
        commandLine.addOption(option);
        
        assertEquals("doe", commandLine.getOptionValue("j", "default"));
    }

    @Test
    public void testGetOptionValue_withDefaultValue_whenOptionNotSet_shouldReturnDefaultValue() {
        assertEquals("default", commandLine.getOptionValue("j", "default"));
    }

    @Test
    public void testGetArgs_withAddedArgs_shouldReturnArrayOfArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList_withAddedArgs_shouldReturnListOfArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertEquals(java.util.Arrays.asList("arg1", "arg2"), commandLine.getArgList());
    }

    @Test
    public void testIterator_withAddedOptions_shouldReturnIteratorOfOptions() {
        Option option1 = new Option("k", "kangaroo", false, "Kangaroo option");
        Option option2 = new Option("l", "lion", false, "Lion option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        java.util.Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions_withAddedOptions_shouldReturnArrayOfOptions() {
        Option option1 = new Option("m", "monkey", false, "Monkey option");
        Option option2 = new Option("n", "narwhal", false, "Narwhal option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertArrayEquals(new Option[]{option1, option2}, commandLine.getOptions());
    }
}