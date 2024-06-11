import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Options options = new Options();

        // Act
        Options result = options.addOptionGroup(group);

        // Assert
        assertTrue(result.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Arrange
        String opt = "opt";
        String description = "description";
        Options options = new Options();

        // Act
        Options result = options.addOption(opt, description);

        // Assert
        assertTrue(result.hasOption(opt));
    }

    @Test
    public void testAddOptionWithHasArg() {
        // Arrange
        String opt = "opt";
        boolean hasArg = true;
        String description = "description";
        Options options = new Options();

        // Act
        Options result = options.addOption(opt, hasArg, description);

        // Assert
        assertTrue(result.hasOption(opt));
    }

    @Test
    public void testAddOptionWithOptAndLongOpt() {
        // Arrange
        String opt = "opt";
        String longOpt = "longOpt";
        boolean hasArg = true;
        String description = "description";
        Options options = new Options();

        // Act
        Options result = options.addOption(opt, longOpt, hasArg, description);

        // Assert
        assertTrue(result.hasOption(opt));
        assertTrue(result.hasOption(longOpt));
    }

    @Test
    public void testAddOptionObject() {
        // Arrange
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();

        // Act
        Options result = options.addOption(option);

        // Assert
        assertTrue(result.hasOption("opt"));
        assertTrue(result.hasOption("longOpt"));
    }

    @Test
    public void testGetOptions() {
        // Arrange
        Options options = new Options();

        // Act
        int size = options.getOptions().size();

        // Assert
        assertEquals(0, size);
    }

    @Test
    public void testGetRequiredOptions() {
        // Arrange
        Options options = new Options();

        // Act
        int size = options.getRequiredOptions().size();

        // Assert
        assertEquals(0, size);
    }

    @Test
    public void testGetOption() {
        // Arrange
        String opt = "opt";
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();
        options.addOption(option);

        // Act
        Option result = options.getOption(opt);

        // Assert
        assertNotNull(result);
        assertEquals("opt", result.getOpt());
        assertEquals("longOpt", result.getLongOpt());
        assertTrue(result.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        // Arrange
        String opt = "opt";
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();
        options.addOption(option);

        // Act
        int size = options.getMatchingOptions(opt).size();

        // Assert
        assertEquals(1, size);
    }

    @Test
    public void testHasOption() {
        // Arrange
        String opt = "opt";
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();
        options.addOption(option);

        // Act
        boolean result = options.hasOption(opt);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasLongOption() {
        // Arrange
        String opt = "longOpt";
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();
        options.addOption(option);

        // Act
        boolean result = options.hasLongOption(opt);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasShortOption() {
        // Arrange
        String opt = "opt";
        Option option = new Option("opt", "longOpt", true, "description");
        Options options = new Options();
        options.addOption(option);

        // Act
        boolean result = options.hasShortOption(opt);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetOptionGroup() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option option = new Option("opt", "longOpt", true, "description");
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);

        // Act
        OptionGroup result = options.getOptionGroup(option);

        // Assert
        assertEquals(group, result);
    }

    @Test
    public void testToString() {
        // Arrange
        Options options = new Options();

        // Act
        String result = options.toString();

        // Assert
        assertTrue(result.contains("[ Options: [ short "));
        assertTrue(result.contains(" ] [ long "));
    }
}