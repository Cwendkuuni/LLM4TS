import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", 1, "Option C");
 options.addOption("d", 2, "Option D");
 options.addOptionGroup(new OptionGroup("group", "Group"));
 options.getOptionGroup("group").addOption("e", true, "Option E");
 options.getOptionGroup("group").addOption("f", 3, "Option F");
 }

 @Test
 public void testParse() throws ParseException {
 String[] arguments = new String[]{"-a", "-b", "-c", "1", "-d", "2", "--group", "-e", "--group", "-f"};
 parser = new DefaultParser();
 CommandLine result = parser.parse(options, arguments);

 assertEquals(arguments.length, result.getArgs().size());
 assertTrue(result.hasOption("a"));
 assertTrue(result.hasOption("b"));
 assertTrue(result.hasOption("c"));
 assertTrue(result.hasOption("d"));
 assertTrue(result.hasOption("e"));
 assertTrue(result.hasOption("f"));
 assertFalse(result.hasOption("x"));
 assertEquals(1, result.getOptionValues("c").length);
 assertEquals(2, result.getOptionValues("d").length);
 assertEquals(3, result.getOptionValues("f")[0].intValue());
 assertNull(result.getOptionValues("x"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 String[] arguments = new String[]{"-c"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 String[] arguments = new String[]{"-b"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 String[] arguments = new String[]{"-x"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseException() throws ParseException {
 String[] arguments = new String[]{"--", "-a", "-b", "-c", "--", "-x"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }
}