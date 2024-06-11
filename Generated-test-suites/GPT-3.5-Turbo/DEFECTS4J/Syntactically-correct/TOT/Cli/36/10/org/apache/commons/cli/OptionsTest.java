import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
        assertFalse(options.hasOption("o3"));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");
        OptionGroup group1 = new OptionGroup();
        group1.addOption(option1);
        group1.addOption(option2);

        options.addOptionGroup(group1);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Option retrievedOption = options.getOption("o1");

        assertEquals(option1, retrievedOption);
        assertEquals("o1", retrievedOption.getKey());

        assertNull(options.getOption("o3"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("o");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("o1"));
        assertTrue(matchingOptions.contains("o2"));

        List<String> emptyList = options.getMatchingOptions("x");

        assertEquals(0, emptyList.size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
        assertFalse(options.hasOption("o3"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("o1"));
        assertTrue(options.hasLongOption("option1"));
        assertFalse(options.hasLongOption("test"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("o1"));
        assertTrue(options.hasShortOption("o2"));
        assertFalse(options.hasShortOption("test"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");
        OptionGroup group1 = new OptionGroup();
        group1.addOption(option1);
        group1.addOption(option2);

        options.addOptionGroup(group1);

        assertNotNull(options.getOptionGroup(option1));
        assertNull(options.getOptionGroup(option2));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Collection<Option> retrievedOptions = options.getOptions();

        assertEquals(2, retrievedOptions.size());
        assertTrue(retrievedOptions.contains(option1));
        assertTrue(retrievedOptions.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");
        OptionGroup group1 = new OptionGroup();
        group1.addOption(option1);
        group1.addOption(option2);

        options.addOptionGroup(group1);

        List<Object> retrievedRequiredOptions = options.getRequiredOptions();

        assertEquals(1, retrievedRequiredOptions.size());
        assertTrue(retrievedRequiredOptions.contains(group1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String stringRepresentation = options.toString();

        assertTrue(stringRepresentation.contains("[ short "));
        assertTrue(stringRepresentation.contains("[ long "));
    }
}