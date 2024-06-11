import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setup() {
 optionGroup = new OptionGroup();

 Option option1 = new Option("option1", "This is option 1", true);
 Option option2 = new Option("option2", "This is option 2", true);
 Option option3 = new Option("option3", "This is option 3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertTrue(names.contains("option1"));
 assertTrue(names.contains("option2"));
 assertTrue(names.contains("option3"));
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 assertTrue(options.contains(option3));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 fail("Should throw IllegalStateException when setting selected to null");
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 fail("Should throw AlreadySelectedException when trying to set the same option twice");
 }

 @Test
 public void testSetSelectedDifferentOption() {
 Option option4 = new Option("option4", "This is option 4", false);
 optionGroup.addOption(option4);
 optionGroup.setSelected(option4);
 assertEquals("option4", optionGroup.getSelected());
 }

 @Test
 public void testSetRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testSetRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[--option1 This is option 1, --option2 This is option 2, option3 This is option 3]";
 assertEquals(expected, optionGroup.toString());
 }
}