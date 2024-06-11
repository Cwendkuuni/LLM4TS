import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private List<Option> options;

 @Test
 public void testOptionGroupConstructors() {
 optionGroup = new OptionGroup();
 Assert.assertNotNull(optionGroup);
 optionGroup = new OptionGroup("My Group");
 Assert.assertEquals("My Group", optionGroup.getLongOpt());
 }

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 options = new ArrayList<>(optionGroup.getOptions());
 Assert.assertEquals(2, options.size());
 Assert.assertEquals(option1, options.get(0));
 Assert.assertEquals(option2, options.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 OptionGroup optionGroup = new OptionGroup();
 try {
 optionGroup.addOption(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("option cannot be null", e.getMessage());
 }
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 List<String> names = new ArrayList<>(optionGroup.getNames());
 Assert.assertEquals(2, names.size());
 Assert.assertEquals("opt1", names.get(0));
 Assert.assertEquals("opt2", names.get(1));
 }

 @Test
 public void testGetOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 List<Option> options = new ArrayList<>(optionGroup.getOptions());
 Assert.assertEquals(2, options.size());
 Assert.assertEquals(option1, options.get(0));
 Assert.assertEquals(option2, options.get(1));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 Assert.assertEquals("opt1", optionGroup.getSelected());
 optionGroup.setSelected(option2);
 Assert.assertEquals("opt2", optionGroup.getSelected());
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 Option option2 = new Option("opt2", "option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 Assert.assertEquals("option opt1 is already selected in this group", e.getMessage());
 }
 }

 @Test
 public void testSetSelectedNull() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "option1");
 try {
 optionGroup.setSelected(null);
 } catch (NullPointerException e) {
 //expected since setSelected method doesn't accept null
 }
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 Assert.assertTrue(optionGroup.isRequired());
 optionGroup.setRequired(false);
 Assert.assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();Option option1 = new Option("opt1", "option1");Option option2 = new Option("opt2", "option2");optionGroup.addOption(option1);optionGroup.addOption(option2);String expected = "[--opt1 option1, --opt2 option2]";Assert.assertEquals(expected, optionGroup.toString());}

 @Test
 public void testAddSameOptionTwice() {
 OptionGroup optionGroup = new OptionGroup();Option option = new Option("opt1", "option1");optionGroup.addOption(option);optionGroup.addOption(option);List<Option> options = new ArrayList<>(optionGroup.getOptions());Assert.assertEquals(1, options.size());}

}