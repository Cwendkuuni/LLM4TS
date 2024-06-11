import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    // test addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "add option");
        group.addOption(option);
        options.addOptionGroup(group);
        OptionGroup resultGroup = options.getOptionGroups().iterator().next();
        Option resultOption = resultGroup.getOptions().get(0);

        assertEquals(option, resultOption);
        assertEquals(group, options.getOptionGroup(resultOption));
    }

    // test addOption method with only short name
    @Test
    public void testAddOptionShortName() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    // test addOption method with short name and description
    @Test
    public void testAddOptionShortNameAndDescription() {
        Options options = new Options();
        options.addOption("a", "add", false, "add option");
        
        assertTrue(options.hasOption("a"));
    }

    // test addOption method with short name, long name, and description
    @Test
    public void testAddOptionShortLongNameAndDescription() {
        Options options = new Options();
        options.addOption("a", "add", false, "add option");

        assertTrue(options.hasOption("a"));
    }

    // test getOption method with valid option
    @Test
    public void testGetOptionValid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    // test getOption method with invalid option
    @Test
    public void testGetOptionInvalid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);
        
        assertNull(options.getOption("c"));
    }

    // test getMatchingOptions method with partial name
    @Test
    public void testGetMatchingOptionsPartialName() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "browse", false, "browse option");
        options.addOption(option1);
        options.addOption(option2);

        List<String> result = options.getMatchingOptions("b");
        assertTrue(result.contains("browse"));
    }

    // test hasOption method with valid option
    @Test
    public void testHasOptionValid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    // test hasOption method with invalid option
    @Test
    public void testHasOptionInvalid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);
        
        assertFalse(options.hasOption("c"));
    }

    // test hasLongOption method with valid option
    @Test
    public void testHasLongOptionValid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);

        assertTrue(options.hasLongOption("add"));
    }

    // test hasLongOption method with invalid option
    @Test
    public void testHasLongOptionInvalid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);
        
        assertFalse(options.hasLongOption("browse"));
    }

    // test hasShortOption method with valid option
    @Test
    public void testHasShortOptionValid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    // test hasShortOption method with invalid option
    @Test
    public void testHasShortOptionInvalid() {
        Options options = new Options();
        Option option = new Option("a", "add", false, "add option");
        options.addOption(option);
        
        assertFalse(options.hasShortOption("b"));
    }

    // test getOptionGroup method with valid option
    @Test
    public void testGetOptionGroupValid() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "browse", false, "browse option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
    }

    // test getOptionGroup method with invalid option
    @Test
    public void testGetOptionGroupInvalid() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "browse", false, "browse option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertNull(options.getOptionGroup(option2));
    }

    // test toString method
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "browse", false, "browse option");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option: a(add) [ARG : None], b=Option: b(browse) [ARG : None]} ] [ long {add=Option: a(add) [ARG : None], browse=Option: b(browse) [ARG : None]} ]";
        assertEquals(expected, options.toString());
    }

}