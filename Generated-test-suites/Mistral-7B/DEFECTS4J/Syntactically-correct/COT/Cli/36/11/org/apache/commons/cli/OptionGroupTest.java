import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Map<String, Option> expectedOptionMap = new HashMap<>();
 expectedOptionMap.put("option1", option1);
 expectedOptionMap.put("option2", option2);
 expectedOptionMap.put("option3", option3);

 Assert.assertEquals(expectedOptionMap, optionGroup.optionMap);
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 List<String> expectedNames = new ArrayList<>();
 expectedNames.add("option1");
 expectedNames.add("option2");
 expectedNames.add("option3");

 Assert.assertEquals(expectedNames, optionGroup.getNames());
 }

 @Test
 public void testGetOptions() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 List<Option> expectedOptions = new ArrayList<>();
 expectedOptions.add(option1);
 expectedOptions.add(option2);
 expectedOptions.add(option3);

 Assert.assertEquals(expectedOptions, optionGroup.getOptions());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(null);

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSame() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);

 Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferent() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequiredTrue() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);

 Assert.assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testSetRequiredFalse() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);

 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");
 Option option3 = new Option("option3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Assert.assertEquals("[--option1 Option 1, --option2 Option 2, --option3 Option 3]", optionGroup.toString());
 }
}