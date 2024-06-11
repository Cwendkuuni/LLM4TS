import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 option1 = new Option("opt1", "Option 1");
 option2 = new Option("opt2", "Option 2");
 option3 = new Option("opt3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testAddOption() {
 OptionGroup result = optionGroup.addOption(option1);
 assertEquals("The option group should be the same after adding an option", optionGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 OptionGroup result = optionGroup.addOption(null);
 fail("Should not be able to add a null option");
 }

 @Test
 public void testGetNames() {
 Collection<String> result = optionGroup.getNames();
 assertEquals("The names of the options in the group should be returned correctly", Arrays.asList("opt1", "opt2", "opt3"), result);
 }

 @Test
 public void testGetOptions() {
 Collection<Option> result = optionGroup.getOptions();
 assertEquals("The options in the group should be returned correctly", Arrays.asList(option1, option2, option3), result);
 }

 @Test
 public void testSetSelectedNull() {
 OptionGroup result = optionGroup;
 result.setSelected(null);
 assertEquals("The selected option should be null after resetting it", null, result.getSelected());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedNullOption() {
 OptionGroup result = optionGroup;
 result.setSelected(null);
 fail("Should not be able to set a null option as selected");
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOption() {
 OptionGroup result = optionGroup;
 result.setSelected(option1);
 result.setSelected(option1);
 }

 @Test
 public void testSetSelectedDifferentOption() {
 OptionGroup result = optionGroup;
 result.setSelected(option2);
 assertEquals("The selected option should be option2 after setting it", "opt2", result.getSelected());
 }

 @Test
 public void testIsRequired() {
 OptionGroup result = new OptionGroup();
 result.setRequired(true);
 assertTrue("The option group should be required if setRequired is called with true", result.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup result = optionGroup;
 String expected = "[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]";
 assertEquals("The stringified representation of the group should be correct", expected, result.toString());
 }

}