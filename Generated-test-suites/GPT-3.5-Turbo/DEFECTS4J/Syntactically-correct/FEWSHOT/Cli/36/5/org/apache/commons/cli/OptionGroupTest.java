import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);

        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testAddOption_NullOption() {
        optionGroup.addOption(null);

        assertTrue(optionGroup.getOptions().isEmpty());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "dir", true, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "dir", true, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);

        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "dir", true, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option);

        assertEquals("f", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "dir", true, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -f File option, --dir Directory option]";
        assertEquals(expected, optionGroup.toString());
    }

}