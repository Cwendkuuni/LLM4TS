import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createTestOptions() {
 return new Options()
 .addOption("a", true, "Option A")
 .addOption("b", false, "Option B")
 .addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", false, "Option C in group1")
 .addOption("d", true, "Option D in group1")
 )
 .addOption("e", null, "Option E with argument")
 .addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("f", null, "Option F with argument in group2")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 Assert.assertEquals("Option A", optionA.getLongOpt());
 Assert.assertEquals(true, optionA.isRequired());
 Assert.assertEquals(null, optionA.getArgName());
 Assert.assertFalse(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createTestOptions();
 Option optionE = options.getOption("e");
 Assert.assertEquals("e", optionE.getLongOpt());
 Assert.assertEquals(null, optionE.getArgName());
 Assert.assertTrue(optionE.hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("a", true, "Option A (duplicate)");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createTestOptions();
 OptionGroup group1 = options.getOptionGroup("group1");
 Option optionC = group1.getOption("c");
 Option optionD = group1.getOption("d");
 Assert.assertEquals("group1", group1.getKey());
 Assert.assertFalse(group1.isRequired());
 Assert.assertEquals("Group 1", group1.getDescription());
 Assert.assertEquals("Option C in group1", optionC.getDescription());
 Assert.assertEquals(false, optionC.isRequired());
 Assert.assertFalse(optionC.hasArg());
 Assert.assertEquals("Option D in group1", optionD.getDescription());
 Assert.assertEquals(true, optionD.isRequired());
 Assert.assertEquals("", optionD.getArgName());
 Assert.assertFalse(optionD.hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionGroup() {
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group1", "Group 1"));
 options.addOptionGroup(new OptionGroup("group1", "Group 1 (duplicate)"));
 }

 @Test
 public void testGetOptions() {
 Options options = createTestOptions();
 List<Option> optionsList = options.getOptions();
 Option optionA = optionsList.get(0);
 Option optionB = optionsList.get(1);
 Option optionC = optionsList.get(2);
 Option optionD = optionsList.get(3);
 Option optionE = optionsList.get(4);
 Option optionF = optionsList.get(5);
 Assert.assertEquals(6, optionsList.size());
 Assert.assertEquals(optionA, optionA);
 Assert.assertEquals(optionB, optionB);
 Assert.assertEquals(optionC, optionC);
 Assert.assertEquals(optionD, optionD);
 Assert.assertEquals(optionE, optionE);
 Assert.assertEquals(optionF, optionF);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalid"};
 parser.parse(options, args);
 }

 @Test
 public void testParseWithValidOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "--b", "--c", "--d"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testParseWithGroupOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--group1", "--c", "--d"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testParseWithOptionWithArgument() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-e", "test"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testParseWithOptionGroupAndArgument() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--group2", "test"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }
}