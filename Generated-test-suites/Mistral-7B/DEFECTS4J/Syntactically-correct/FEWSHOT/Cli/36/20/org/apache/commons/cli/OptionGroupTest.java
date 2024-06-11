package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Set<String> expectedNames;
    private Option option1;
    private Option option2;
    private Option option3;

    public OptionGroupTest(Option option1, Option option2, Option option3) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    @Parameterized.Parameters(name = "{index}: OptionGroup with options [{0}, {1}, {2}]")
    public static Iterable<Object[]> data() {
        Option option1 = new Option("o1", "option1", false, "o1");
        Option option2 = new Option("o2", "option2", false, "o2");
        Option option3 = new Option("o3", "option3", false, "o3");

        return Arrays.asList(new Object[][] {
                {option1, option2, option3},
                {option2, option1, option3},
                {option3, option1, option2},
                {option1, option1, option3},
                {option1, option1, null},
                {option1, null, option3},
                {null, option2, option3}
        });
    }

    @Test
    public void testAddOption() {
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertEquals(3, optionGroup.getNames().size());
    }

    @Test
    public void testGetNames() {
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Set<String> actualNames = new HashSet<>(optionGroup.getNames());
        expectedNames = new HashSet<>(Arrays.asList(option1.getKey(), option2.getKey(), option3.getKey()));

        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions() {
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<Option> actualOptions = new ArrayList<>(optionGroup.getOptions());
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testSetSelected() {
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertEquals(option3.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());

        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedString = "[-o1, --o1, -o2, --o2, -o3, --o3]";

        assertEquals(expectedString, optionGroup.toString());
    }
}