import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option firstOption = new Option("f", "first", false, "First option");
        Option secondOption = new Option("s", "second", false, "Second option");

        group.addOption(firstOption);
        group.addOption(secondOption);

        assertTrue(group.getOptions().contains(firstOption));
        assertTrue(group.getOptions().contains(secondOption));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option firstOption = new Option("f", "first", false, "First option");

        group.addOption(firstOption);

        assertTrue(group.getNames().contains("f"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option firstOption = new Option("f", "first", false, "First option");
        Option secondOption = new Option("s", "second", false, "Second option");

        group.addOption(firstOption);
        group.addOption(secondOption);

        try {
            group.setSelected(firstOption);
            assertEquals("f", group.getSelected());

            group.setSelected(secondOption);
            assertEquals("s", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    @Test
    public void testSetSelectedWithAlreadySelectedException() {
        OptionGroup group = new OptionGroup();
        Option firstOption = new Option("f", "first", false, "First option");
        Option secondOption = new Option("s", "second", false, "Second option");

        group.addOption(firstOption);
        group.addOption(secondOption);

        try {
            group.setSelected(firstOption);
            group.setSelected(secondOption);
            group.setSelected(firstOption);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option firstOption = new Option("f", "first", false, "First option");
        Option secondOption = new Option("s", "second", false, "Second option");

        group.addOption(firstOption);
        group.addOption(secondOption);

        String expected = "[f -first, s -second]";
        assertEquals(expected, group.toString());
    }
}