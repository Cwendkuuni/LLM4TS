import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void hasOption_whenOptionSet_returnsTrue() {
        // Arrange
        Option option = new Option("v", "version", false, "Print version information");
        commandLine.addOption(option);

        // Act
        boolean result = commandLine.hasOption("v");

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasOption_whenOptionNotSet_returnsFalse() {
        // Arrange

        // Act
        boolean result = commandLine.hasOption("v");

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasOption_whenOptionSetWithShortName_returnsTrue() {
        // Arrange
        Option option = new Option("v", "version", false, "Print version information");
        commandLine.addOption(option);

        // Act
        boolean result = commandLine.hasOption('v');

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasOption_whenOptionNotSetWithShortName_returnsFalse() {
        // Arrange

        // Act
        boolean result = commandLine.hasOption('v');

        // Assert
        assertFalse(result);
    }

    @Test
    public void getOptionValue_whenOptionSet_returnsValue() {
        // Arrange
        Option option = new Option("l", "language", true, "Specify programming language");
        option.setValue("Java");
        commandLine.addOption(option);

        // Act
        String result = commandLine.getOptionValue("l");

        // Assert
        assertEquals("Java", result);
    }

    @Test
    public void getOptionValue_whenOptionNotSet_returnsNull() {
        // Arrange

        // Act
        String result = commandLine.getOptionValue("l");

        // Assert
        assertNull(result);
    }

    @Test
    public void getOptionValue_whenOptionSetWithShortName_returnsValue() {
        // Arrange
        Option option = new Option("l", "language", true, "Specify programming language");
        option.setValue("Java");
        commandLine.addOption(option);

        // Act
        String result = commandLine.getOptionValue('l');

        // Assert
        assertEquals("Java", result);
    }

    @Test
    public void getOptionValue_whenOptionNotSetWithShortName_returnsNull() {
        // Arrange

        // Act
        String result = commandLine.getOptionValue('l');

        // Assert
        assertNull(result);
    }

    @Test
    public void getOptionValues_whenOptionSetWithMultipleValues_returnsValuesArray() {
        // Arrange
        Option option = new Option("f", "files", true, "Specify file paths");
        option.setValues(new String[]{"file1.txt", "file2.txt"});
        commandLine.addOption(option);

        // Act
        String[] result = commandLine.getOptionValues("f");

        // Assert
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, result);
    }

    @Test
    public void getOptionValues_whenOptionNotSet_returnsNull() {
        // Arrange

        // Act
        String[] result = commandLine.getOptionValues("f");

        // Assert
        assertNull(result);
    }

    @Test
    public void getOptionValues_whenOptionSetWithShortNameAndMultipleValues_returnsValuesArray() {
        // Arrange
        Option option = new Option("f", "files", true, "Specify file paths");
        option.setValues(new String[]{"file1.txt", "file2.txt"});
        commandLine.addOption(option);

        // Act
        String[] result = commandLine.getOptionValues('f');

        // Assert
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, result);
    }

    @Test
    public void getOptionValues_whenOptionNotSetWithShortName_returnsNull() {
        // Arrange

        // Act
        String[] result = commandLine.getOptionValues('f');

        // Assert
        assertNull(result);
    }

    @Test
    public void getOptionValue_withDefaultValue_whenOptionNotSet_returnsDefaultValue() {
        // Arrange

        // Act
        String result = commandLine.getOptionValue("f", "default.txt");

        // Assert
        assertEquals("default.txt", result);
    }

    @Test
    public void getOptionValue_withDefaultValue_whenOptionSet_returnsValue() {
        // Arrange
        Option option = new Option("f", "file", true, "Specify file path");
        option.setValue("file.txt");
        commandLine.addOption(option);

        // Act
        String result = commandLine.getOptionValue("f", "default.txt");

        // Assert
        assertEquals("file.txt", result);
    }

    @Test
    public void getOptionValue_withDefaultValue_whenOptionNotSetWithShortName_returnsDefaultValue() {
        // Arrange

        // Act
        String result = commandLine.getOptionValue('f', "default.txt");

        // Assert
        assertEquals("default.txt", result);
    }

    @Test
    public void getOptionValue_withDefaultValue_whenOptionSetWithShortName_returnsValue() {
        // Arrange
        Option option = new Option("f", "file", true, "Specify file path");
        option.setValue("file.txt");
        commandLine.addOption(option);

        // Act
        String result = commandLine.getOptionValue('f', "default.txt");

        // Assert
        assertEquals("file.txt", result);
    }

    @Test
    public void getArgs_whenNoArgsPassed_returnsEmptyArray() {
        // Arrange

        // Act
        String[] result = commandLine.getArgs();

        // Assert
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void getArgs_whenArgsPassed_returnsArgsArray() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        String[] result = commandLine.getArgs();

        // Assert
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void getArgList_whenNoArgsPassed_returnsEmptyList() {
        // Arrange

        // Act
        List result = commandLine.getArgList();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getArgList_whenArgsPassed_returnsArgsList() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        List result = commandLine.getArgList();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0));
        assertEquals("arg2", result.get(1));
    }

    @Test
    public void addArg_whenArgAdded_getArgsReturnsAddedArg() {
        // Arrange

        // Act
        commandLine.addArg("arg1");
        String[] result = commandLine.getArgs();

        // Assert
        assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void addOption_whenOptionAdded_getOptionsReturnsAddedOption() {
        // Arrange
        Option option = new Option("f", "file", true, "Specify file path");

        // Act
        commandLine.addOption(option);
        Option[] result = commandLine.getOptions();

        // Assert
        assertArrayEquals(new Option[]{option}, result);
    }

}