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
    public void testHasOption_shortNameOptionExists_returnsTrue() {
        // Arrange
        commandLine.addOption(new Option("a"));

        // Act
        boolean result = commandLine.hasOption("a");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasOption_shortNameOptionNotExists_returnsFalse() {
        // Act
        boolean result = commandLine.hasOption("a");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasOption_longNameOptionExists_returnsTrue() {
        // Arrange
        commandLine.addOption(new Option("a", "alpha"));

        // Act
        boolean result = commandLine.hasOption("alpha");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasOption_longNameOptionNotExists_returnsFalse() {
        // Act
        boolean result = commandLine.hasOption("alpha");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetOptionObject_optionSet_returnsOptionValue() {
        // Arrange
        commandLine.addOption(new Option("a", "alpha"));
        commandLine.addOption(new Option("b", "beta"));
        commandLine.addArg("argument");

        // Act
        Object result = commandLine.getOptionObject("alpha");

        // Assert
        assertEquals("argument", result);
    }

    @Test
    public void testGetOptionObject_optionNotSet_returnsNull() {
        // Arrange
        commandLine.addOption(new Option("a", "alpha"));

        // Act
        Object result = commandLine.getOptionObject("alpha");

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValue_optionSet_returnsOptionValue() {
        // Arrange
        commandLine.addOption(new Option("a", "alpha"));
        commandLine.addArg("argument");

        // Act
        String result = commandLine.getOptionValue("alpha");

        // Assert
        assertEquals("argument", result);
    }

    @Test
    public void testGetOptionValue_optionNotSet_returnsNull() {
        // Arrange
        commandLine.addOption(new Option("a", "alpha"));

        // Act
        String result = commandLine.getOptionValue("alpha");

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValues_optionSet_returnsOptionValuesArray() {
        // Arrange
        commandLine.addOption(new Option("a"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        // Act
        String[] result = commandLine.getOptionValues("a");

        // Assert
        assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testGetOptionValues_optionNotSet_returnsNull() {
        // Arrange
        commandLine.addOption(new Option("a"));

        // Act
        String[] result = commandLine.getOptionValues("a");

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetArgs_argsAdded_returnsArrayWithAddedArgs() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        String[] result = commandLine.getArgs();

        // Assert
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testGetArgList_argsAdded_returnsListWithAddedArgs() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        List<String> result = commandLine.getArgList();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("arg1"));
        assertTrue(result.contains("arg2"));
    }
}