import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOptions() {
 Options options = new Options();

 // test adding a single short option
 options.addOption("a", "alias", false, "An alias option");
 Option optA = options.getOption("a");
 assertTrue(optA.isSettable());
 assertFalse(optA.isRequired());
 assertEquals("An alias option", optA.getDescription());

 // test adding a single long option
 options.addOption("b", "big", false, "A big option");
 Option optB = options.getOption("b");
 assertTrue(optB.isSettable());
 assertFalse(optB.isRequired());
 assertEquals("A big option", optB.getDescription());

 // test adding an option with short and long names
 options.addOption("c", "char", false, "A character option");
 Option optC = options.getOption("c");
 assertTrue(optC.isSettable());
 assertFalse(optC.isRequired());
 assertEquals("A character option", optC.getDescription());
 assertTrue(optC.hasLongOpt());
 assertEquals("char", optC.getLongOpt());

 // test adding a required option
 options.addOption("d", "data", true, "A data option");
 Option optD = options.getOption("d");
 assertTrue(optD.isSettable());
 assertTrue(optD.isRequired());
 assertEquals("A data option", optD.getDescription());

 // test adding an option group
 OptionGroup group = new OptionGroup("G", "Group");
 group.addOption(new Option("e", "echo", false, "Outputs the command line arguments."));
 options.addOptionGroup(group);
 Option optE = options.getOption("e");
 assertTrue(optE.isSettable());
 assertFalse(optE.isRequired());
 assertEquals("Outputs the command line arguments.", optE.getDescription());
 assertTrue(optE.isEnabled());

 // test parsing with no arguments
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toCommandLineParser());
 assertNull(cmd.getOptionValue("a"));
 assertNull(cmd.getOptionValue("b"));
 assertNull(cmd.getOptionValue("c"));
 assertNull(cmd.getOptionValue("d"));
 assertNull(cmd.getOptionValue("e"));

 // test parsing with all options set
 cmd = parser.parse(options.toCommandLineParser(), new String[]{"-a", "-b", "--big", "-c", "--char", "--data", "--group", "-e"});
 assertNotNull(cmd.getOptionValue("a"));
 assertNotNull(cmd.getOptionValue("b"));
 assertNotNull(cmd.getOptionValue("c"));
 assertNotNull(cmd.getOptionValue("d"));
 assertNotNull(cmd.getOptionValue("e"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();

 // test matching a short option
 options.addOption("a", "alias", false, "An alias option");
 options.addOption("b", "big", false, "A big option");
 options.addOption("c", "char", false, "A character option");
 options.addOption("d", "data", true, "A data option");

 List<String> matches = options.getMatchingOptions("a");
 assertEquals(1, matches.size());
 assertEquals("a", matches.get(0));

 List<String> matches2 = options.getMatchingOptions("l");
 assertEquals(1, matches2.size());
 assertEquals("long", matches2.get(0));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();

 // test hasShortOption
 options.addOption("a", "alias", false, "An alias option");
 options.addOption("b", "big", false, "A big option");
 options.addOption("c", "char", false, "A character option");
 options.addOption("d", "data", true, "A data option");

 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("b"));
 assertTrue(options.hasShortOption("c"));
 assertTrue(options.hasShortOption("d"));
 assertFalse(options.hasShortOption("e"));

 // test hasLongOption
 options.addOption("e", "echo", false, "Outputs the command line arguments.");

 assertTrue(options.hasLongOption("big"));
 assertTrue(options.hasLongOption("char"));
 assertTrue(options.hasLongOption("data"));
 assertTrue(options.hasLongOption("echo"));
 assertFalse(options.hasLongOption("unknown"));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();

 // test getting an option by short name
 options.addOption("a", "alias", false, "An alias option");
 Option optA = options.getOption("a");
 assertEquals("An alias option", optA.getDescription());

 // test getting an option by long name
 options.addOption("b", "big", false, "A big option");
 Option optB = options.getOption("b");
 assertEquals("A big option", optB.getDescription());

 // test getting an option that doesn't exist
 Option optNone = options.getOption("none");
 assertNull(optNone);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();

 // test adding a duplicate short option
 options.addOption("a", "alias", false, "An alias option");
 options.addOption("a", "alias2", false, "Another alias option");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = new Options();

 // test adding a duplicate long option
 options.addOption("b", "big", false, "A big option");
 options.addOption("b", "big2", false, "Another big option");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddRequiedOptionToGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("G", "Group");
 Option optD = new Option("d", "data", true, "A data option");

 options.addOptionGroup(group);
 group.addOption(optD);
 }
}