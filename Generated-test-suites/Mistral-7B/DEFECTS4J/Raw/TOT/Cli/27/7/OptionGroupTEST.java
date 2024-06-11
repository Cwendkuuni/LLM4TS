import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 group.addOption(option1);
 group.addOption(option2);

 assertTrue(group.getOptions().contains(option1));
 assertTrue(group.getOptions().contains(option2));
 }

 @Test
 public void testGetNames() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 group.addOption(option1);
 group.addOption(option2);

 assertEquals(group.getNames().size(), 2);
 }

 @Test
 public void testSetSelected() throws ParseException {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 group.addOption(option1);
 group.addOption(option2);

 assertNull(group.getSelected());

 group.setSelected(option1);
 assertEquals(group.getSelected(), option1.getOpt());

 try {
 group.setSelected(option2);
 fail();
 } catch (AlreadySelectedException e) {
 // expected exception
 }
 }

 @Test
 public void testGetSelected() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 assertEquals(group.getSelected(), option1.getOpt());
 }

 @Test
 public void testSetRequired() {
 OptionGroup group = new OptionGroup();

 group.setRequired(true);
 assertTrue(group.isRequired());

 group.setRequired(false);
 assertFalse(group.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 group.addOption(option1);
 group.addOption(option2);

 assertEquals(group.toString(), "[--option1 Description for option1, --option2 Description for option2]");
 }
}