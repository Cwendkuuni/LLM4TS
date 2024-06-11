import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option 1", true, "o1-desc");
 Option option2 = new Option("o2", "option 2", false, "o2-desc");
 Option option3 = new Option("o3", "option 3", false, "o3-desc");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection names = optionGroup.getNames();
 assertNotNull(names);
 assertEquals(3, names.size());
 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 assertTrue(names.contains("o3"));
 }

 @Test
 public void testGetOptions() {
 Collection options = optionGroup.getOptions();
 assertNotNull(options);
 assertEquals(3, options.size());
 assertInstanceOf(Option.class, options.toArray()[0]);
 assertInstanceOf(Option.class, options.toArray()[1]);
 assertInstanceOf(Option.class, options.toArray()[2]);
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedDifferentOption() {
 optionGroup.setSelected(option2);
 assertEquals("o2", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[-o1 option 1 true o1-desc, -o2 option 2 false o2-desc, --o3 option 3 false o3-desc]";
 assertEquals(expected, optionGroup.toString());
 }
}