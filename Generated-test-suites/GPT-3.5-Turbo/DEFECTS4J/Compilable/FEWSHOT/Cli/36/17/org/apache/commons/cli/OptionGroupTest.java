package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();

        assertSame(optionGroup, optionGroup.addOption(option1));
        assertEquals(1, optionGroup.getOptions().size());
        assertSame(option1, optionGroup.getOptions().toArray()[0]);

        assertSame(optionGroup, optionGroup.addOption(option2));
        assertEquals(2, optionGroup.getOptions().size());
        assertSame(option2, optionGroup.getOptions().toArray()[1]);
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("d", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("d", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[-f File option, -d Directory option]", optionGroup.toString());
    }
}