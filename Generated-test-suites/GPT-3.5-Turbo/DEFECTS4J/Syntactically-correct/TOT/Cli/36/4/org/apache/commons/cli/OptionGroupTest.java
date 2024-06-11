import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Map<String, Option> optionMap;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        optionMap = new HashMap<>();
    }

    @Test
    public void addOption_ShouldAddOptionToOptionGroup() {
        // Arrange
        Option option = new Option("o", "option", true, "An option");

        // Act
        OptionGroup addedOptionGroup = optionGroup.addOption(option);
        optionMap.put(option.getKey(), option);

        // Assert
        assertEquals(optionMap, addedOptionGroup.getOptions());
    }

    @Test
    public void getNames_ShouldReturnOptionNames() {
        // Arrange
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "option2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        Collection<String> names = optionGroup.getNames();

        // Assert
        assertTrue(names.contains("option1"));
        assertTrue(names.contains("option2"));
    }

    @Test
    public void getOptions_ShouldReturnOptions() {
        // Arrange
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "option2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        Collection<Option> options = optionGroup.getOptions();

        // Assert
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test(expected = AlreadySelectedException.class)
    public void setSelected_SameOptionAlreadySelected_ShouldThrowAlreadySelectedException() throws AlreadySelectedException {
        // Arrange
        Option option = new Option("o", "option", true, "An option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);

        // Act & Assert
        optionGroup.setSelected(option);
    }

    @Test
    public void setSelected_DifferentOptionAlreadySelected_ShouldNotThrowAlreadySelectedException() {
        // Arrange
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "option2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        // Act & Assert
        assertDoesNotThrow(() -> optionGroup.setSelected(option2));
    }

    @Test
    public void getSelected_InitiallyNoOptionSelected_ShouldReturnNull() {
        // Arrange & Act
        String selectedOption = optionGroup.getSelected();

        // Assert
        assertNull(selectedOption);
    }

    @Test
    public void getSelected_OptionSelected_ShouldReturnSelectedOption() throws AlreadySelectedException {
        // Arrange
        Option option = new Option("o", "option", true, "An option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);

        // Act
        String selectedOption = optionGroup.getSelected();

        // Assert
        assertEquals("option", selectedOption);
    }

    @Test
    public void setRequired_ShouldSetRequired() {
        // Arrange & Act
        optionGroup.setRequired(true);

        // Assert
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void toString_ShouldReturnStringifiedOptionGroup() {
        // Arrange
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "option2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        String stringifiedOptionGroup = optionGroup.toString();

        // Assert
        assertEquals("[option1 Option 1, option2 Option 2]", stringifiedOptionGroup);
    }
}