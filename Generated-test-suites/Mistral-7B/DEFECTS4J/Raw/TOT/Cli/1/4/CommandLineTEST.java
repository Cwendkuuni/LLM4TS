import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 private CommandLine commandLine;
 private String[] args;
 private Options options;
 private CommandLineParser parser;

 @Before
 public void setUp() {
 args = new String[]{"-a", "1", "--optionB", "2", "--no-optionC", "3"};
 options = new Options();
 options.addOption("a", "option A", true, "an integer");
 options.addOption("b", "option B", true, "a string");
 options.addOption(null, "no-optionC", false, "no option C");
 parser = new DefaultParser();
 }

 @Test
 public void testHasOption() {
 commandLine = parser.parse(options, args);
 assertTrue(commandLine.hasOption("a"));
 assertTrue(commandLine.hasOption('a'));
 assertTrue(commandLine.hasOption("b"));
 assertTrue(commandLine.hasOption('b'));
 assertFalse(commandLine.hasOption("nonexistent"));
 assertFalse(commandLine.hasOption('z'));
 }

 @Test
 public void testGetOptionValue() {
 commandLine = parser.parse(options, args);
 assertEquals("1", commandLine.getOptionValue("a"));
 assertEquals("1", commandLine.getOptionValue('a'));
 assertEquals("2", commandLine.getOptionValue("b"));
 assertEquals("2", commandLine.getOptionValue('b'));
 assertNull(commandLine.getOptionValue("nonexistent"));
 assertNull(commandLine.getOptionValue('z'));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionValueNullOptions() {
 commandLine = new CommandLine();
 assertNull(commandLine.getOptionValue("a"));
 }

 @Test
 public void testGetOptionValues() {
 commandLine = parser.parse(options, args);
 String[] values = commandLine.getOptionValues("a");
 assertEquals(1, values.length);
 assertEquals("1", values[0]);
 values = commandLine.getOptionValues("b");
 assertEquals(1, values.length);
 assertEquals("2", values[0]);
 }

 @Test
 public void testGetOptionValuesEmpty() {
 commandLine = parser.parse(options, new String[0]);
 assertNull(commandLine.getOptionValues("a"));
 }

 @Test
 public void testGetOptionValueDefaultValue() {
 commandLine = parser.parse(options, new String[0]);
 assertEquals("", commandLine.getOptionValue("a", "default"));
 }

 @Test
 public void testGetArgs() {
 commandLine = parser.parse(options, args);
 String[] argsArray = commandLine.getArgs();
 assertEquals(3, argsArray.length);
 assertEquals("3", argsArray[0]);
 }

 @Test
 public void testGetArgList() {
 commandLine = parser.parse(options, args);
 List argList = commandLine.getArgList();
 assertEquals(3, argList.size());
 assertEquals("3", argList.get(0));
 }
}