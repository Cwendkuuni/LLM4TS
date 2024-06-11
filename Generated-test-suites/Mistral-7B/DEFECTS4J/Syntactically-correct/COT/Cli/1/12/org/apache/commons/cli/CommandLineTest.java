import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private static final String TEST_CLASS_NAME = "CommandLineTest";

 @Test
 public void testCommandLineConstruction() {
 CommandLine commandLine = new CommandLine();
 // Verify the command line is empty after construction
 Assert.assertEquals(0, commandLine.args.size());
 Assert.assertEquals(0, commandLine.options.size());
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 // Set an option
 Option option = Option.builder("opt").build();
 commandLine.addOption(option);
 // Test hasOption
 Assert.assertTrue(commandLine.hasOption("opt"));
 Assert.assertTrue(commandLine.hasOption('o'));
 // Test hasOption false
 Assert.assertFalse(commandLine.hasOption("nonExistentOption"));
 Assert.assertFalse(commandLine.hasOption('x'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 // Set an option with no value
 Option optionNoValue = Option.builder("optNoValue").build();
 commandLine.addOption(optionNoValue);
 // Test getOptionValue with no value
 Assert.assertNull(commandLine.getOptionValue("optNoValue"));
 Assert.assertNull(commandLine.getOptionValue('o'));
 // Set an option with a value
 Option optionValue = Option.builder("optValue").build();
 commandLine.addOption(optionValue);
 commandLine.addArg("value");
 // Test getOptionValue
 Assert.assertEquals("value", commandLine.getOptionValue("optValue"));
 Assert.assertEquals("value", commandLine.getOptionValue('o'));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 // Set an option with multiple values
 Option optionValues = Option.builder("optValues")
 .longOpt("longOptValues")
 .hasArg(true)
 .valueSeparator('=')
 .build();
 commandLine.addOption(optionValues);
 commandLine.addArg("value1=value11");
 commandLine.addArg("value2=value22");
 // Test getOptionValues
 String[] optionValuesArray = commandLine.getOptionValues("optValues");
 Assert.assertArrayEquals(new String[]{"value11", "value22"}, optionValuesArray);
 String[] optionLongValuesArray = commandLine.getOptionValues("longOptValues");
 Assert.assertArrayEquals(new String[]{"value11", "value22"}, optionLongValuesArray);
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 // Set arguments
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 // Test getArgs
 String[] argsArray = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, argsArray);
 }

 @Test
 public void testToString() {
 CommandLine commandLine = new CommandLine();
 // Set options and arguments
 Option option = Option.builder("opt").build();
 commandLine.addOption(option);
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 // Test toString
 String expectedToString = "[ CommandLine: [ options: [Option{key='opt', args=[], optional=true, value=null, longOpt='', hasArg=false, argName=null, children=[], typeClass=class org.apache.commons.cli.BasicParser$OptionHandler}, OptionValues{values=[], metaValueSeparator=, option={}}] [ args: [arg1, arg2] ] ]";
 Assert.assertEquals(expectedToString, commandLine.toString());
 }

 @Test
 public void testParseException() {
 Options options = new Options();
 Option option = Option.builder("opt").required(true).build();
 options.addOption(option);
 CommandLine commandLine = new CommandLine();
 try {
 commandLine.parse(options, new String[]{"--nonExistentOption"});
 } catch (ParseException e) {
 // Test parse exception
 Assert.assertEquals("Missing required option 'opt'", e.getMessage());
 }
 }

}