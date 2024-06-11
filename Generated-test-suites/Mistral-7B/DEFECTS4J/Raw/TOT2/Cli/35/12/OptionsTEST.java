import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();
 options.addOption("a", "anOption", false, "An example option");
 options.addOption("b", "anOptionB", true, "An optional option B with a value");
 options.addOptionGroup(new OptionGroup("group", "A group", true, "A test group") {{
 this.setRequired(true);
 addOption("g", "aGroupOption", false, "An option in the group");
 addOption("h", "anotherGroupOption", false, "Another option in the group");
 }});
 return options;
 }

 @Test
 public void testAddOptionShort() {
 Options options = createTestOptions();
 assertTrue(options.hasShortOption("a"));
}

 @Test
 public void testAddOptionLong() {
 Options options = createTestOptions();
 assertTrue(options.hasLongOption("anOption"));
}

 @Test
 public void testAddOptionWithArg() {
 Options options = createTestOptions();
 assertTrue(options.hasOption("b"));
}

 @Test
 public void testAddOptionGroup() {
 Options options = createTestOptions();
 OptionGroup group = options.getOptionGroup("group");
 assertTrue(group.isRequired());
}

 @Test
 public void testHasOptionWithArg() throws ParseException {
 Options options = createTestOptions();
 CommandLine parsed = options.parse(new String[]{"-b", "value"});
 assertTrue(parsed.hasOption("b"));
 assertEquals("value", parsed.getOptionValue("b"));
}

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = createTestOptions();
 options.parse(new String[]{"-z"});
 fail("Expected ParseException with message: Unrecognized option: z");
}

 @Test
 public void testParseWithMissingArgument() throws ParseException {
 Options options = createTestOptions();
 try {
 options.parse(new String[]{"-a"});
 fail("Expected ParseException with message: The option '-a' requires an argument.");
 } catch (ParseException e) {
 // expected
 }
}

 @Test
 public void testParseWithExtraArguments() throws ParseException {
 Options options = createTestOptions();
 try {
 options.parse(new String[]{"-a", "value1", "-b", "value2"});
 fail("Expected ParseException with message: Unrecognized option: value1");
 } catch (ParseException e) {
 // expected
 }
}
}