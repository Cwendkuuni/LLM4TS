import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 @Test
 public void testAddOptionToGroup() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");

 group.addOption(option1);
 group.addOption(option2);

 assertEquals(2, group.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");

 group.addOption(option1);
 group.addOption(option2);

 Collection names = group.getNames();

 assertEquals(2, names.size());
 assertEquals("opt1", names.iterator().next());
 assertEquals("opt2", names.iterator().next());
 }

 @Test
 public void testSetSelectedOption() throws Exception {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 assertEquals("o1", group.getSelected());

 group.setSelected(option2);
 assertNull(group.getSelected());

 group.setSelected(option1);
 assertEquals("o1", group.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedTwiceException() throws Exception {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1", true, "o1");
 Option option2 = new Option("opt2", "Option 2", false, "o2");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 group.setSelected(option2);
 }

 @Test
 public void testIsRequired() {
 OptionGroup group = new OptionGroup();
 group.setRequired(true);
 assertEquals(true, group.isRequired());

 OptionGroup notRequiredGroup = new OptionGroup();
 notRequiredGroup.setRequired(false);
 assertEquals(false, notRequiredGroup.isRequired());
 }
}