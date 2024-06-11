import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("d", false, "Option D in group 1"));
 options.addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("e", true, "Option E in group 2")
 .addOption("f", 2, "Option F in group 2"));

 String[] arguments = {"--a", "argA", "--b", "1", "--c", "3", "--d", "--e", "--f", "argF"};
 CommandLine cmd = new Parser().parse(options, arguments);

 assertEquals("--a", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("a"));
 assertEquals(true, cmd.getBooleanOptionValue("a"));
 assertEquals("argA", cmd.getOptionValue("a", "defaultValueA"));

 assertEquals("--b", cmd.getOptionValue("b"));
 assertEquals(1, cmd.getCountOfOptions("b"));
 assertEquals(1, cmd.getValues("b").length);
 assertEquals("1", cmd.getOptionValue("b", "defaultValueB"));

 assertEquals("--c", cmd.getOptionValue("c"));
 assertEquals(3, cmd.getCountOfOptions("c"));
 assertEquals(3, cmd.getValues("c").length);
 assertEquals("3", cmd.getOptionValue("c", "defaultValueC"));

 assertFalse(cmd.hasOption("z"));
 assertNull(cmd.getOptionValue("z"));

 assertEquals("--d", cmd.getOptionValue("d"));
 assertTrue(cmd.hasOption("d"));
 assertFalse(cmd.getBooleanOptionValue("d"));

 assertEquals("--e", cmd.getOptionValue("e"));
 assertTrue(cmd.hasOption("e"));
 assertEquals(true, cmd.getBooleanOptionValue("e"));

 assertEquals("--f", cmd.getOptionValue("f"));
 assertEquals("argF", cmd.getOptionValue("f"));
 assertTrue(cmd.hasOption("f"));
 assertEquals(2, cmd.getCountOfOptions("f"));
 assertEquals(2, cmd.getValues("f").length);

 assertEquals("group1", cmd.getOptionGroup("group1").getName());
 assertEquals("--d", cmd.getOption("d").getKey());
 assertTrue(cmd.getOption("d").isSelected());

 assertEquals("group2", cmd.getOptionGroup("group2").getName());
 assertEquals("--e", cmd.getOption("e").getKey());
 assertTrue(cmd.getOption("e").getValue().length > 0);
 assertEquals("--f", cmd.getOption("f").getKey());
 assertEquals("argF", cmd.getOption("f").getValue()[0]);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 1, "Option D with argument");

 String[] arguments = {"--a", "--b", "--c", "--d"};
 new Parser().parse(options, arguments);
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", true, "Option D is required");

 String[] arguments = {"--a", "--b", "--c"};
 try {
 new Parser().parse(options, arguments);
 fail("Missing required option: d");
 } catch (MissingOptionException e) {
 assertEquals("Option d is required", e.getMessage());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");

 String[] arguments = {"--x", "--a", "--b", "--c"};
 try {
 new Parser().parse(options, arguments);
 fail("Unrecognized option: x");
 } catch (UnrecognizedOptionException e) {
 assertEquals("Unrecognized option: x", e.getMessage());
 }
 }
}