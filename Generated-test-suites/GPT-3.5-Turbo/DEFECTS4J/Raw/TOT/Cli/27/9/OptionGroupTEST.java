import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains(option1.getOpt()));
        assertTrue(optionGroup.getNames().contains(option2.getOpt()));
        assertTrue(optionGroup.getNames().contains(option3.getOpt()));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());

        // Test for AlreadySelectedException
        try {
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertEquals(e.getSelected(), option1);
        }
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
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedString = "[-" + option1.getOpt() + " " + option1.getDescription() + ", " +
                "-" + option2.getOpt() + " " + option2.getDescription() + ", " +
                "-" + option3.getOpt() + " " + option3.getDescription() + "]";

        assertEquals(expectedString, optionGroup.toString());
    }

}