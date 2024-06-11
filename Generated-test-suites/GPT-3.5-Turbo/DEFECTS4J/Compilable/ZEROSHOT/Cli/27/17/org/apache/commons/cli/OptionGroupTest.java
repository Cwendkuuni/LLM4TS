package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        Collection names = optionGroup.getNames();
        assertEquals(1, names.size());
        assertTrue(names.contains(option.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testSetSelected() {
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");

        try {
            optionGroup.setSelected(optionA);
            assertEquals(optionA.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(optionB);
            assertEquals(optionB.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());

        } catch (AlreadySelectedException e) {
            fail("Expected no AlreadySelectedException to be thrown");
        }
    }

    @Test
    public void testSetSelectedWithAlreadySelectedOption() {
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");

        try {
            optionGroup.setSelected(optionA);
            optionGroup.setSelected(optionB);

            optionGroup.setSelected(optionA);
            fail("Expected AlreadySelectedException to be thrown");

        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(optionA, e.getOption());
        }
    }

    @Test
    public void testGetSelected() {
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");

        try {
            optionGroup.setSelected(optionA);
            assertEquals(optionA.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(optionB);
            assertEquals(optionB.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());

        } catch (AlreadySelectedException e) {
            fail("Expected no AlreadySelectedException to be thrown");
        }
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
        Option optionA = new Option("a", "option a");
        optionA.setDescription("Option A");
        Option optionB = new Option("b", "option b");
        optionB.setDescription("Option B");

        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);

        String expected = "[-a Option A, -b Option B]";
        assertEquals(expected, optionGroup.toString());
    }
}