import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 Assert.assertEquals(option2.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOptionTwice() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option2);

 Assert.assertEquals(option2.getKey(), optionGroup.getSelected());
 }

 @Test
 public void testSetRequiredFalse() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);

 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testSetRequiredTrue() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);

 Assert.assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals("[--opt1 Option 1, --opt2 Option 2 ]", optionGroup.toString());
 }
}