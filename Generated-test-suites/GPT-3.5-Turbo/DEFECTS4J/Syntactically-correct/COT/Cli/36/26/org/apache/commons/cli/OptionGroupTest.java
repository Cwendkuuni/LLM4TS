import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        // Test adding a single option to the group
        Option option1 = new Option("f", "file", true, "Input file");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        // Test getting the names of the options in the group
        Option option1 = new Option("f", "file", true, "Input file");
        Option option2 = new Option("d", "directory", true, "Input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.getNames().contains("f"));
        assertTrue(group.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        // Test getting all the options in the group
        Option option1 = new Option("f", "file", true, "Input file");
        Option option2 = new Option("d", "directory", true, "Input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        // Test setting the selected option of the group
        Option option1 = new Option("f", "file", true, "Input file");
        Option option2 = new Option("d", "directory", true, "Input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals("f", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        // Test selecting an option that is already selected
        Option option1 = new Option("f", "file", true, "Input file");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        group.setSelected(option1);
        group.setSelected(option1); // Exception should be thrown here
    }

    @Test
    public void testGetSelected() {
        // Test getting the selected option of the group
        Option option1 = new Option("f", "file", true, "Input file");
        Option option2 = new Option("d", "directory", true, "Input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals("f", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        // Test setting the required property of the group
        Option option1 = new Option("f", "file", true, "Input file");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        // Test getting the string representation of the group
        Option option1 = new Option("f", "file", true, "Input file");
        Option option2 = new Option("d", "directory", true, "Input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--file Input file, --directory Input directory]";

        assertEquals(expected, group.toString());
    }
}