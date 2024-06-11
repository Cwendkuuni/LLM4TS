package org.apache.commons.cli;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<Option> options = optionGroup.getOptions();

 Assert.assertEquals(2, options.size());
 Assert.assertTrue(options.contains(option1));
 Assert.assertTrue(options.contains(option2));
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<String> names = optionGroup.getNames();

 Assert.assertEquals(2, names.size());
 Assert.assertTrue(names.contains(option1.getOpt()));
 Assert.assertTrue(names.contains(option2.getOpt()));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);

 Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());

 optionGroup.setSelected(option2);

 Assert.assertEquals(option2.getOpt(), optionGroup.getSelected());

 optionGroup.setSelected(option1); // should throw exception
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedTwice() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 String result = optionGroup.toString();

 String expected = "[-option1 Option 1, --option2 Option 2 ]";

 Assert.assertEquals(expected, result);
 }

}