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
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Map<String, Option> expectedOptionMap = new HashMap<>();
 expectedOptionMap.put("option1", option1);
 expectedOptionMap.put("option2", option2);

 Assert.assertEquals(expectedOptionMap, optionGroup.optionMap);
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 List<String> expectedNames = new ArrayList<>();
 expectedNames.add("option1");
 expectedNames.add("option2");

 Assert.assertEquals(expectedNames, new ArrayList<>(optionGroup.getNames()));
 }

 @Test
 public void testGetOptions() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 List<Option> expectedOptions = new ArrayList<>();
 expectedOptions.add(option1);
 expectedOptions.add(option2);

 Assert.assertEquals(expectedOptions, new ArrayList<>(optionGroup.getOptions()));
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(null);
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 Assert.assertTrue(optionGroup.isRequired());

 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 String expectedString = "[-option1 description1, --option2 description2]";
 Assert.assertEquals(expectedString, optionGroup.toString());
 }
}