import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    @Test
    public void testCanProcess_ValidArguments_ReturnsTrue() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("-option").listIterator();

        // Act
        boolean result = option.canProcess(commandLine, arguments);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCanProcess_InvalidArguments_ReturnsFalse() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("-invalid").listIterator();

        // Act
        boolean result = option.canProcess(commandLine, arguments);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testToString_ReturnsExpectedString() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        String expectedString = "OptionImpl";

        // Act
        String result = option.toString();

        // Assert
        assertEquals(expectedString, result);
    }

    @Test
    public void testEquals_SameOption_ReturnsTrue() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        // Act
        boolean result = option1.equals(option2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentOption_ReturnsFalse() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, true);

        // Act
        boolean result = option1.equals(option2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHashCode_SameOption_ReturnsSameHashCode() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        // Act
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_DifferentOption_ReturnsDifferentHashCode() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, true);

        // Act
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testFindOption_FoundTrigger_ReturnsOption() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        option.addTrigger("trigger");

        // Act
        Option result = option.findOption("trigger");

        // Assert
        assertEquals(option, result);
    }

    @Test
    public void testFindOption_InvalidTrigger_ReturnsNull() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        option.addTrigger("trigger");

        // Act
        Option result = option.findOption("-invalid");

        // Assert
        assertNull(result);
    }

    @Test
    public void testIsRequired_True_ReturnsTrue() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);

        // Act
        boolean result = option.isRequired();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsRequired_False_ReturnsFalse() {
        // Arrange
        OptionImpl option = new OptionImpl(1, false);

        // Act
        boolean result = option.isRequired();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDefaults_NothingToDefault_DoesNothing() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Act
        option.defaults(commandLine);

        // Assert
        // No assertions, should not throw any exceptions
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_EmptyPrefixList_ThrowsException() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<>();

        // Act
        option.checkPrefixes(prefixes);

        // Assert
        // Expected IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_PrefixNotPresent_ThrowsException() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");

        // Act
        option.checkPrefixes(prefixes);

        // Assert
        // Expected IllegalArgumentException
    }
}