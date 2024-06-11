import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 @Test
 public void testOptionGroup() throws ParseException {

 // Creating an OptionGroup
 OptionGroup optionGroup = new OptionGroup();

 // Adding options to the OptionGroup
 Option option1 = new Option("o1", "option1", false, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");
 Option option3 = new Option("o3", "option3", false, "This is option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 // Testing getNames() method
 Collection<String> names = optionGroup.getNames();
 Assert.assertEquals(3, names.size());
 Assert.assertTrue(names.contains("o1"));
 Assert.assertTrue(names.contains("o2"));
 Assert.assertTrue(names.contains("o3"));

 // Testing getOptions() method
 Collection<Option> options = optionGroup.getOptions();
 Assert.assertEquals(3, options.size());
 Assert.assertTrue(options.contains(option1));
 Assert.assertTrue(options.contains(option2));
 Assert.assertTrue(options.contains(option3));

 // Testing setSelected() method with valid option
 optionGroup.setSelected(option1);
 Assert.assertEquals("o1", optionGroup.getSelected());

 // Testing setSelected() method with invalid option
 try {
 optionGroup.setSelected(new Option("invalidOption", "", false, ""));
 Assert.fail();
 } catch (AlreadySelectedException e) {
 // Expected exception
 }

 // Testing toString() method
 Assert.assertEquals("[--option1, --option2, --option3]", optionGroup.toString());
 }

 @Test(expected = ParseException.class)
 public void testSetSelectedWithInvalidOption() throws ParseException {

 // Creating an OptionGroup
 OptionGroup optionGroup = new OptionGroup();

 // Adding options to the OptionGroup
 Option option1 = new Option("o1", "option1", false, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");
 Option option3 = new Option("o3", "option3", false, "This is option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 // Testing setSelected() method with invalid option
 optionGroup.setSelected(new Option("invalidOption", "", false, ""));
 }
}