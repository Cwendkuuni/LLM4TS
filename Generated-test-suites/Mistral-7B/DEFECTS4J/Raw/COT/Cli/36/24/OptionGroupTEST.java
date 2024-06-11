import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getNames().size());
 Assert.assertTrue(optionGroup.getNames().contains("o1"));
 Assert.assertTrue(optionGroup.getNames().contains("o2"));
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);

 optionGroup.setSelected(null);
 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testSetSelectedOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 option3 = new Option("o3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test
 public void testIsRequired() {
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
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 String expected = "[--o1 Option 1, --o2 Option 2]";
 Assert.assertEquals(expected, optionGroup.toString());
 }

 // Edge cases and exception handling test cases

 @Test(expected = NullPointerException.class)
 public void testAddNullOption() {
 optionGroup = new OptionGroup();
 optionGroup.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullKey() {
 optionGroup = new OptionGroup();
 Option option = new Option(null, "Option");
 optionGroup.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithEmptyKey() {
 optionGroup = new OptionGroup();
 Option option = new Option("", "Option");
 optionGroup.addOption(option);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedWithNullGroup() {
 Option option = new Option("o1", "Option 1");
 try {
 option.setSelected(null);
 } catch (AlreadySelectedException e) {
 // Expected exception
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedWithNullOption() {
 OptionGroup optionGroup = new OptionGroup();
 try {
 optionGroup.setSelected(null);
 } catch (AlreadySelectedException e) {
 // Expected exception
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedWithAlreadySelectedOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 // Expected exception
 }
 }

}