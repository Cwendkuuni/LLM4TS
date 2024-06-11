package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");
 Option option2 = new Option("option2", "This is option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");

 optionGroup.addOption(option1);
 optionGroup.addOption(option1);
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");
 Option option2 = new Option("option2", "This is option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertTrue(optionGroup.getNames().contains("option1"));
 assertTrue(optionGroup.getNames().contains("option2"));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");
 Option option2 = new Option("option2", "This is option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals("option1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals("option2", optionGroup.getSelected());

 try {
 optionGroup.setSelected(option1);
 fail();
 } catch (AlreadySelectedException e) {
 // expected
 }
 }

 @Test
 public void testIsRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());

 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");
 Option option2 = new Option("option2", "This is option 2");
 Option option3 = new Option("option3", "This is option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals("[--option1 This is option 1, --option2 This is option 2, --option3 This is option 3]", optionGroup.toString());
 }

 @Test
 public void testSetSelectedWithMultipleOptions() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "This is option 1");
 Option option2 = new Option("option2", "This is option 2");
 Option option3 = new Option("option3", "This is option 3");
 Option option4 = new Option("option4", "This is option 4");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 optionGroup.addOption(option4);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option4);

 assertEquals("option1, option4", optionGroup.toString());
 }
}