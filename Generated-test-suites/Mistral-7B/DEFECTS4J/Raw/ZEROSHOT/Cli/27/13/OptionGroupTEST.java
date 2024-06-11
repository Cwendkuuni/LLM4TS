import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setup() {
 optionGroup = new OptionGroup();

 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", true, "Description for option2");
 Option option3 = new Option("o3", "option3", true, "Description for option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 assertTrue(names.contains("o3"));
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
 fail("Expected an IllegalStateException when selecting a null option");
 }

 @Test
 public void testSetSelectedOption1() throws AlreadySelectedException {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedTwice() throws AlreadySelectedException {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 fail("Expected an AlreadySelectedException when selecting the same option twice");
 }

 @Test
 public void testSetSelectedOption2() throws AlreadySelectedException {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 assertEquals("o2", optionGroup.getSelected());
 }

 @Test
 public void testIsRequired() {
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[--option1 Description for option1, --option2 Description for option2, --option3 Description for option3]";
 String actual = optionGroup.toString();
 assertEquals(expected, actual);
 }
}