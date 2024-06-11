import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private String[] testArgs = new String[]{"-a", "-b", "--optionC=valueC", "--optionD", "arg1", "arg2"};

 @Test
 public void testCreateCommandLine() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNotNull(commandLine);
 }

 @Test
 public void testHasOption() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", true, "Option C");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 TestUtils.assertFalse(commandLine, "hasOption", "a");
 TestUtils.assertTrue(commandLine, "hasOption", "b");
 TestUtils.assertFalse(commandLine, "hasOption", "z");
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", true, "Option C");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 Assert.assertNull(commandLine, "getOptionValue", "a");
 Assert.assertNull(commandLine, "getOptionValue", "b");
 TestUtils.assertEquals("valueC", commandLine, "getOptionValue", "c");
 }

 @Test
 public void testGetOptionObjects() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("c", true, "Option C", Integer.TYPE);
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 Assert.assertNull(commandLine, "getOptionObject", "a");
 TestUtils.assertEquals(new Integer(1), commandLine, "getOptionObject", "c");
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionObjectNullOption() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine, "getOptionObject", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionObjectInvalidOptionType() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "Option A", String[].class);
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 Assert.assertNull(commandLine, "getOptionObject", "a");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetOptionValuesInvalidOptionIndex() {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("c", true, "Option C");
 options.addOption("d", true, "Option D");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 String[] values = commandLine.getOptionValues("d");
 Assert.assertNotNull(values);
 Assert.assertEquals(2, values.length);
 Assert.assertEquals("arg1", values[0]);
 Assert.assertEquals("arg2", values[1]);

 TestUtils.assertNull(commandLine, "getOptionValues", "z");
 }

 @Test
 public void testGetArgs() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 String[] args = commandLine.getArgs();
 TestUtils.assertEquals(2, args.length);
 TestUtils.assertEquals("arg1", args[0]);
 TestUtils.assertEquals("arg2", args[1]);
 }

 @Test
 public void testGetArgList() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 List<String> argList = commandLine.getArgList();
 TestUtils.assertEquals(2, argList.size());
 TestUtils.assertEquals("arg1", argList.get(0));
 TestUtils.assertEquals("arg2", argList.get(1));
 }

 @Test
 public void testToString() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 String result = commandLine.toString();
 TestUtils.assertTrue(result.contains("CommandLine: [ options:"));
 TestUtils.assertTrue(result.contains("args: [arg1, arg2] ] ]"));
 }

 @Test
 public void testAddArg() {
 CommandLine commandLine = new CommandLine();
 commandLine.addArg("arg1");
 CommandLine commandLine2 = new CommandLine();
 commandLine2.addArg("arg2");
 commandLine.addAll(commandLine2.getArgList());

 List<String> args = commandLine.getArgList();
 TestUtils.assertEquals(2, args.size());
 TestUtils.assertEquals("arg1", args.get(0));
 TestUtils.assertEquals("arg2", args.get(1));
 }

 @Test
 public void testAddOption() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("optionX", true, "Option X");
 commandLine.addOption(option);

 List<Option> options = new ArrayList<>();
 options.add(option);
 TestUtils.assertEquals(options, commandLine.getOptions());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionInvalidOption() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("", true, "Invalid Option");
 commandLine.addOption(option);
 }

 @Test
 public void testIterator() throws ParseException {
 CommandLine commandLine = new CommandLine(testArgs);
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new PosixParser();
 parser.parse(options, testArgs);
 commandLine = parser.getParsedArgs();

 List<Option> expectedOptions = new ArrayList<>();
 expectedOptions.add(new Option("a", true, "Option A"));
 expectedOptions.add(new Option("b", false, "Option B"));
 expectedOptions.add(new Option("c", true, "Option C"));
 expectedOptions.add(new Option("d", true, "Option D"));

 Iterator<Option> iterator = commandLine.iterator();
 TestUtils.assertEquals(expectedOptions.size(), iterator.size());
 for (Option option : expectedOptions) {
 Option optionFromCommandLine = iterator.next();
 TestUtils.assertEquals(option, optionFromCommandLine);
 }
 }
}