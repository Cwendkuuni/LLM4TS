import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionGroupTest {

    @Mock
    private Option optionMock;
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void addOption_ValidOption_OptionAddedToGroup() {
        optionGroup.addOption(optionMock);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(optionMock));
    }

    @Test
    public void getNames_NoOptions_ReturnsEmptyCollection() {
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.isEmpty());
    }

    @Test
    public void getNames_WithOptions_ReturnsCollectionWithOptionNames() {
        Option option1 = new Option("o1", "opt1", false, null);
        Option option2 = new Option("o2", "opt2", false, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }

    @Test
    public void getOptions_NoOptions_ReturnsEmptyCollection() {
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.isEmpty());
    }

    @Test
    public void getOptions_WithOptions_ReturnsCollectionWithOptions() {
        optionGroup.addOption(optionMock);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(optionMock));
    }

    @Test
    public void setSelected_NullOption_SelectedIsNull() throws AlreadySelectedException {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void setSelected_NewOption_SelectedIsSet() throws AlreadySelectedException {
        Option option1 = new Option("o1", "opt1", false, null);
        Option option2 = new Option("o2", "opt2", false, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void setSelected_OptionAlreadySelected_AlreadySelectedExceptionThrown() throws AlreadySelectedException {
        Option option1 = new Option("o1", "opt1", false, null);
        Option option2 = new Option("o2", "opt2", false, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        
        // Exception is expected to be thrown in the previous line
    }

    @Test
    public void getSelected_NoOptionSelected_ReturnsNull() {
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void getSelected_OptionSelected_ReturnsSelectedOptionName() throws AlreadySelectedException {
        Option option1 = new Option("o1", "opt1", false, null);
        Option option2 = new Option("o2", "opt2", false, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);
        
        assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @Test
    public void setRequired_RequiredSetToTrue_IsRequiredReturnsTrue() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void isRequired_DefaultValue_ReturnsFalse() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void setRequired_RequiredSetToFalse_IsRequiredReturnsFalse() {
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void toString_NoOptions_ReturnsEmptyString() {
        String expectedString = "[]";
        assertEquals(expectedString, optionGroup.toString());
    }

    @Test
    public void toString_WithOptions_ReturnsFormattedString() {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[-o1 Option 1, -o2 Option 2]";
        assertEquals(expectedString, optionGroup.toString());
    }
}