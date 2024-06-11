import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 assertEquals("An alias option", options.getOption("a").getLongOpt());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 option.setLongOpt("long-alias");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("long-alias", options.getOption("a").getLongOpt());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "A group of options");
 Option option = new Option("g", "option-g", false, "An option in the group", group);
 options.addOptionGroup(group);
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "A group of options"));
 options.addOptionGroup(new OptionGroup("group2", "Another group of options"));
 List<Object> requiredOpts = options.getRequiredOptions();
 assertEquals(1, requiredOpts.size());
 assertEquals(option, requiredOpts.get(0));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("a", "option1", false, "Option 1");
 Option option2 = new Option("b", "option2", false, "Option 2");
 Option option3 = new Option("c", "option3", false, "Option 3");
 Option option4 = new Option("d", "option4", false, "Option 4");
 Option option5 = new Option("ab", "option5", false, "Option 5");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOption(option4);
 options.addOption(option5);
 List<String> matchingOpts = options.getMatchingOptions("a");
 assertEquals(2, matchingOpts.size());
 assertEquals(option1.getKey(), matchingOpts.get(0));
 assertEquals(option5.getKey(), matchingOpts.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option1 = new Option("a", "option1", false, "Option 1");
 Option option2 = new Option("b", "option2", false, "Option 2");
 Option option3 = new Option("c", "option3", false, "Option 3");
 Option option4 = new Option("d", "option4", false, "Option 4");
 Option option5 = new Option("e", "option5", false, "Option 5");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOption(option4);
 options.addOption(option5);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("option1"));
 assertTrue(options.hasOption("b"));
 assertFalse(options.hasOption("z"));
 }

 @Test
 public void testParse() {
 Options options = new Options();
 options.addOption("a", true, "An alias option");
 options.addOptionGroup(new OptionGroup("group", "A group of options") {{
 addOption("g", false, "An option in the group");
 addOption("h", true, "An option with a value in the group");
 }});
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("g"));
 assertNull(cmd.getOptionValue("g"));
 assertNull(cmd.getOptionValue("h"));
 } catch (ParseException e) {
 // empty
 }
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("a", "option1", false, "Option 1");
 Option option2 = new Option("b", "option2", false, "Option 2");
 Option option3 = new Option("c", "option3", false, "Option 3");
 Option option4 = new Option("d", "option4", false, "Option 4");
 Option option5 = new Option("e", "option5", false, "Option 5");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOption(option4);
 options.addOption(option5);
 OptionGroup group = new OptionGroup("group", "A group of options") {{
 addOption("g", false, "An option in the group");
 addOption("h", true, "An option with a value in the group");
 }};
 options.addOptionGroup(group);
 String expected = "[ Options: [ short {\"a\":Option{key='a', longOpt=null, hasArg=true, value=null, required=false, args=null, optionClass=class org.apache.commons.cli.Option, optParser={}, desc=\"An alias option\"}}] "
 + "[ long {group=OptionGroup{key='group', longOpt=null, hasArg=null, value=null, required=false, args=[], optionClass=class org.apache.commons.cli.OptionGroup, optParser={group=OptionGroup{key='group2', longOpt=null, hasArg=null, value=null, required=false, args=[], optionClass=class org.apache.commons.cli.OptionGroup}}}]";
 assertEquals(expected, options.toString());
 }
}