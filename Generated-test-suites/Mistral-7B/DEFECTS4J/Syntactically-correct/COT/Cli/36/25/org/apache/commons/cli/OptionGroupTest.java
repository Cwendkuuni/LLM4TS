import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Before
 public void setup() {
 optionGroup = new OptionGroup();
 option1 = new Option("option1", "Description for option1");
 option2 = new Option("option2", "Description for option2");
 option3 = new Option("option3", "Description for option3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("option", "Description for option");
 optionGroup.addOption(option);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 Assert.assertEquals(3, names.size());
 Assert.assertTrue(names.containsAll(option1.getKey(), option2.getKey(), option3.getKey()));
 }

 @Test
 public void testSetSelected_Null() {
 optionGroup.setSelected(null);
 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelected_SameOption() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelected_DifferentOption() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 Assert.assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("[--option1 Description for option1, --option2 Description for option2, --option3 Description for option3]", optionGroup.toString());
 }

}