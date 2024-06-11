import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Assert.assertEquals(3, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 List<String> names = new ArrayList<>(optionGroup.getNames());

 Assert.assertEquals(option1.getKey(), names.get(0));
 Assert.assertEquals(option2.getKey(), names.get(1));
 Assert.assertEquals(option3.getKey(), names.get(2));
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedTwice() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetSelectedValidOption() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);

 Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDuplicateOption() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt1", "option1");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 Option option3 = new Option("opt3", "option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);

 String expected = "[--opt1, option1 ]";
 String actual = optionGroup.toString();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);

 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);

 Assert.assertTrue(optionGroup.isRequired());
 }
}