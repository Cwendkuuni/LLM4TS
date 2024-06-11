import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createTestOptions() {
 return new Options()
 .addOption("a", "testOptionA")
 .addOption("b", "testOptionB", true)
 .addOptionGroup(new OptionGroup("group1")
 .addOption("c", "testOptionC")
 .addOption("d", "testOptionD", true)
 )
 .addOptionGroup(new OptionGroup("group2")
 .addOption("e", "testOptionE")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 }

 @Test
 public void testAddOptionWithDescription() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 Assert.assertEquals("testOptionA", optionA.getLongOpt());
 Assert.assertEquals("testOptionA", optionA.getDescription());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = createTestOptions();
 Option optionB = options.getOption("b");
 Assert.assertEquals(true, optionB.hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidArgument() {
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "invalid");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createTestOptions();
 OptionGroup group1 = options.getOptionGroup("group1");
 Option optionC = group1.getOption("c");
 Assert.assertNotNull(optionC);
 }

 @Test
 public void testAddRequiredOption() {
 Options options = createTestOptions();
 options.addOptionGroup(new OptionGroup("group1")
 .addOption("c", "testOptionC")
 .setRequired(true)
 );
 List<Option> optionsList = options.helpOptions();
 Option optionC = optionsList.get(0);
 Assert.assertTrue(optionC.isRequired());
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() {
 Options options = createTestOptions();
 options.addOptionGroup(new OptionGroup("group1")
 .addOption("c", "testOptionC")
 .setRequired(true)
 );
 String[] args = {"test"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.assertEquals("Missing required option 'c'", e.getMessage());
 }
 }

 @Test
 public void testParseWithValidOptions() {
 Options options = createTestOptions();
 String[] args = {"-a", "valueA", "-b", "valueB", "--group1", "-c", "valueC", "--group2", "-e", "valueE"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.fail("Test should not fail with given arguments");
 }
 }

}