import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = new OptionImpl(); // Replace OptionImpl with the actual implementation class
        commandLine = new WriteableCommandLineImpl(); // Replace WriteableCommandLineImpl with the actual implementation class
    }

    @Test
    public void process_ValidArguments_CommandLineContainsResults() {
        // Typical Use Case
        // Arrange
        ListIterator<String> args = List.of("-a", "value").listIterator();

        // Act
        option.process(commandLine, args);

        // Assert
        // Validate that the necessary results are stored in the CommandLine object
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getValue("a"));
    }

    @Test(expected = OptionException.class)
    public void process_InvalidArguments_ThrowsException() {
        // Error Scenario
        // Arrange
        ListIterator<String> args = List.of("-a").listIterator(); // Missing value for the option

        // Act
        option.process(commandLine, args);

        // Assert
        // Expects OptionException to be thrown
    }

    @Test
    public void defaults_NoDefaultsAdded_CommandLineUnchanged() {
        // Typical Use Case
        // Arrange
        // No defaults added to the option

        // Act
        option.defaults(commandLine);

        // Assert
        // Validate that the CommandLine object remains unchanged
        assertEquals(commandLine.getSize(), 0);
    }

    @Test
    public void canProcess_ValidArgument_ReturnsTrue() {
        // Typical Use Case
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-a";

        // Act
        boolean result = option.canProcess(commandLine, argument);

        // Assert
        assertTrue(result);
    }

    @Test
    public void canProcess_InvalidArgument_ReturnsFalse() {
        // Edge Case
        // Arrange
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "--invalid";

        // Act
        boolean result = option.canProcess(commandLine, argument);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getTriggers_NonEmptySet_ReturnsCorrectSet() {
        // Typical Use Case
        // Arrange
        Set<String> expectedTriggers = Set.of("-a", "--option");

        // Act
        Set<String> actualTriggers = option.getTriggers();

        // Assert
        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void getPrefixes_NonEmptySet_ReturnsCorrectSet() {
        // Typical Use Case
        // Arrange
        Set<String> expectedPrefixes = Set.of("-", "--");

        // Act
        Set<String> actualPrefixes = option.getPrefixes();

        // Assert
        assertEquals(expectedPrefixes, actualPrefixes);
    }

    @Test
    public void validate_ValidCommandLine_NoExceptionThrown() {
        // Typical Use Case
        // Arrange
        commandLine.addOption("a", "value");

        // Act
        option.validate(commandLine);

        // Assert
        // Expects no OptionException to be thrown
    }

    @Test(expected = OptionException.class)
    public void validate_InvalidCommandLine_ThrowsException() {
        // Error Scenario
        // Arrange
        // Missing required option in the commandLine

        // Act
        option.validate(commandLine);

        // Assert
        // Expects OptionException to be thrown
    }

    @Test
    public void helpLines_DepthZero_ReturnsNonEmptyList() {
        // Typical Use Case
        // Arrange
        int depth = 0;
        Set helpSettings = Set.of("helpSetting1", "helpSetting2");
        Comparator comp = (o1, o2) -> o1.getDescription().compareTo(o2.getDescription());

        // Act
        List helpLines = option.helpLines(depth, helpSettings, comp);

        // Assert
        assertNotNull(helpLines);
        assertFalse(helpLines.isEmpty());
    }

    @Test
    public void appendUsage_ValidArguments_ContainsOptionInformation() {
        // Typical Use Case
        // Arrange
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Set.of("helpSetting1", "helpSetting2");
        Comparator comp = (o1, o2) -> o1.getDescription().compareTo(o2.getDescription());

        // Act
        option.appendUsage(buffer, helpSettings, comp);

        // Assert
        // Validate that the buffer contains the expected option information
        String optionUsage = buffer.toString();
        assertTrue(optionUsage.contains(option.getPreferredName()));
        assertTrue(optionUsage.contains(option.getDescription()));
    }

    @Test
    public void getPreferredName_StringReturned_NotNull() {
        // Typical Use Case
        // Arrange

        // Act
        String preferredName = option.getPreferredName();

        // Assert
        assertNotNull(preferredName);
    }

    @Test
    public void getDescription_StringReturned_NotNull() {
        // Typical Use Case
        // Arrange

        // Act
        String description = option.getDescription();

        // Assert
        assertNotNull(description);
    }

    @Test
    public void getId_IntReturned_NotNegative() {
        // Typical Use Case
        // Arrange

        // Act
        int id = option.getId();

        // Assert
        assertTrue(id >= 0);
    }

    @Test
    public void findOption_FoundOption_ReturnsOption() {
        // Typical Use Case
        // Arrange
        String trigger = "-a";

        // Act
        Option foundOption = option.findOption(trigger);

        // Assert
        assertEquals(option, foundOption);
    }

    @Test
    public void findOption_OptionNotFound_ReturnsNull() {
        // Typical Use Case
        // Arrange
        String trigger = "--invalid";

        // Act
        Option foundOption = option.findOption(trigger);

        // Assert
        assertNull(foundOption);
    }

    @Test
    public void isRequired_RequiredOption_ReturnsTrue() {
        // Typical Use Case
        // Arrange
        boolean requiredOption = true;

        // Act
        boolean isRequired = option.isRequired();

        // Assert
        assertEquals(requiredOption, isRequired);
    }

    @Test
    public void isRequired_OptionNotRequired_ReturnsFalse() {
        // Typical Use Case
        // Arrange
        boolean requiredOption = false;

        // Act
        boolean isRequired = option.isRequired();

        // Assert
        assertEquals(requiredOption, isRequired);
    }

    @Test
    public void getParent_ParentOptionSet_ReturnsParentOption() {
        // Typical Use Case
        // Arrange
        Option parentOption = new OptionImpl(); // Replace OptionImpl with the actual implementation class
        option.setParent(parentOption);

        // Act
        Option actualParentOption = option.getParent();

        // Assert
        assertEquals(parentOption, actualParentOption);
    }

    @Test
    public void getParent_ParentOptionNotSet_ReturnsNull() {
        // Typical Use Case
        // Arrange

        // Act
        Option actualParentOption = option.getParent();

        // Assert
        assertNull(actualParentOption);
    }
}