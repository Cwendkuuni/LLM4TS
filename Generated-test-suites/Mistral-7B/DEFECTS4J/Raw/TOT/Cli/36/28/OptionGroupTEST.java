package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 @Test
 public void testOptionGroup_AddOptions() {
 Option option1 = new Option("o1", true, "Short description for option1");
 Option option2 = new Option("o2", true, "Short description for option2");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<Option> options = optionGroup.getOptions();
 assertEquals(2, options.size());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroup_SelectOption_AlreadySelected() {
 Option option1 = new Option("o1", true, "Short description for option1");
 Option option2 = new Option("o2", true, "Short description for option2");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testOptionGroup_SelectOption() {
 Option option1 = new Option("o1", true, "Short description for option1");
 Option option2 = new Option("o2", true, "Short description for option2");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testOptionGroup_Required() {
 Option option1 = new Option("o1", true, "Short description for option1");
 Option option2 = new Option("o2", true, "Short description for option2");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testOptionGroup_ToString() {
 Option option1 = new Option("o1", true, "Short description for option1");
 Option option2 = new Option("o2", true, "Short description for option2");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--o1, --o2]", optionGroup.toString());
 }
}