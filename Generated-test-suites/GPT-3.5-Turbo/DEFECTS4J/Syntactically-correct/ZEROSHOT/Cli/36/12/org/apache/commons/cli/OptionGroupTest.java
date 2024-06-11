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
        Option option = new Option("t", "test", false, "Testing option");
        optionGroup.addOption(option);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        Option option2 = new Option("u", "test2", false, "Testing option 2");
        Option option3 = new Option("v", "test3", false, "Testing option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("t"));
        assertTrue(optionGroup.getNames().contains("u"));
        assertTrue(optionGroup.getNames().contains("v"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        Option option2 = new Option("u", "test2", false, "Testing option 2");
        Option option3 = new Option("v", "test3", false, "Testing option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        Option option2 = new Option("u", "test2", false, "Testing option 2");
        Option option3 = new Option("v", "test3", false, "Testing option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals("t", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("u", optionGroup.getSelected());
        
        optionGroup.setSelected(option3);
        assertEquals("v", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        optionGroup.addOption(option1);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        Option option2 = new Option("u", "test2", false, "Testing option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);
        assertEquals("t", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("t", "test1", false, "Testing option 1");
        Option option2 = new Option("u", "test2", false, "Testing option 2");
        Option option3 = new Option("v", "test3", false, "Testing option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "["
                + "-t Testing option 1, "
                + "-u Testing option 2, "
                + "-v Testing option 3"
                + "]";
        assertEquals(expected, optionGroup.toString());
    }
}