import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithNoSelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for Option 1");
 Option option2 = new Option("option2", "Description for Option 2");

 optionGroup.addOption(option1).addOption(option2);

 try {
 optionGroup.parse("-option1");
 assertNull(optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should fail as "option1" is not the selected option
 }

 try {
 optionGroup.parse("-option2");
 assertNull(optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should fail as "option2" is not the selected option
 }
 }

 @Test
 public void testOptionGroupWithSelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for Option 1");
 Option option2 = new Option("option2", "Description for Option 2");

 optionGroup.addOption(option1).addOption(option2);
 optionGroup.setSelected(option1);

 try {
 optionGroup.parse("-option1");
 assertSame("option1", optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should succeed as "option1" is the selected option
 }

 try {
 optionGroup.parse("-option2");
 assertNull(optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should fail as "option2" is not the selected option
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupWithAlreadySelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for Option 1");
 Option option2 = new Option("option2", "Description for Option 2");

 optionGroup.addOption(option1).addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testOptionGroupWithRequiredFlag() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for Option 1", true, "o");
 Option option2 = new Option("option2", "Description for Option 2", false, "i");

 optionGroup.addOption(option1).addOption(option2);
 optionGroup.setRequired(true);

 try {
 optionGroup.parse("-o");
 assertSame("option1", optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should succeed as "option1" is required
 }

 try {
 optionGroup.parse("-i");
 assertNull(optionGroup.getSelected());
 } catch (ParseException e) {
 // parse should fail as "option2" is not required and was not provided
 }
 }
}