import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option = new Option("opt", "option description", true, "defaultValue");
 optionGroup.addOption(option);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option 1 description");
 Option option2 = new Option("opt2", "option 2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Assert.assertEquals(2, optionGroup.getNames().size());
 Assert.assertTrue(optionGroup.getNames().containsAll(Arrays.asList("opt1", "opt2")));
 }

 @Test
 public void testSetSelected() throws Exception {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option 1 description");
 Option option2 = new Option("opt2", "option 2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(option1);
 Assert.assertEquals("opt1", optionGroup.getSelected());
 } finally {
 optionGroup.setSelected(null);
 }

 try {
 optionGroup.setSelected(option2);
 Assert.assertEquals("opt2", optionGroup.getSelected());
 } finally {
 optionGroup.setSelected(null);
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws Exception {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option 1 description");
 Option option2 = new Option("opt2", "option 2 description");
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
 optionGroup.setRequired(false);
 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option 1 description");
 Option option2 = new Option("opt2", "option 2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Assert.assertEquals("[--opt1 option 1 description, --opt2 option 2 description]", optionGroup.toString());
 }
}