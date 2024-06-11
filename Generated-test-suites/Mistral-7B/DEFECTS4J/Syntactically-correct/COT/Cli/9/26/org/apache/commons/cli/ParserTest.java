import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--input", "input.txt", "-o", "output.txt", "-h"};
 private static final String[] ARGUMENTS_WITH_NON_OPTION = {"arg1", "arg2"};
 private static final String[] ARGUMENTS_EMPTY = {};

 @Test
 public void testParseWithValidOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help message");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS);

 assertEquals(3, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 assertFalse(commandLine.hasOption("help"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("r", "required", true, "required option");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS);

 fail("Expected MissingOptionException");
 }

 @Test
 public void testParseWithOptionalArg() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("d", "directory", true, "directory name");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS);

 assertEquals(3, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 assertNull(commandLine.getOptionValue("directory"));
 }

 @Test
 public void testParseWithOptionalArgValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("d", "directory", true, "directory name");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, new String[]{"--input", "input.txt", "--output", "output.txt", "--directory", "test"});

 assertEquals(0, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 assertEquals("test", commandLine.getOptionValue("directory"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS);

 fail("Expected MissingArgumentException");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnknownOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, new String[]{"--unknown"});

 fail("Expected UnrecognizedOptionException");
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS, null, true);

 assertEquals(2, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("input", "input.txt");
 properties.setProperty("output", "output.txt");
 CommandLine commandLine = parser.parse(options, null, properties, false);

 assertEquals(0, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 }

 @Test
 public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("input", "input.txt");
 properties.setProperty("output", "output.txt");
 CommandLine commandLine = parser.parse(options, ARGUMENTS, properties, true);

 assertEquals(1, commandLine.getArgs().size());
 assertEquals("input.txt", commandLine.getArgs().get(0));
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS_EMPTY);

 assertEquals(0, commandLine.getArgs().size());
 }

 @Test
 public void testParseWithNonOptionArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, ARGUMENTS_WITH_NON_OPTION, false);

 assertEquals(2, commandLine.getArgs().size());
 assertEquals("arg1", commandLine.getArgs().get(0));
 assertEquals("arg2", commandLine.getArgs().get(1));
 }
}