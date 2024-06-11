import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 Options options = new Options();
 assertEquals(0, options.getOptions().size());
 assertEquals(0, options.getRequiredOptions().size());
 }

 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 Option option = new Option("a", "append", false, "append file");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("a"));
 assertFalse(options.hasOption("append"));
 assertFalse(options.hasOption("A"));
 }

 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 Option option = new Option("append", "a", false, "append file");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("append"));
 assertTrue(options.hasOption("a"));
 assertFalse(options.hasOption("append"));
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 Option option = new Option("a", "append", true, "append file <file>");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("a"));
 assertTrue(options.hasOption("a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionShort() {
 Options options = new Options();
 Option option = new Option("a", "append", false, "append file");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionLong() {
 Options options = new Options();
 Option option = new Option("append", "a", false, "append file");
 options.addOption(option);
 options.addOption(option);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 options.addOptionGroup(group);
 assertEquals(0, options.getOptions().size());
 assertEquals(1, options.getOptionGroups().size());
 assertEquals(group, options.getOptionGroup("group"));
 }

 @Test
 public void testAddOptionGroupWithOptions() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 Option option1 = new Option("i", "input", true, "input file");
 Option option2 = new Option("o", "output", true, "output file");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 assertEquals(2, options.getOptions().size());
 assertEquals(group, options.getOptionGroup("group"));
 assertEquals(option1, options.getOption("i"));
 assertEquals(option2, options.getOption("o"));
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("i", "input", true, "input file");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "group options") {{
 setRequired(true);
 }});
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("i"));
 assertTrue(options.getRequiredOptions().contains(option));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddRequiredOptionTwice() {
 Options options = new Options();
 Option option = new Option("i", "input", true, "input file");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "group options") {{
 setRequired(true);
 addOption(option);
 }});
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 Option option = new Option("i", "input", true, "input file");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--input", "input.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("input.txt", cmd.getOptionValue("i"));
 assertNull(cmd.getOptionValue("o"));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();Option option = new Option("i", "input", true, "input file");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "group options") {{
 setRequired(true);
 addOption(option);
 }});
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--group"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 assertEquals("The following options are required: [i]", e.getMessage());
 }
 }
}