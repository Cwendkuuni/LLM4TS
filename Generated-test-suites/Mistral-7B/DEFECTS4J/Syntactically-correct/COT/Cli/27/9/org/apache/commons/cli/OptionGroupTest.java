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
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(option1.getOpt(), optionGroup.getNames().iterator().next());
 Assert.assertEquals(option2.getOpt(), optionGroup.getNames().iterator().next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNamesUnmodifiable() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertNotSame(optionGroup.getNames(), optionGroup.getNames()); // check it's not the same object
 // test the exception
 optionGroup.getNames().add(new Option("o3", "Option 3"));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 option3 = new Option("o3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 Assert.assertEquals(option1, optionGroup.getSelectedOption());

 optionGroup.setSelected(option2);
 Assert.assertEquals(option2, optionGroup.getSelectedOption());

 optionGroup.setSelected(option3);
 Assert.assertEquals(option3, optionGroup.getSelectedOption());

 optionGroup.setSelected(option1); // should not throw an exception since it's being reset
 Assert.assertEquals(option1, optionGroup.getSelectedOption());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
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

 Assert.assertEquals("[--o1 Option 1, --o2 Option 2]", optionGroup.toString());
 }

}