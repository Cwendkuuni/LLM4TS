import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "testing option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getNames().contains("t"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "testing option 1");
        Option option2 = new Option("u", "test2", false, "testing option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            assertEquals("t", optionGroup.getSelected());
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException was not thrown");
        } catch (AlreadySelectedException e) {
            // Expected exception
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
        Option option1 = new Option("t", "test1", false, "testing option 1");
        Option option2 = new Option("u", "test2", false, "testing option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedOutput = "[ -t testing option 1, -u testing option 2]";
        assertEquals(expectedOutput, optionGroup.toString());
    }

}