import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    private OptionGroup group;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        group = new OptionGroup();
        option1 = new Option("a", "option1", false, "First option");
        option2 = new Option("b", "option2", false, "Second option");
    }

    @Test
    public void testAddOption() {
        group.addOption(option1);
        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        group.addOption(option1);
        group.addOption(option2);
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        group.addOption(option1);
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSetSelectedWithException() {
        group.addOption(option1);
        group.setSelected(option1);
        try {
            group.setSelected(option1);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetSelectedNull() {
        group.addOption(option1);
        group.setSelected(option1);
        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetRequired() {
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

}