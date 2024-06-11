import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", false, "input file");

        // Act
        commandLine.addOption(option);

        // Assert
        assertTrue(commandLine.hasOption("f"));
        assertFalse(commandLine.hasOption("n"));
    }

    @Test
    public void testAddValue() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("n", "name", true, "name of the person");
        String value = "John";

        // Act
        commandLine.addValue(option, value);

        // Assert
        assertTrue(commandLine.hasOption("n"));
        assertEquals("John", commandLine.getValue("n"));
    }

    @Test
    public void testGetValues() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", true, "input file");
        String value1 = "file1.txt";
        String value2 = "file2.txt";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        // Act
        List<String> values = commandLine.getValues("f");

        // Assert
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "output", true, "output file");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default.txt");

        // Act
        commandLine.setDefaultValues(option, defaultValues);

        // Assert
        assertTrue(commandLine.hasOption("o"));
        assertEquals(defaultValues, commandLine.getValues("o"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("v", "verbose", false, "enable verbose output");

        // Act
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("v", "verbose", false, "enable verbose output");

        // Act
        commandLine.setDefaultSwitch(option, true);

        // Assert
        assertTrue(commandLine.hasOption("v"));
        assertTrue(commandLine.getSwitch("v"));
    }

    @Test
    public void testAddProperty() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "outputDir";
        String value = "/path/to/dir/";

        // Act
        commandLine.addProperty(property, value);

        // Assert
        assertTrue(commandLine.hasProperty(property));
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Act & Assert
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("-file"));
        assertTrue(commandLine.looksLikeOption("--file"));
        assertFalse(commandLine.looksLikeOption("file"));
    }
}