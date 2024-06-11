import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithNoOptions() {
 OptionGroup optionGroup = new OptionGroup();
 assertEquals("[]", optionGroup.toString());
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testOptionGroupWithOneOption() {
 Option option = new Option("o", "opt", false, "Option one");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option);

 assertEquals("[-opt]", optionGroup.toString());
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testOptionGroupWithMultipleOptions() {
 Option option1 = new Option("o1", "opt1", false, "Option one");
 Option option2 = new Option("o2", "opt2", false, "Option two");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--opt1, --opt2]", optionGroup.toString());
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testOptionGroupWithSelectedOption() throws Exception {
 Option option1 = new Option("o1", "opt1", false, "Option one");
 Option option2 = new Option("o2", "opt2", false, "Option two");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 assertEquals("[--opt1]", optionGroup.toString());
 assertEquals("opt1", optionGroup.getSelected());
 }

 @Test(expected = IllegalStateException.class)
 public void testOptionGroupWithTwoSelectedOptions() throws Exception {
 Option option1 = new Option("o1", "opt1", false, "Option one");
 Option option2 = new Option("o2", "opt2", false, "Option two");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testOptionGroupIsRequiredWithNoOptions() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testOptionGroupIsRequiredWithOptions() {
 Option option = new Option("o", "opt", false, "Option one");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option);
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupAlreadySelectedException() throws Exception {
 Option option1 = new Option("o1", "opt1", false, "Option one");
 Option option2 = new Option("o2", "opt2", false, "Option two");
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }
}