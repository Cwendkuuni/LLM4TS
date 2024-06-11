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
 option1 = new Option("option1", "option1 description");
 optionGroup.addOption(option1);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "option1 description");
 option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 Assert.assertEquals(option2.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "option1 description");
 optionGroup.addOption(option1);

 optionGroup.setSelected(null);
 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOptionTwice() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "option1 description");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testSetSelected() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "option1 description");
 option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
 }

 @Test
 public void testSetRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 Assert.assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "option1 description");
 option2 = new Option("option2", "option2 description");
 option3 = new Option("option3", "option3 description", true, "force");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Assert.assertEquals("[--option1 option1 description, -option2 option2 description, --force force]", optionGroup.toString());
 }
}