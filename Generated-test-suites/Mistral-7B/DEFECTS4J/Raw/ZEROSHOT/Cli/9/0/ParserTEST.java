import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParser() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 options.addOptionGroup(new OptionGroup("cg", "Container group")
 .addOption(new Option("c", "char", false, "Character option"))
 .addOption(new Option("g", "group", true, "Group option")));

 Parser parser = new DefaultParser();
 String[] args = {"-a", "--alpha", "--cg", "-g", "test", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, args, null);

 assertEquals("Alpha option", cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("b"));
 assertSame(options.getOptionGroup("cg"), cmd.getOptionGroup("cg"));
 assertSame(options.findOption("c"), cmd.getOption("c"));
 assertSame(options.findOption("g"), cmd.getOption("g"));
 assertEquals("test", cmd.getOptionValue("g"));
 assertEquals("test arg1 arg2", cmd.getArgs().toString());
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", "alpha", true, "Alpha option");
 Parser parser = new DefaultParser();
 String[] args = new String[0];
 parser.parse(options, args, null);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 Parser parser = new DefaultParser();
 String[] args = {"--x", "--a"};
 parser.parse(options, args, null);
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 Parser parser = new DefaultParser();
 String[] args = {"-a"};
 parser.parse(options, args, null);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 Parser parser = new DefaultParser();
 String[] args = {"-a", "badValue"};
 parser.parse(options, args, null);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("p", "prop", true, "Property option");
 Parser parser = new DefaultParser();
 Properties props = new Properties();
 props.setProperty("p", "value");
 String[] args = new String[0];
 CommandLine cmd = parser.parse(options, args, props);
 assertEquals("value", cmd.getOptionValue("p"));
 }

 @Test
 public void testParseWithMultipleOptionsAndArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 Parser parser = new DefaultParser();
 String[] args = {"-a", "--alpha", "--beta", "--cg", "-g", "test", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, args, null);
 assertEquals("Alpha option", cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("b"));
 assertSame(options.getOptionGroup("cg"), cmd.getOptionGroup("cg"));
 assertSame(options.findOption("c"), cmd.getOption("c"));
 assertSame(options.findOption("g"), cmd.getOption("g"));
 assertEquals("test", cmd.getOptionValue("g"));
 assertEquals("arg1 arg2", cmd.getArgs().toString());
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 Parser parser = new DefaultParser();
 String[] args = {"--", "-a", "--beta", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, args, null, true);
 assertNull(cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("b"));
 assertEquals("arg1 arg2", cmd.getArgs().toString());
 }
}