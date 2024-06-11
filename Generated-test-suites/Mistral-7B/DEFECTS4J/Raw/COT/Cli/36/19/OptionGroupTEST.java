import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 optionGroup.addOption(option1);
 assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals(2, optionGroup.getNames().size());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(null);
 fail("Expected AlreadySelectedException");
 } catch (AlreadySelectedException e) {
 // expected
 }
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 option3 = new Option("opt3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 String expected = "[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]";
 assertEquals(expected, optionGroup.toString());
 }

 @Test
 public void testIsRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }
}