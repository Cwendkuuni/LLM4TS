import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option = new Option("option1", "description1");
 optionGroup.addOption(option);
 Assert.assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Assert.assertEquals(2, optionGroup.getNames().size());
 Assert.assertTrue(optionGroup.getNames().containsAll(Arrays.asList("option1", "option2")));
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 optionGroup.setSelected(null);
 Assert.assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option1", "description1");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetSelected() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 Assert.assertEquals("option1", optionGroup.getSelected());
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
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 Assert.assertEquals("[--option1 description1]", optionGroup.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullOption() {
 optionGroup = new OptionGroup();
 Option option = null;
 optionGroup.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithSameKeyTwice() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option1", "description2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 }
}