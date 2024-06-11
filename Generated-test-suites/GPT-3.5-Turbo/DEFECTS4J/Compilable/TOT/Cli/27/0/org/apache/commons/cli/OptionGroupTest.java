package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option optionA;
    private Option optionB;
    private Option optionC;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
        
        optionA = new Option("a", "option A");
        optionB = new Option("b", "option B");
        optionC = new Option("c", "option C");
        
        optionGroup.addOption(optionA)
                   .addOption(optionB)
                   .addOption(optionC);
    }

    @Test
    public void testAddOption() {
        Option optionD = new Option("d", "option D");

        optionGroup.addOption(optionD);

        assertTrue(optionGroup.getOptions().contains(optionD));
    }

    @Test
    public void testGetNames() {
        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(optionA));
        assertTrue(optionGroup.getOptions().contains(optionB));
        assertTrue(optionGroup.getOptions().contains(optionC));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(optionA);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        optionGroup.setSelected(optionA);

        optionGroup.setSelected(optionB);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        optionGroup.setSelected(optionA);
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[ -a option A, -b option B, -c option C ]";

        assertEquals(expected, optionGroup.toString());
    }

}