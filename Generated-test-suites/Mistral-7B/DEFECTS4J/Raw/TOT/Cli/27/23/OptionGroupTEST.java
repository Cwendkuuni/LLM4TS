import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option 1", false, "o1-arg");
 Option option2 = new Option("o2", "option 2", false, "o2-arg");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testOptionGroupGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option 1", false, "o1-arg");
 Option option2 = new Option("o2", "option 2", false, "o2-arg");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection names = optionGroup.getNames();

 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 }

 @Test
 public void testOptionGroupSetSelected() throws ParseException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option 1", false, "o1-arg");
 Option option2 = new Option("o2", "option 2", false, "o2-arg");
 Option invalidOption = new Option("o3", "invalid option", false, "o3-arg");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(invalidOption);
 fail("AlreadySelectedException should have been thrown");
 } catch (AlreadySelectedException e) {
 // Expected
 }

 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testOptionGroupSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option 1", false, "o1-arg");
 Option option2 = new Option("o2", "option 2", false, "o2-arg");
 Option option3 = new Option("o3", "option 3", false, "o3-arg");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals("[--option-1 option 1\n--option-2 option 2\n--option-3 option 3\n]", optionGroup.toString());
 }
}