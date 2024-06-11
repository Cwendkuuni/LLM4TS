import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void addOption_shouldAddOptionToGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", true, "File path");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void getNames_shouldReturnNamesOfOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "directory", true, "Directory path");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void getOptions_shouldReturnOptionsInGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "directory", true, "Directory path");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void setSelected_shouldSelectOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "directory", true, "Directory path");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("d", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void setSelected_shouldThrowExceptionIfOptionAlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "directory", true, "Directory path");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void setSelected_shouldResetSelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", true, "File path");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);
        assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void setRequired_shouldSetRequiredFlag() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void toString_shouldReturnStringifiedRepresentation() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "directory", true, "Directory path");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-f File path, -d Directory path]";
        assertEquals(expected, optionGroup.toString());
    }
}