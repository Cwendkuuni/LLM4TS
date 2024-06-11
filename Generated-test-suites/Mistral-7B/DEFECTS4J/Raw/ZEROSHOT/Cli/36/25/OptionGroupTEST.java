import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();

 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");
 Option option3 = new Option("opt3", "Option 3", false, "o3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertEquals("opt1", names.iterator().next());
 assertEquals("opt2", names.iterator().next());
 assertEquals("opt3", names.iterator().next());
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals(option2, options.iterator().next());
 assertEquals(option3, options.iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 optionGroup.setRequired(true);
 assertEquals(true, optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]";
 assertEquals(expected, optionGroup.toString());
 }
}