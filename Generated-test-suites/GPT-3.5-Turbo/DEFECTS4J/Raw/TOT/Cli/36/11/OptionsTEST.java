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
    public void testAddOption_NoArgument_Success() {
        // Arrange
        String opt = "p";
        String description = "Print option";
        
        // Act
        options.addOption(opt, description);

        // Assert
        assertTrue(options.hasOption(opt));
        assertFalse(options.getOption(opt).hasArg());
        assertEquals(description, options.getOption(opt).getDescription());
    }

    @Test
    public void testAddOption_WithArgument_Success() {
        // Arrange
        String opt = "l";
        boolean hasArg = true;
        String description = "Log option";

        // Act
        options.addOption(opt, hasArg, description);

        // Assert
        assertTrue(options.hasOption(opt));
        assertTrue(options.getOption(opt).hasArg());
        assertEquals(description, options.getOption(opt).getDescription());
    }

    @Test
    public void testAddOption_WithShortAndLongName_Success() {
        // Arrange
        String opt = "c";
        String longOpt = "config";
        boolean hasArg = true;
        String description = "Config option";

        // Act
        options.addOption(opt, longOpt, hasArg, description);

        // Assert
        assertTrue(options.hasOption(opt));
        assertTrue(options.hasLongOption(longOpt));
        assertTrue(options.getOption(opt).hasArg());
        assertEquals(description, options.getOption(opt).getDescription());
        assertEquals(description, options.getOption(longOpt).getDescription());
    }

    @Test
    public void testAddOptionGroup_GroupIsRequired_ShouldAddOptionsToRequiredList() {
        // Arrange
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("v", "version", false, "Version option");
        group.addOption(option1);
        group.addOption(option2);

        // Act
        options.addOptionGroup(group);

        // Assert
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("h"));
        assertTrue(options.getRequiredOptions().contains("v"));
    }

    @Test
    public void testGetOption_ExistingOption_ShouldReturnOption() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        Option result = options.getOption("o");

        // Assert
        assertNotNull(result);
        assertEquals(option, result);
        assertEquals(option.getDescription(), result.getDescription());
    }

    @Test
    public void testGetOption_NonExistingOption_ShouldReturnNull() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        Option result = options.getOption("i");

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMatchingOptions_PartialName_ShouldReturnMatchingOptions() {
        // Arrange
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("o", "output", false, "Output option");
        options.addOption(option1);
        options.addOption(option2);

        // Act
        List<String> result = options.getMatchingOptions("o");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("output"));
    }

    @Test
    public void testGetMatchingOptions_NoMatchingOptions_ShouldReturnEmptyList() {
        // Arrange
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("o", "output", false, "Output option");
        options.addOption(option1);
        options.addOption(option2);

        // Act
        List<String> result = options.getMatchingOptions("i");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHasOption_ExistingOption_ShouldReturnTrue() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasOption("o");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasOption_NonExistingOption_ShouldReturnFalse() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasOption("i");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasLongOption_ExistingOption_ShouldReturnTrue() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasLongOption("output");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasLongOption_NonExistingOption_ShouldReturnFalse() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasLongOption("input");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasShortOption_ExistingOption_ShouldReturnTrue() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasShortOption("o");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasShortOption_NonExistingOption_ShouldReturnFalse() {
        // Arrange
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);

        // Act
        boolean result = options.hasShortOption("i");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetOptionGroup_OptionBelongsToGroup_ShouldReturnGroup() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "help", false, "Help option");
        group.addOption(option);
        options.addOptionGroup(group);

        // Act
        OptionGroup result = options.getOptionGroup(option);

        // Arrange
        assertEquals(group, result);
    }

    @Test
    public void testGetOptionGroup_OptionDoesNotBelongsToGroup_ShouldReturnNull() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "help", false, "Help option");
        group.addOption(option);
        options.addOptionGroup(group);

        // Act
        OptionGroup result = options.getOptionGroup(new Option("o", "output", true, "Output option"));

        // Arrange
        assertNull(result);
    }

    @Test
    public void testToString_NoOptions_ShouldReturnEmptyString() {
        // Arrange

        // Act
        String result = options.toString();

        // Assert
        assertEquals("[ Options: [ short {} ] [ long {} ]", result);
    }

    @Test
    public void testToString_WithOptions_ShouldReturnOptionsString() {
        // Arrange
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("o", "output", true, "Output option");
        options.addOption(option1);
        options.addOption(option2);

        // Act
        String result = options.toString();

        // Assert
        assertEquals("[ Options: [ short {h:help, o:output} ] [ long {help, output} ]", result);
    }
}