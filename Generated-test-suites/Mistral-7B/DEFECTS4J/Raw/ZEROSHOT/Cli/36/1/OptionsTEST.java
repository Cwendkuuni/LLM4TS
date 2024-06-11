import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Assert;

public class OptionsTest {

 @org.junit.Before
 public void setUp() {
 Options options = new Options();
 }

 @org.junit.Test
 public void testAddOption() {
 // Test adding a single short option
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 // Test getting the option
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("optionA", optionA.getLongOpt());
 Assert.assertEquals(false, optionA.hasArg());
 Assert.assertEquals("Description for option A", optionA.getDescription());

 // Test adding a single long option
 options = new Options();
 options.addOption("b", "optionB", false, "Description for option B");
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionB);
 Assert.assertEquals("optionB", optionB.getKey());
 Assert.assertEquals(false, optionB.hasArg());
 Assert.assertEquals("Description for option B", optionB.getDescription());

 // Test adding a short option with an argument
 options = new Options();
 options.addOption("c", "optionC", true, "Description for option C");
 Option optionC = options.getOption("c");
 Assert.assertNotNull(optionC);
 Assert.assertEquals("optionC", optionC.getLongOpt());
 Assert.assertEquals(true, optionC.hasArg());
 Assert.assertEquals("Description for option C", optionC.getDescription());
 }

 @org.junit.Test
 public void testAddOptionGroup() {
 // Test adding an option group
 Options options = new Options();
 OptionGroup group = new OptionGroup("mygroup", "My Group Options");
 options.addOptionGroup(group);
 group.addOption(new Option("g", "groupOption", false, "Group option description"));
 Option groupOption = options.getOption("g");
 Assert.assertNotNull(groupOption);
 Assert.assertEquals("groupOption", groupOption.getLongOpt());
 Assert.assertEquals(false, groupOption.hasArg());
 Assert.assertEquals("Group option description", groupOption.getDescription());
 Assert.assertEquals("My Group Options", group.getLongOpt());

 // Test parsing with the option group
 CommandLineParser parser = new BasicParser();
 String[] args = {"-g"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Assert.assertTrue(cmd.hasOption("g"));
 } catch (ParseException e) {
 e.printStackTrace();
 Assert.fail("Parsing should have succeeded");
 }
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 // Test adding a duplicate option
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("a", "optionA2", false, "Description for option A2");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 // Test adding a duplicate long option
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("--optionA", "optionA2", false, "Description for option A2");
 }

 @org.junit.Test
 public void testGetMatchingOptions() {
 // Test getting matching options
 Options options = new Options();
 options.addOption("option1", "opt1", false, "Option 1");
 options.addOption("option2", "opt2", false, "Option 2");
 options.addOption("option3", "opt3", false, "Option 3");
 options.addOption("option4", "opt4", false, "Option 4");
 options.addOption("option5", "opt5", false, "Option 5");

 List<String> matchingOptions = options.getMatchingOptions("opt");
 Assert.assertEquals(3, matchingOptions.size());
 Assert.assertTrue(matchingOptions.contains("option1"));
 Assert.assertTrue(matchingOptions.contains("option2"));
 Assert.assertTrue(matchingOptions.contains("option3"));
 }

 @org.junit.Test
 public void testHasOption() {
 // Test hasOption
 Options options = new Options();
 options.addOption("option1", "opt1", false, "Option 1");
 options.addOption("option2", "opt2", false, "Option 2");
 options.addOption("option3", "opt3", false, "Option 3");
 options.addOption("option4", "opt4", false, "Option 4");
 options.addOption("option5", "opt5", false, "Option 5");

 Assert.assertTrue(options.hasOption("opt1"));
 Assert.assertTrue(options.hasOption("opt2"));
 Assert.assertTrue(options.hasOption("opt3"));
 Assert.assertTrue(options.hasOption("opt4"));
 Assert.assertTrue(options.hasOption("opt5"));
 Assert.assertFalse(options.hasOption("nonOption"));
 }

 @org.junit.Test
 public void testToString() {
 // Test toString
 Options options = new Options();
 options.addOption("option1", "opt1", false, "Option 1");
 options.addOption("option2", "opt2", false, "Option 2");
 options.addOption("option3", "opt3", false, "Option 3");
 options.addOption("option4", "opt4", false, "Option 4");
 options.addOption("option5", "opt5", false, "Option 5");

 Assert.assertEquals("[ Options: [ short {a=false, option1=Option{key='option1', longOpt='opt1', hasArg=false, description='Option 1'}},\n" +
 " option2=Option{key='option2', longOpt='opt2', hasArg=false, description='Option 2'}, option3=Option{key='option3', longOpt='opt3', hasArg=false, description='Option 3'}, option4=Option{key='option4', longOpt='opt4', hasArg=false, description='Option 4'}, option5=Option{key='option5', longOpt='opt5', hasArg=false, description='Option 5'} ] [ long {} ]]", options.toString());
 }
}