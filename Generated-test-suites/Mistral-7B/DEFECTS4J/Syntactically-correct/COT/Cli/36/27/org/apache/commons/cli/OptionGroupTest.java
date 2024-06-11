import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 Assert.assertEquals(option2.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 optionGroup.addOption(option1);

 optionGroup.setSelected(null);

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 optionGroup.addOption(option1);

 optionGroup.setSelected(option1);

 Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option2);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 option3 = new Option("opt3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Assert.assertEquals("[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]", optionGroup.toString());
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
}