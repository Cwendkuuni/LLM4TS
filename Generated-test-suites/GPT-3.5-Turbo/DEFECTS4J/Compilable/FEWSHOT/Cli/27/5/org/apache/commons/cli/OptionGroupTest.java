package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Test
    public void testAddOption() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("o1"));
        assertTrue(optionGroup.getNames().contains("o2"));
    }

    @Test
    public void testGetOptions() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            assertEquals("o1", optionGroup.getSelected());

            optionGroup.setSelected(option2);
            assertEquals("o2", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not occur");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        option1.setDescription("description1");
        Option option2 = new Option("o2", "option2");
        option2.setDescription("description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 description1, -o2 description2]";
        assertEquals(expected, optionGroup.toString());
    }
}