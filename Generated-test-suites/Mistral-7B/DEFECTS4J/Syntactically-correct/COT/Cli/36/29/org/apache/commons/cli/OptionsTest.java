import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 options.addOption("a", "optionA", false, "This is option A");
 Option optA = options.getOption("a");
 Assert.assertNotNull(optA);
 Assert.assertEquals("optionA", optA.getLongOpt());
 Assert.assertFalse(optA.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 options.addOption("b", "optionB", true, "This is option B");
 Option optB = options.getOption("b");
 Assert.assertNotNull(optB);
 Assert.assertEquals("optionB", optB.getLongOpt());
 Assert.assertTrue(optB.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("mygroup", "My group");
 options.addOptionGroup(group);
 Option optGroup = options.getOption("mygroup");
 Assert.assertNotNull(optGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 options.addOption("a", "optionA", false, "This is option A");
 options.addOption("a", "optionA", false, "This is option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 options.addOption("a", "optionA", false, "This is option A");
 options.addOption("--optionA", "optionA", false, "This is option A");
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = options.getOptions();
 Assert.assertNotNull(optionsList);
 Assert.assertTrue(optionsList.isEmpty());
 }

 @Test
 public void testHasOption() {
 options.addOption("a", "optionA", false, "This is option A");
 Assert.assertTrue(options.hasOption("a"));
 Assert.assertTrue(options.hasOption("optionA"));
 }

 @Test
 public void testHasShortOption() {
 options.addOption("a", "optionA", false, "This is option A");
 Assert.assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testHasLongOption() {
 options.addOption("a", "optionA", false, "This is option A");
 Assert.assertTrue(options.hasLongOption("optionA"));
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption("option1", null, false, null);
 options.addOption("option2", null, false, null);
 options.addOption("option3", null, false, null);
 List<String> optionsList = options.getMatchingOptions("opt");
 Assert.assertEquals(2, optionsList.size());
 Assert.assertEquals("option1", optionsList.get(0));
 Assert.assertEquals("option3", optionsList.get(1));
 }

 @Test
 public void testParse() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--optionA"};
 CommandLine cmd = parser.parse(options, args);
 Option optA = options.getOption("optionA");
 Assert.assertTrue(cmd.hasOption("optionA"));
 Assert.assertEquals(optA.getValue(), cmd.getOptionValue("optionA"));
 }

}