package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import java.util.Arrays;
import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        Collection<Option> options = group.getOptions();
        Assert.assertTrue(options.contains(option1));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        Collection<String> names = group.getNames();
        Assert.assertEquals(Arrays.asList("a", "b"), names);
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        try {
            group.setSelected(option1);
            Assert.assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Unexpected AlreadySelectedException");
        }
    }

    @Test
    public void testSetSelectedAlreadySelectedException() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        try {
            group.setSelected(option1);
            group.setSelected(option1);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        String expected = "[-a Option Alpha, -b Option Beta]";
        Assert.assertEquals(expected, group.toString());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Assert.assertTrue(group.isRequired());
    }
}