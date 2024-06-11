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

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);

 optionGroup.setSelected(null);
 Assert.assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);

 Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals("[--option-1 Option 1,\n--option-2 Option 2]\n", optionGroup.toString());
 }
}