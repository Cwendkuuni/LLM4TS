import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParser() throws ParseException {
 Parser parser = new DefaultParser();

 // Test parsing with no options
 Options options = new Options();
 String[] args = new String[0];
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().size());
 assertFalse(cmd.hasOptions());

 // Test parsing with options
 options.addOption("a", true, "Option A");
 args = new String[]{"--a"};
 cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.getOptionValue("a"));

 // Test parsing with required options
 options.addRequiredOption("b", true, "Option B");
 args = new String[0];
 try {
 parser.parse(options, args);
 fail("Expected MissingOptionException");
 } catch (MissingOptionException e) {
 // Expected
 }
 args = new String[]{"--b"};
 cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.getOptionValue("b"));

 // Test parsing with properties
 options.addOption("c", true, "Option C");
 Properties properties = new Properties();
 properties.setProperty("c", "value");
 cmd = parser.parse(options, null, properties);
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.getOptionValue("c").equals("value"));

 // Test parsing with arguments
 options.addOption("d", true, "Option D");
 args = new String[]{"--d", "arg"};
 cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("d"));
 assertTrue(cmd.getOptionValue("d").equals("arg"));
 assertTrue(cmd.getArgs().size() == 0);

 // Test parsing with multiple arguments
 args = new String[]{"--d", "arg1", "arg2"};
 cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("d"));
 assertTrue(cmd.getOptionValues("d").length == 2);
 assertTrue(cmd.getArgs().size() == 0);

 // Test parsing with stopAtNonOption
 options.addOption("e", true, "Option E");
 args = new String[]{"--e", "--f", "-f", "arg"};
 cmd = parser.parse(options, args, true);
 assertTrue(cmd.getArgs().size() == 1);
 assertEquals("arg", cmd.getArgs().get(0));

 // Test parsing with help
 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("test", options);
 }
}