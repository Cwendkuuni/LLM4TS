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
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 optionGroup = new OptionGroup();
 Option option = null;
 optionGroup.addOption(option);
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 List<String> names = new ArrayList<>(optionGroup.getNames());
 List<String> expected = new ArrayList<>(Collections.singletonList("o1")) {
 @Override
 public String get(int index) {
 return super.get(index);
 }
 };

 Assert.assertEquals(expected, names);
 }

 @Test
 public void testSetSelectedNullOption() {
 optionGroup = new OptionGroup();
 Option option = new Option("o", "option");

 try {
 optionGroup.setSelected(option);
 } catch (AlreadySelectedException e) {
 // expected
 }

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedNullOptionName() {
 optionGroup = new OptionGroup();
 Option option = new Option("o", "option");

 try {
 optionGroup.setSelected(null);
 } catch (AlreadySelectedException e) {
 // expected
 }

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedAlreadySelected() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 // expected
 }

 Assert.assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedNewOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option2);

 Assert.assertEquals("o2", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOptionTwice() {
 optionGroup = new OptionGroup();
 Option option = new Option("o", "option");
 optionGroup.setSelected(option);

 try {
 optionGroup.setSelected(option);
 } catch (AlreadySelectedException e) {
 // expected
 }

 Assert.assertEquals("o", optionGroup.getSelected());
 }

 @Test
 public void testIsRequired() {
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

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 String expected = "[--option-one option one, --option-two option two]";
 String actual = optionGroup.toString();

 Assert.assertEquals(expected, actual);
 }
}