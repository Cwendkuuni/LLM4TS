import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 /**
 * Test for the addOption() method.
 */
 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 Option option3 = new Option("opt3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Collection<Option> options = optionGroup.getOptions();

 Assert.assertEquals(3, options.size());
 Assert.assertTrue(options.contains(option1));
 Assert.assertTrue(options.contains(option2));
 Assert.assertTrue(options.contains(option3));
 }

 /**
 * Test for the getNames() method.
 */
 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 Option option3 = new Option("opt3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Collection<String> names = optionGroup.getNames();

 Assert.assertEquals(3, names.size());
 Assert.assertTrue(names.contains("opt1"));
 Assert.assertTrue(names.contains("opt2"));
 Assert.assertTrue(names.contains("opt3"));
 }

 /**
 * Test for the setSelected() and getSelected() methods.
 */
 @Test
 public void testSetAndGetSelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 // should not reach here
 }

 Assert.assertEquals("opt1", optionGroup.getSelected());

 try {
 optionGroup.setSelected(option2);
 } catch (AlreadySelectedException e) {
 // should not reach here
 }

 Assert.assertEquals("opt2", optionGroup.getSelected());

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 Assert.assertEquals("Option 'opt1' has already been selected.", e.getMessage());
 }

 Assert.assertEquals("opt2", optionGroup.getSelected());
 }

 /**
 * Test for the isRequired() method.
 */
 @Test
 public void testIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("opt", "Option");

 optionGroup.setRequired(true);
 optionGroup.addOption(option);

 Assert.assertTrue(optionGroup.isRequired());

 optionGroup.setRequired(false);
 Assert.assertFalse(optionGroup.isRequired());
 }
}