import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "file option");

        OptionGroup result = optionGroup.addOption(option);

        assertEquals(option, result.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "file option");
        optionGroup.addOption(option);

        assertEquals("f", optionGroup.getNames().iterator().next());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "file option");
        optionGroup.addOption(option);

        assertEquals(option, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("v", "verbose", false, "verbose option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("f", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("v", "verbose", false, "verbose option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-f file option, -v verbose option]";
        assertEquals(expected, optionGroup.toString());
    }
}