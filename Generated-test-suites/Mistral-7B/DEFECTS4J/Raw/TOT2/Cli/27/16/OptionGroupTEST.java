package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

@Test
public void testAddOption() {
OptionGroup optionGroup = new OptionGroup();
Option option = new Option("testOption", "Test Option", false, "test");
optionGroup.addOption(option);
Collection<Option> options = optionGroup.getOptions();
assertEquals(1, options.size());
Option testOption = options.iterator().next();
assertEquals(option, testOption);
}

@Test
public void testGetNames() {
OptionGroup optionGroup = new OptionGroup();
Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
Option option2 = new Option("testOption2", "Test Option 2", false, "test2");
optionGroup.addOption(option1);
optionGroup.addOption(option2);
Collection<String> names = optionGroup.getNames();
assertEquals(Arrays.asList("testOption1", "testOption2"), names);
}

@Test
public void testSetSelected() {
OptionGroup optionGroup = new OptionGroup();
Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
Option option2 = new Option("testOption2", "Test Option 2", false, "test2");
optionGroup.addOption(option1);
optionGroup.addOption(option2);
optionGroup.setSelected(option1);
assertEquals(option1.getOpt(), optionGroup.getSelected());
}

@Test(expected = AlreadySelectedException.class)
public void testSetSelectedAlreadySelected() {
OptionGroup optionGroup = new OptionGroup();
Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
Option option2 = new Option("testOption2", "Test Option 2", false, "test2");
optionGroup.addOption(option1);
optionGroup.addOption(option2);
optionGroup.setSelected(option1);
optionGroup.setSelected(option2);
}
}