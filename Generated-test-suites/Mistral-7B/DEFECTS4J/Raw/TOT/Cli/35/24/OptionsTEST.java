import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "annotation", true, "display annotation information")
 .addOption("b", "boolean", false, "display boolean information")
 .addOption("c", "complex", false, "display complex information")
 .addOptionGroup(new OptionGroup("g", "group", "Options in this group are mutually exclusive"))
 .addOptions(new Option[] {
 new Option("x", "xflag", false, "display x information"),
 new Option("y", "yflag", false, "display y information")
 })
 .addOption("d", "delimiter", true, "set delimiter for output", ',')
 .addOption("e", "error", false, "display error information");
 }

 @Test
 public void testParseShortOptions() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("a -b c");
 assertTrue(parsed.hasOption("a"));
 assertFalse(parsed.hasOption("b"));
 assertTrue(parsed.hasOption("c"));
 }

 @Test
 public void testParseLongOptions() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("--annotation --boolean");
 assertTrue(parsed.hasOption("a"));
 assertFalse(parsed.hasOption("b"));
 }

 @Test
 public void testParseShortAndLongOptions() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("--annotation -b c --delimiter , -e");
 assertTrue(parsed.hasOption("a"));
 assertFalse(parsed.hasOption("b"));
 assertTrue(parsed.hasOption("c"));
 assertTrue(parsed.getOptionValue("d").equals(","));
 assertTrue(parsed.hasOption("e"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = createOptions();
 options.addOption("r", true, "required option");
 CommandLine parsed = options.parse("");
 fail("parse should have thrown ParseException for missing required option");
 }

 @Test
 public void testParseMissingOption() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("");
 assertFalse(parsed.hasOptions());
 }

 @Test
 public void testParseMultipleValues() throws ParseException {
 Options options = new Options()
 .addOption("h", "home", true, "home directory", "default")
 .addOption("p", "path", true, "file path", new String[2]);
 CommandLine parsed = options.parse("--home /usr --path /etc foo bar");
 assertEquals("/usr", parsed.getOptionValue("h"));
 String[] paths = parsed.getOptionValues("p");
 assertEquals(3, paths.length);
 assertEquals("/etc", paths[0]);
 assertEquals("foo", paths[1]);
 }

 @Test
 public void testParseGroup() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("--group g --xflag --yflag");
 OptionGroup group = options.getOptionGroup("g");
 assertTrue(parsed.hasOption("x"));
 assertTrue(parsed.hasOption("y"));
 assertFalse(parsed.hasOption("g"));
 assertFalse(parsed.hasOption("z"));
 }

 @Test(expected = ParseException.class)
 public void testParseMutuallyExclusiveOptions() throws ParseException {
 Options options = createOptions();
 CommandLine parsed = options.parse("--group g --xflag --yflag");
 OptionGroup group = options.getOptionGroup("g");
 Option xopt = group.getOptions().get(0);
 Option yopt = group.getOptions().get(1);
 assertTrue(parsed.isSet(xopt.getKey()));
 assertTrue(parsed.isSet(yopt.getKey()));
 fail("parse should have thrown ParseException for mutually exclusive options");
 }
}