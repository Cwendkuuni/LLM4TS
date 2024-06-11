import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    // Test case for addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.setRequired(true);
        optionGroup1.addOption(new Option("a", "option a", false, "description a"));
        optionGroup1.addOption(new Option("b", "option b", false, "description b"));
        options.addOptionGroup(optionGroup1);

        assertTrue(options.getRequiredOptions().contains(optionGroup1));

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        optionGroup2.addOption(new Option("c", "option c", false, "description c"));
        optionGroup2.addOption(new Option("d", "option d", false, "description d"));
        options.addOptionGroup(optionGroup2);

        assertFalse(options.getRequiredOptions().contains(optionGroup2));
    }

    // Test case for addOption method (single-character name)
    @Test
    public void testAddOptionSingleCharacter() {
        Options options = new Options();
        Option option1 = new Option("a", "description");
        options.addOption(option1);

        assertTrue(options.getOptions().contains(option1));

        Option option2 = new Option("b", "description");
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option2));
    }

    // Test case for addOption method (single-character name with argument)
    @Test
    public void testAddOptionSingleCharacterWithArg() {
        Options options = new Options();
        Option option1 = new Option("a", true, "description");
        options.addOption(option1);

        assertTrue(options.getOptions().contains(option1));

        Option option2 = new Option("b", true, "description");
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option2));
    }

    // Test case for addOption method (single-character name and long name)
    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        Option option1 = new Option("a", "longA", false, "description");
        options.addOption(option1);

        assertTrue(options.getOptions().contains(option1));

        Option option2 = new Option("b", "longB", false, "description");
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option2));
    }

    // Test case for getOption method
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "description");
        options.addOption(option1);

        assertEquals(option1, options.getOption("a"));

        Option option2 = new Option("b", "description");
        options.addOption(option2);

        assertEquals(option2, options.getOption("b"));
    }

    // Test case for getMatchingOptions method
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "longA", false, "description a");
        options.addOption(option1);

        assertTrue(options.getMatchingOptions("a").contains("longA"));

        Option option2 = new Option("b", "longB", false, "description b");
        options.addOption(option2);

        assertTrue(options.getMatchingOptions("b").contains("longB"));
    }

    // Test case for hasOption method
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "description");
        options.addOption(option1);

        assertTrue(options.hasOption("a"));

        Option option2 = new Option("b", "description");
        options.addOption(option2);

        assertTrue(options.hasOption("b"));
    }

    // Test case for hasLongOption method
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "description");
        options.addOption(option1);

        assertTrue(options.hasLongOption("a"));

        Option option2 = new Option("b", "description");
        options.addOption(option2);

        assertTrue(options.hasLongOption("b"));
    }

    // Test case for hasShortOption method
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "description");
        options.addOption(option1);

        assertTrue(options.hasShortOption("a"));

        Option option2 = new Option("b", "description");
        options.addOption(option2);

        assertTrue(options.hasShortOption("b"));
    }

    // Test case for getOptionGroup method
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.setRequired(true);
        Option option1 = new Option("a", "option a", false, "description a");
        optionGroup1.addOption(option1);
        Option option2 = new Option("b", "option b", false, "description b");
        optionGroup1.addOption(option2);
        options.addOptionGroup(optionGroup1);

        assertEquals(optionGroup1, options.getOptionGroup(option1));
        assertEquals(optionGroup1, options.getOptionGroup(option2));
        
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        Option option3 = new Option("c", "option c", false, "description c");
        optionGroup2.addOption(option3);
        Option option4 = new Option("d", "option d", false, "description d");
        optionGroup2.addOption(option4);
        options.addOptionGroup(optionGroup2);

        assertNull(options.getOptionGroup(option3));
        assertNull(options.getOptionGroup(option4));
    }

}