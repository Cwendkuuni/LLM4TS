package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Creates a test {@link OptionGroup}.
     *
     * @return the tested {@link OptionGroup}.
     */
    private OptionGroup createOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("opt1", "Option 1", false, '1');
        Option option2 = new Option("opt2", "Option 2", false, '2');
        Option option3 = new Option("opt3", "Option 3", false, '3');

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        return optionGroup;
    }

    /**
     * Tests the {@link OptionGroup#getNames()} method.
     */
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = createOptionGroup();

        List<String> names = Arrays.asList("opt1", "opt2", "opt3");
        Map<String, Option> expectedOptions = new HashMap<>();
        expectedOptions.put("opt1", new Option("opt1", null, false, '1'));
        expectedOptions.put("opt2", new Option("opt2", null, false, '2'));
        expectedOptions.put("opt3", new Option("opt3", null, false, '3'));

        Assert.assertEquals(names, optionGroup.getNames());
        Assert.assertEquals(expectedOptions, optionGroup.getOptionMap());
    }

    /**
     * Tests the {@link OptionGroup#getOptions()} method.
     */
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = createOptionGroup();

        List<Option> expectedOptions = Arrays.asList(
                new Option("opt1", null, false, '1'),
                new Option("opt2", null, false, '2'),
                new Option("opt3", null, false, '3')
        );

        Assert.assertEquals(expectedOptions, optionGroup.getOptions());
    }

    /**
     * Tests the {@link OptionGroup#setSelected(Option)} method.
     */
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = createOptionGroup();
        Option option1 = new Option("opt1", null, false, '1');
        Option option2 = new Option("opt2", null, false, '2');

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals(option2.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertNotNull(optionGroup.getSelected());
            Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertNotNull(optionGroup.getSelected());
            Assert.assertEquals(option2.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option2);
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals(option2.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option2);
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option3);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option1);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option2);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option1);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option3);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option1);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option2);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(option3);
        }

        try {
            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }
    }

    /**
     * Tests the {@link OptionGroup#getSelected()} method.
     */
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = createOptionGroup();
        Option option1 = new Option("opt1", null, false, '1');
        Option option2 = new Option("opt2", null, false, '2');

        Assert.assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals(option2.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    /**
     * Tests the {@link OptionGroup#setRequired(boolean)} and {@link OptionGroup#isRequired()} methods.
     */
    @Test
    public void testSetRequiredAndIsRequired() {
        OptionGroup optionGroup = createOptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Tests the {@link OptionGroup#toString()} method.
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = createOptionGroup();
        Option option1 = new Option("opt1", null, false, '1');
        Option option2 = new Option("opt2", null, false, '2');
        Option option3 = new Option("opt3", null, false, '3');

        Assert.assertEquals("[--opt1, --opt2, --opt3]", optionGroup.toString());
        Assert.assertEquals("[--opt1, --opt2, --opt3]", optionGroup.toString());

        optionGroup.setSelected(option1);
        Assert.assertEquals("[--opt1, --opt2, --opt3]", optionGroup.toString());

        optionGroup.setSelected(option2);
        Assert.assertEquals("[--opt2, --opt1, --opt3]", optionGroup.toString());

        optionGroup.setSelected(option3);
        Assert.assertEquals("[--opt3, --opt1, --opt2]", optionGroup.toString());
    }

    /**
     * Tests the {@link OptionGroup#setSelected(Option)} method with an already selected option.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        OptionGroup optionGroup = createOptionGroup();
        Option option1 = new Option("opt1", null, false, '1');
        Option option2 = new Option("opt2", null, false, '2');

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}