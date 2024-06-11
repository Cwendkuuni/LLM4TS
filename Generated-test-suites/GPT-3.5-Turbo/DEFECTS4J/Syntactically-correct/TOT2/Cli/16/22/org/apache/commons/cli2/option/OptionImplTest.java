import static org.junit.Assert.*;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testCanProcess_ReturnsTrueIfArgumentPresent() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = Collections.singletonList("argument").listIterator();

        // Act
        boolean result = option.canProcess(commandLine, arguments);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCanProcess_ReturnsFalseIfArgumentNotPresent() {
        // Arrange
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = Collections.emptyList().listIterator();

        // Act
        boolean result = option.canProcess(commandLine, arguments);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEquals_ReturnsTrueForEqualObjects() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        // Assert
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEquals_ReturnsFalseForDifferentObjects() {
        // Arrange
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, false);

        // Assert
        assertFalse(option1.equals(option2));
    }

    // Additional test cases for other public methods can be added here

}