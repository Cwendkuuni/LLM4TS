import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Option 1 description");
 option2 = new Option("option2", "Option 2 description");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Option 1 description");
 option2 = new Option("option2", "Option 2 description");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 Assert.assertEquals(option2.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Option 1 description");
 option2 = new Option("option2", "Option 2 description");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);

 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Option 1 description");
 option2 = new Option("option2", "Option 2 description");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Option 1 description");
 option2 = new Option("option2", "Option 2 description");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 optionGroup = new OptionGroup();

 optionGroup.setRequired(true);
 optionGroup.setRequired(false);

 Assert.assertTrue(optionGroup.isRequired());
 Assert.assertFalse(optionGroup.isRequired());
 }
}