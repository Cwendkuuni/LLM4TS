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
    public void testHasOption() {
        // Test 1: Ensure that the hasOption method returns true when an option is set
        // Typical Use Case
        Option option = new Option("a", "apple", false, "Apple option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("a"));

        // Test 2: Ensure that the hasOption method returns false when an option is not set
        // Typical Use Case
        assertFalse(commandLine.hasOption("b"));

        // Test 3: Ensure that the hasOption method returns true when an option is set
        // Edge Case: option as a character
        Option option1 = new Option("c", "cat", false, "Cat option");
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption('c'));

        // Test 4: Ensure that the hasOption method returns false when an option is not set
        // Edge Case: option as a character
        assertFalse(commandLine.hasOption('d'));

        // Test 5: Ensure that the hasOption method returns true when an option is set
        // Error Scenario: Option with special characters
        Option option2 = new Option("e", "exclamation!", false, "Exclamation option");
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption("e"));

        // Test 6: Ensure that the hasOption method returns false when an option is not set
        // Error Scenario: Option with special characters
        assertFalse(commandLine.hasOption("f@#$%"));
    }

    @Test
    public void testGetOptionValue() {
        // Test 1: Ensure that the getOptionValue method returns the correct value for a set option
        // Typical Use Case
        Option option = new Option("a", "apple", true, "Apple option");
        option.setArgName("value");
        option.setValueSeparator(':');
        commandLine.addOption(option);
        String[] args = {"-a", "10:20:30"};
        commandLine.parse(args);
        assertEquals("10", commandLine.getOptionValue("a"));

        // Test 2: Ensure that the getOptionValue method returns null for a non-existing option
        // Typical Use Case
        assertNull(commandLine.getOptionValue("b"));

        // Test 3: Ensure that the getOptionValue method returns the correct value for a set option
        // Edge Case: option as a character
        Option option1 = new Option("c", "cat", true, "Cat option");
        option1.setArgName("value");
        option1.setValueSeparator(',');
        commandLine.addOption(option1);
        String[] args1 = {"-c", "10,20,30"};
        commandLine.parse(args1);
        assertEquals("10", commandLine.getOptionValue('c'));

        // Test 4: Ensure that the getOptionValue method returns null for a non-existing option
        // Edge Case: option as a character
        assertNull(commandLine.getOptionValue('d'));

        // Test 5: Ensure that the getOptionValue method returns the correct value for a set option
        // Error Scenario: Option with special characters
        Option option2 = new Option("e", "exclamation!", true, "Exclamation option");
        option2.setArgName("value");
        option2.setValueSeparator(':');
        commandLine.addOption(option2);
        String[] args2 = {"-e", "10:20:30"};
        commandLine.parse(args2);
        assertEquals("10", commandLine.getOptionValue("e"));

        // Test 6: Ensure that the getOptionValue method returns null for a non-existing option
        // Error Scenario: Option with special characters
        assertNull(commandLine.getOptionValue("f@#$%"));
    }

    // Additional test methods for other methods in CommandLine class can be added here

}