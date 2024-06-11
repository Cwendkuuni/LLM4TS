import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Test
    public void addOption_ValidOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option = new Option("a", "add", true, "add option");

        // Act
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        // Assert
        assertTrue(options.contains(option));
    }

    @Test
    public void addOption_NullOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option = null;

        // Act
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        // Assert
        assertFalse(options.contains(option));
    }

    @Test
    public void getNames_OptionsExist_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option1 = new Option("l", "list", false, "list option");
        Option option2 = new Option("s", "search", true, "search option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        Collection<String> names = optionGroup.getNames();

        // Assert
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }

    @Test
    public void getNames_NoOptionsExist_EmptyCollection() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        Collection<String> names = optionGroup.getNames();

        // Assert
        assertTrue(names.isEmpty());
    }

    @Test
    public void getOptions_OptionsExist_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option1 = new Option("u", "update", false, "update option");
        Option option2 = new Option("d", "delete", true, "delete option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        Collection<Option> options = optionGroup.getOptions();

        // Assert
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void getOptions_NoOptionsExist_EmptyCollection() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        Collection<Option> options = optionGroup.getOptions();

        // Assert
        assertTrue(options.isEmpty());
    }

    @Test
    public void setSelected_ValidOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option = new Option("r", "read", false, "read option");
        optionGroup.addOption(option);

        // Act
        try {
            optionGroup.setSelected(option);
            String selected = optionGroup.getSelected();

            // Assert
            assertEquals(option.getKey(), selected);
        } catch (AlreadySelectedException e) {
            fail();
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void setSelected_OtherOptionSelected_Exception() throws AlreadySelectedException {
        // Arrange
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "option 1");
        Option option2 = new Option("o2", "option2", true, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        // Act
        optionGroup.setSelected(option2); // Exception expected
    }

    @Test
    public void setSelected_NullOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option");
        optionGroup.addOption(option);

        // Act
        try {
            optionGroup.setSelected(null);
            String selected = optionGroup.getSelected();

            // Assert
            assertNull(selected);
        } catch (AlreadySelectedException e) {
            fail();
        }
    }

    @Test
    public void getSelected_OptionSelected_Success() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);

        // Act
        String selected = optionGroup.getSelected();

        // Assert
        assertEquals(option.getKey(), selected);
    }

    @Test
    public void getSelected_NoOptionSelected_Null() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        String selected = optionGroup.getSelected();

        // Assert
        assertNull(selected);
    }

    @Test
    public void setRequired_RequiredOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        optionGroup.setRequired(true);

        // Assert
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void setRequired_NotRequiredOption_Success() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        optionGroup.setRequired(false);

        // Assert
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void toString_OptionsExist_StringifiedRepresentation() {
        // Arrange
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "option 1");
        Option option2 = new Option("o2", "option2", true, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Act
        String representation = optionGroup.toString();

        // Assert
        assertEquals("[ -o1, --option1 , -o2, --option2 ]", representation);
    }

    @Test
    public void toString_NoOptionsExist_EmptyString() {
        // Arrange
        optionGroup = new OptionGroup();

        // Act
        String representation = optionGroup.toString();

        // Assert
        assertEquals("[]", representation);
    }
}