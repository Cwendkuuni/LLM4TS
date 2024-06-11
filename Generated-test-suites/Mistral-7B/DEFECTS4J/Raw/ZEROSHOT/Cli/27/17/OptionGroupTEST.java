import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testOptionGroupCreation() {
 optionGroup = new OptionGroup();
 assertEquals("OptionGroup creation is successful", 0, optionGroup.getNames().size());
 assertEquals("OptionGroup creation is successful", 0, optionGroup.getOptions().size());
 }

 @Test
 public void testAddOption() {
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("Number of options in the group is correct", 2, optionGroup.getNames().size());
 assertEquals("Number of options in the group is correct", 2, optionGroup.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = null;
 optionGroup.addOption(option);
 fail("Should not be able to add null option");
 }

 @Test
 public void testSetSelectedNullOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("o1", "option1", false, "Description for option1");
 optionGroup.addOption(option);

 try {
 optionGroup.setSelected(null);
 fail("Should not be able to set null option as selected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetSelectedSameOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 fail("Should be able to set option1 as selected since it's the first option added");
 }

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 // Expected, option1 was already set as selected
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(option2);
 fail("Should not be able to set option2 as selected since option1 was previously set");
 } catch (AlreadySelectedException e) {
 // Expected
 }
 }

 @Test
 public void testGetSelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);

 assertEquals("Selected option name is correct", "o1", optionGroup.getSelected());
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue("OptionGroup is required", optionGroup.isRequired());
 }

 @Test
 public void testSetRequiredFalse() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse("OptionGroup is not required", optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 Option option3 = new Option("o3", "option3", false, "Description for option3");
 Option optionGroupOption = new Option("g", "group", false, "Description for option group");
 optionGroupOption.setValues(new String[]{"g1", "g2"});
 Option optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 optionGroup.addOption(optionGroupOption);
 optionGroupOption.setValue(optionGroup);
 optionGroup.setSelected(option1);

 String expectedOutput = "[--g group\n" +
 "  --g1 group1\n" +
 "  --g2 group2\n" +
 "  -o1 option1 Description for option1\n" +
 "  -o2 option2 Description for option2\n" +
 "  -o3 option3 Description for option3\n" +
 "]\n";

 assertEquals("ToString method returns the expected output", expectedOutput, optionGroup.toString());
 }

}