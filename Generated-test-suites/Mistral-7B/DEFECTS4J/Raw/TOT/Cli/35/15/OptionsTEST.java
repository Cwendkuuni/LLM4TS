import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 assertEquals("Activate option", options.getOption("a").getLongOpt());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("b", "baseDir,", true, "Base directory for input files");
 options.addOption(option);
 assertEquals("baseDir", options.getOption("b").getOpt());
 assertEquals("Base directory for input files", options.getOption("b").getDescription());
 assertEquals(true, options.getOption("b").hasArg());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("c", "configFile", true, "Configuration file");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group1", "Group 1", new Option[] { option }));
 options.getOptionGroup("group1").setRequired(true);

 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();

 String[] args = {"--configFile", "test.cfg"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 assertEquals("test.cfg", cmd.getOptionValue("configFile"));
 } catch (ParseException e) {
 //Expected
 }

 args = new String[] {"--configFile"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 assertNull(cmd.getOptionValue("configFile"));
 fail("Missing argument exception not thrown");
 } catch (ParseException e) {
 assertEquals("Missing required option 'configFile'.", e.getMessage());
 }
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("group1", "Group 1");
 Option option1 = new Option("g1_a", "group1.option1", false, "Option 1 in group 1");
 Option option2 = new Option("g1_b", "group1.option2", false, "Option 2 in group 1");
 group1.addOption(option1);
 group1.addOption(option2);
 options.addOptionGroup(group1);

 OptionGroup group2 = new OptionGroup("group2", "Group 2");
 Option option3 = new Option("g2_a", "group2.option3", false, "Option 3 in group 2");
 group2.addOption(option3);
 options.addOptionGroup(group2);

 assertEquals(2, options.getOptionGroups().size());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 assertEquals(option, options.getOption("a"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullOption() {
 Options options = new Options();
 assertNull(options.getOption(null));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("longOption1", "long option 1");
 Option option2 = new Option("longOption2", "long option 2");
 options.addOption(option1);
 options.addOption(option2);

 List<String> matchingOptions = options.getMatchingOptions("longOption");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("longOption1"));
 assertTrue(matchingOptions.contains("longOption2"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("active"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 assertTrue(options.hasLongOption("active"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testNoSuchOption() {
 Options options = new Options();
 assertFalse(options.hasOption("noOption"));
 }

 @Test
 public void testHelpFormatter() {
 Options options = new Options();
 Option option = new Option("a", "active", false, "Activate option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();

 String[] args = {"--help"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 fail("Help should have been printed");
 } catch (ParseException e) {
 //Expected
 }

 String helpText = formatter.printHelp("MyApp", options);
 assertTrue(helpText.contains("Activate option"));
 }
}