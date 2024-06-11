package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        Option option3 = new Option("o3", "option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().toArray(new Option[0])[0]);
        assertEquals(option2, optionGroup.getOptions().toArray(new Option[0])[1]);
        assertEquals(option3, optionGroup.getOptions().toArray(new Option[0])[2]);
    }

    @Test
    public void testOptionGroupGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        Option option3 = new Option("o3", "option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> names = new ArrayList<>(optionGroup.getNames());
        Collections.sort(names);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add(option1.getKey());
        expectedNames.add(option2.getKey());
        expectedNames.add(option3.getKey());

        assertEquals(expectedNames, names);
    }

    @Test
    public void testOptionGroupSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        Option option3 = new Option("o3", "option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertEquals(option3.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertSame(option1, optionGroup.getSelectedOption());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testOptionGroupIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();

        assertFalse(optionGroup1.isRequired());
        optionGroup2.setRequired(true);
        assertTrue(optionGroup2.isRequired());
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--o1 option 1, --o2 option 2]", optionGroup.toString());
    }

    @Test
    public void testOptionGroupParse() throws ParseException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        String[] args = {"--o1", "input"};
        options.parse(args);

        Option selectedOption = optionGroup.getSelectedOption();
        assertEquals(option1, selectedOption);
        assertEquals("input", option1.getValue());
    }
}