import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, false);
 Option option2 = new Option("option2", "Option 2", false, false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testOptionGroupGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, false);
 Option option2 = new Option("option2", "Option 2", false, false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertTrue(optionGroup.getNames().contains("option1"));
 assertTrue(optionGroup.getNames().contains("option2"));
 }

 @Test
 public void testOptionGroupSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, false);
 Option option2 = new Option("option2", "Option 2", false, false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals("option1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals("option2", optionGroup.getSelected());

 try {
 optionGroup.setSelected(option1);
 fail();
 } catch (AlreadySelectedException e) {
 // expected
 }
 }

 @Test
 public void testOptionGroupIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, false);
 Option option2 = new Option("option2", "Option 2", false, false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertFalse(optionGroup.isRequired());

 OptionGroup requiredOptionGroup = new OptionGroup();
 requiredOptionGroup.setRequired(true);

 assertTrue(requiredOptionGroup.isRequired());
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", false, false);
 Option option2 = new Option("option2", "Option 2", false, false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--option1 Option 1, --option2 Option 2]", optionGroup.toString());
 }
}