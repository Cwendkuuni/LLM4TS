import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 Options opts = new Options();

 opts.addOption("a", "alert", false, "Display alert message.");
 opts.addOption("b", "backup", true, "Backup file.");
 opts.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", "color", true, "Set color.")
 .addOption("d", "debug", false, "Debug mode."));
 opts.addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("e", "edit", true, "Edit file.")
 .addOption("f", "format", true, "Format file."));
 return opts;
 }

 @Test
 public void testAddOption() {
 Options opts = createOptions();
 assertEquals("alert", opts.getOption("a").getLongOpt());
 assertEquals(false, opts.getOption("a").isRequired());
 assertNull(opts.getOption("a").getValue());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options opts = createOptions();
 assertEquals("alert", opts.getOption("alert").getLongOpt());
 assertEquals(false, opts.getOption("alert").isRequired());
 assertNull(opts.getOption("alert").getValue());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options opts = createOptions();
 Option opt = opts.getOption("a");
 assertEquals("a", opt.getOpt());
 assertEquals("alert", opt.getLongOpt());
 assertEquals(false, opt.isRequired());
 assertNull(opt.getValue());
 }

 @Test
 public void testAddOptionWithValue() {
 Options opts = createOptions();
 opts.addOption("x", "example", true, "Example option with value.");
 Option opt = opts.getOption("x");
 assertEquals("x", opt.getOpt());
 assertEquals("example", opt.getLongOpt());
 assertNotNull(opt.getValue());
 }

 @Test
 public void testAddOptionGroup() {
 Options opts = createOptions();
 OptionGroup group1 = opts.getOptionGroup("group1");
 OptionGroup group2 = opts.getOptionGroup("group2");

 assertEquals("Group 1", group1.getName());
 assertEquals("group1", group1.getKey());
 assertEquals("Group 2", group2.getName());
 assertEquals("group2", group2.getKey());
 }

 @Test
 public void testHasOption() {
 Options opts = createOptions();
 assertTrue(opts.hasOption("a"));
 assertTrue(opts.hasOption("alert"));
 assertTrue(opts.hasOption("b"));
 assertTrue(opts.hasOption("backup"));
 assertFalse(opts.hasOption("z"));
 }

 @Test
 public void testHasLongOption() {
 Options opts = createOptions();
 assertTrue(opts.hasLongOption("alert"));
 assertFalse(opts.hasLongOption("z"));
 }

 @Test
 public void testHasShortOption() {
 Options opts = createOptions();
 assertTrue(opts.hasShortOption("a"));
 assertFalse(opts.hasShortOption("z"));
 }

 @Test
 public void testGetOption() {
 Options opts = createOptions();
 Option optionA = opts.getOption("a");
 Option optionB = opts.getOption("b");
 Option optionC = opts.getOption("c");
 Option optionD = opts.getOption("debug");
 Option optionE = opts.getOption("e");
 Option optionF = opts.getOption("format");
 Option optionG = opts.getOption("group1");
 Option optionH = opts.getOption("z");

 assertEquals("alert", optionA.getLongOpt());
 assertEquals(false, optionA.isRequired());
 assertNull(optionA.getValue());

 assertEquals("backup", optionB.getLongOpt());
 assertEquals(true, optionB.isRequired());
 assertNull(optionB.getValue());

 assertEquals("color", optionC.getLongOpt());
 assertEquals(true, optionC.isRequired());
 assertNull(optionC.getValue());

 assertEquals("debug", optionD.getLongOpt());
 assertEquals(false, optionD.isRequired());
 assertNull(optionD.getValue());

 assertNull(optionE);
 assertNull(optionF);
 assertNull(optionG);
 assertNull(optionH);
 }

 @Test
 public void testParseOptions() {
 Options opts = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"-a", "-b", "--color", "--debug"};
 try {
 CommandLine cmd = parser.parse(opts, args);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("alert"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("backup"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.hasOption("color"));
 assertTrue(cmd.hasOption("debug"));
 assertTrue(cmd.hasNoOption("z"));
 } catch (ParseException e) {
 fail("Failed to parse options: " + e.getMessage());
 }
}

 @Test(expected = ParseException.class)
 public void testParseOptionsWithInvalidArgument() {
 Options opts = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"-a", "value", "--debug"};
 try {
 CommandLine cmd = parser.parse(opts, args);
 fail("Should have thrown ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsWithMissingRequiredOption() {
 Options opts = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"--backup"};
 try {
 CommandLine cmd = parser.parse(opts, args);
 fail("Should have thrown ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsWithMixedLongAndShortOptions() {
 Options opts = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"-a", "--alert", "--backup", "-b", "--color", "--debug"};
 try {
 CommandLine cmd = parser.parse(opts, args);
 fail("Should have thrown ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsWithInvalidLongOption() {
 Options opts = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"--invalid"};
 try {
 CommandLine cmd = parser.parse(opts, args);
 fail("Should have thrown ParseException");
 } catch (ParseException e) {
 // expected
 }
 }
}