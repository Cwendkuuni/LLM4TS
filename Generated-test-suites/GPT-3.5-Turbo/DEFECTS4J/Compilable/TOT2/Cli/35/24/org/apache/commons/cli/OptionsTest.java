package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optA", false, "Option A");
        Option option2 = new Option("b", "optB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "optC", true, "Option C");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    // Add more test cases for each public method of the Options class

}