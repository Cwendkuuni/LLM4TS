import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option = new Option("optionA", "Option A");
 optionGroup.addOption(option);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("optionA", "Option A");
 Option option2 = new Option("optionB", "Option B");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Assert.assertEquals(2, optionGroup.getNames().size());
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "Option A");
 Option optionB = new Option("optionB", "Option B");
 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);
 optionGroup.setSelected(optionA);
 Assert.assertEquals("optionA", optionGroup.getSelected());
 optionGroup.setSelected(optionB);
 Assert.assertNull(optionGroup.getSelected());
 optionGroup.setSelected(optionA);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "Option A");
 Option optionB = new Option("optionB", "Option B");
 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);
 optionGroup.setSelected(optionA);
 optionGroup.setSelected(optionB);
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
 Option option1 = new Option("optionA", "Option A");
 Option option2 = new Option("optionB", "Option B");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Assert.assertEquals("[--optionA Option A, --optionB Option B]", optionGroup.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetSelectedException() {
 optionGroup = new OptionGroup();
 Assert.assertNull(optionGroup.getSelected());
 }
}