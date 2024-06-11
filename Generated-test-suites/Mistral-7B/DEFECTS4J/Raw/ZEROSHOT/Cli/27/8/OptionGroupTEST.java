import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;
 private Option option3;

 @Before
 public void setUp() {
 option1 = new Option("o1", "option1", true, "Description for Option 1");
 option2 = new Option("o2", "option2", true, "Description for Option 2");
 option3 = new Option("o3", "option3", true, "Description for Option 3");
 optionGroup = new OptionGroup("testGroup", "Test Option Group")
 .addOption(option1)
 .addOption(option2)
 .addOption(option3);
 }

 @Test
 public void testGetNames() {
 Map<String, Option> expectedOptions = new LinkedHashMap<>();
 expectedOptions.put("o1", option1);
 expectedOptions.put("o2", option2);
 expectedOptions.put("o3", option3);
 assertEquals(expectedOptions.keySet(), optionGroup.getNames());
 }

 @Test
 public void testGetOptions() {
 Map<String, Option> expectedOptions = new LinkedHashMap<>();
 expectedOptions.put("o1", option1);
 expectedOptions.put("o2", option2);
 expectedOptions.put("o3", option3);
 assertEquals(expectedOptions, optionGroup.getOptions());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 assertEquals(option1.getOpt(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup.setSelected(option2);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullOption() {
 optionGroup.addOption(null);
 }

 @Test
 public void testToString() {
 String expected = "[-o1 Description for Option 1, -o2 Description for Option 2, --option3 Description for Option 3]";
 assertEquals(expected, optionGroup.toString());
 }
}