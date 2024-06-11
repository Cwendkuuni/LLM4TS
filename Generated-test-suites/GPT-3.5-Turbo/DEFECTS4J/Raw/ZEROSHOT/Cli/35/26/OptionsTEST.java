import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void addOptionGroup_shouldAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Group.addOption(new Option("a", "apple", false, "apple option"));
        Group.addOption(new Option("b", "banana", false, "banana option"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void addOption_shouldAddOption() {
        Option option = new Option("a", "apple", false, "apple option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void getOption_shouldReturnOptionWithGivenName() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        Option result = options.getOption("a");

        assertEquals(option, result);
    }

    @Test
    public void getMatchingOptions_shouldReturnMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        options.addOption(option1);
        options.addOption(option2);

        List<String> result = options.getMatchingOptions("a");

        assertEquals(1, result.size());
        assertEquals("apple", result.get(0));
    }

    @Test
    public void hasOption_shouldReturnTrueIfOptionExists() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        boolean result = options.hasOption("a");

        assertTrue(result);
    }

    @Test
    public void hasOption_shouldReturnFalseIfOptionDoesNotExist() {
        Option option = new Option("b", "banana", false, "banana option");
        options.addOption(option);

        boolean result = options.hasOption("a");

        assertFalse(result);
    }

    @Test
    public void hasLongOption_shouldReturnTrueIfLongOptionExists() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        boolean result = options.hasLongOption("apple");

        assertTrue(result);
    }

    @Test
    public void hasLongOption_shouldReturnFalseIfLongOptionDoesNotExist() {
        Option option = new Option("b", "banana", false, "banana option");
        options.addOption(option);

        boolean result = options.hasLongOption("apple");

        assertFalse(result);
    }

    @Test
    public void hasShortOption_shouldReturnTrueIfShortOptionExists() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        boolean result = options.hasShortOption("a");

        assertTrue(result);
    }

    @Test
    public void hasShortOption_shouldReturnFalseIfShortOptionDoesNotExist() {
        Option option = new Option("b", "banana", false, "banana option");
        options.addOption(option);

        boolean result = options.hasShortOption("a");

        assertFalse(result);
    }

    @Test
    public void getOptionGroup_shouldReturnOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "apple", false, "apple option"));
        group.addOption(new Option("b", "banana", false, "banana option"));
        options.addOptionGroup(group);

        OptionGroup result = options.getOptionGroup(new Option("a", "apple", false, "apple option"));

        assertEquals(group, result);
    }

    @Test
    public void toString_shouldReturnStringRepresentation() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        String result = options.toString();

        assertEquals("[ Options: [ short {a=Option[ opt: a longOpt: apple hasArg: false description: apple option ]} ] [ long {apple=Option[ opt: a longOpt: apple hasArg: false description: apple option ]} ]", result);
    }
}