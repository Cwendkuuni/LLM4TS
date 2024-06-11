import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        Option option3 = new Option("c", "option C");

        OptionGroup result = optionGroup.addOption(option1);
        assertNotNull(result);
        assertTrue(optionGroup.getOptions().contains(option1));

        result = optionGroup.addOption(option2);
        assertNotNull(result);
        assertTrue(optionGroup.getOptions().contains(option2));

        result = optionGroup.addOption(option3);
        assertNotNull(result);
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        Option option3 = new Option("c", "option C");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        assertNotNull(names);
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        Option option3 = new Option("c", "option C");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<Option> options = optionGroup.getOptions();
        assertNotNull(options);
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        Option option3 = new Option("c", "option C");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
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
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a option A, -b option B]";
        assertEquals(expected, optionGroup.toString());
    }
}