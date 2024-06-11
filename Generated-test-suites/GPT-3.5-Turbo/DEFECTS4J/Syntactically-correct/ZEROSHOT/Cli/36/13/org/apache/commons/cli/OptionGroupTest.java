import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-f File option, -d Directory option]";
        assertEquals(expected, optionGroup.toString());
    }
}