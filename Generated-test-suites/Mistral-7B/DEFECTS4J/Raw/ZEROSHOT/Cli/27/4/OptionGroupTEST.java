import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals(3, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Collection names = optionGroup.getNames();

 assertEquals(3, names.size());
 assertEquals("opt1", names.iterator().next());
 assertEquals("opt2", names.iterator().next());
 assertEquals("opt3", names.iterator().next());
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertNull(optionGroup.getSelected());

 optionGroup.setSelected(option1); // should not throw an exception
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertEquals(true, optionGroup.isRequired());

 OptionGroup optionGroup2 = new OptionGroup();
 optionGroup2.setRequired(false);
 assertEquals(false, optionGroup2.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals("[--opt1 Option 1\n--opt2 Option 2\n--opt3 Option 3\n]", optionGroup.toString());
 }
}