package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "alpha", false, "Alpha option");
        option2 = new Option("b", "beta", false, "Beta option");
        option3 = new Option("g", "gamma", false, "Gamma option");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
        
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertNull(optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
        
        assertNotNull(optionGroup.getSelected());
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[ -a Alpha option, -b Beta option, -g Gamma option ]";
        
        assertEquals(expected, optionGroup.toString());
    }
}