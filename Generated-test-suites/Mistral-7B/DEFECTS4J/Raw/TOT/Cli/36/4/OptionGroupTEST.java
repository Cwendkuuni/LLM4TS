import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithValidOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-o1"});
 Assert.assertEquals(option1.getKey(), cmd.getOptionValue("o1"));
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test
 public void testOptionGroupWithRequiredOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", true, "r1", "required option");
 Option option2 = new Option("o2", "option two");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[0]);
 Assert.fail("A required option must be provided.");
 } catch (ParseException e) {
 // expected
 }

 try {
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-r1"});
 Assert.assertEquals(option1.getKey(), cmd.getOptionValue("o1"));
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test
 public void testOptionGroupWithInvalidOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-o3"});
 Assert.fail("An invalid option was provided.");
 } catch (ParseException e) {
 // expected
 }
 }
}