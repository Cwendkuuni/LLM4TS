import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1", true, "o1-desc");
 option2 = new Option("o2", "Option 2", true, "o2-desc");
 option3 = new Option("o3", "Option 3", true, "o3-desc");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 assertTrue(names.contains("o3"));
 }

 @Test
 public void testGetOptions() {
 Collection options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 assertTrue(options.contains(option3));
 }

 @Test
 public void testSetAndGetSelected() {
 Option selectedOption = null;
 try {
 optionGroup.setSelected(option1);
 selectedOption = optionGroup.getSelected();
 assertEquals("o1", selectedOption.getOpt());
 } finally {
 optionGroup.setSelected(null);
 }

 try {
 optionGroup.setSelected(option2);
 selectedOption = optionGroup.getSelected();
 assertEquals("o2", selectedOption.getOpt());
 } finally {
 optionGroup.setSelected(null);
 }

 try {
 optionGroup.setSelected(option3);
 selectedOption = optionGroup.getSelected();
 assertEquals("o3", selectedOption.getOpt());
 } finally {
 optionGroup.setSelected(null);
 }

 try {
 optionGroup.setSelected(option1);
 fail("AlreadySelectedException should have been thrown");
 } catch (AlreadySelectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsRequired() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());

 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[-o1 Option 1\n--o1-desc\n, -o2 Option 2\n--o2-desc\n, -o3 Option 3\n--o3-desc]\n";
 assertEquals(expected, optionGroup.toString());
 }
}