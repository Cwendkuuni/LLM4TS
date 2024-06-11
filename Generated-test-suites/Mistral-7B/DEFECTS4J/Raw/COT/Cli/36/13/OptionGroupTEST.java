import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 Option option = new Option("option1", "this is option1", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option);

 Map<String, Option> optionMap = optionGroup.optionMap;

 Assert.assertEquals(1, optionMap.size());
 Assert.assertEquals(option, optionMap.get("option1"));
 }

 @Test
 public void testGetNames() {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Set<String> names = new HashSet<>(optionGroup.getNames());

 Assert.assertEquals(3, names.size());
 Assert.assertTrue(names.contains("option1"));
 Assert.assertTrue(names.contains("option2"));
 Assert.assertTrue(names.contains("option3"));
 }

 @Test
 public void testGetOptions() {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 List<Option> options = new ArrayList<>(optionGroup.getOptions());

 Assert.assertEquals(3, options.size());
 Assert.assertEquals(option1, options.get(0));
 Assert.assertEquals(option2, options.get(1));
 Assert.assertEquals(option3, options.get(2));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 Assert.assertEquals("option1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 Assert.assertEquals("option2", optionGroup.getSelected());

 optionGroup.setSelected(option3);
 Assert.assertEquals("option3", optionGroup.getSelected());

 optionGroup.setSelected(option1);
 Assert.assertEquals("option1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);
 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setRequired(true);
 Assert.assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 Option option1 = new Option("option1", "this is option1", false, null);
 Option option2 = new Option("option2", "this is option2", false, null);
 Option option3 = new Option("option3", "this is option3", false, null);optionGroup = new OptionGroup(); optionGroup.addOption(option1); optionGroup.addOption(option2); optionGroup.addOption(option3);

 String toString = optionGroup.toString();

 Assert.assertEquals("[--option1 this is option1, --option2 this is option2, --option3 this is option3]", toString);
 }
}