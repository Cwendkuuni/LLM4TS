package org.junit.tests;

import org.apache.commons.cli.*;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionGroupTest {

    // ... Previous test cases

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", true, "o1");
        Option option2 = new Option("option2", true, "o2");
        Option option3 = new Option("option3", false, "o3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[-o1 Option1\n"
                + "  This is Option1 description.\n"
                + "-o2 Option2\n"
                + "  This is Option2 description.\n"
                + "--o3 Option3\n"
                + "  This is Option3 description.\n"
                + "]";

        String actual = optionGroup.toString();

        assertEquals(expected, actual);
    }

    // ... Additional test cases if needed

}