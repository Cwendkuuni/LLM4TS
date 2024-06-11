import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithRequiredOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option requiredOption = new Option("r", "required", true, "This is a required option");
 optionGroup.addOption(requiredOption);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions().toArray(new Option[0]), new String[]{"-r"});
 assertTrue(optionGroup.isSelected() != null);
 } catch (ParseException e) {
 fail("Parsing failed.");
 }
 }

 @Test
 public void testOptionGroupWithMultipleOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option opt1 = new Option("o1", "option1", false, "Option 1");
 Option opt2 = new Option("o2", "option2", false, "Option 2");
 optionGroup.addOption(opt1);
 optionGroup.addOption(opt2);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions().toArray(new Option[0]), new String[]{"-o1", "-o2"});
 assertEquals(optionGroup.getSelected(), opt1);
 } catch (ParseException e) {
 fail("Parsing failed.");
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupWithAlreadySelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option opt1 = new Option("o1", "option1", false, "Option 1");
 Option opt2 = new Option("o2", "option2", false, "Option 2");
 optionGroup.addOption(opt1);
 optionGroup.addOption(opt2);
 optionGroup.setSelected(opt1);
 optionGroup.setSelected(opt2);
 }

 @Test
 public void testOptionGroupWithNoSelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option opt1 = new Option("o1", "option1", false, "Option 1");
 Option opt2 = new Option("o2", "option2", false, "Option 2");
 optionGroup.addOption(opt1);
 optionGroup.addOption(opt2);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions().toArray(new Option[0]), new String[0]);
 assertNull(optionGroup.getSelected());
 } catch (ParseException e) {
 fail("Parsing failed.");
 }
 }
}