import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Before
 public void setUp() {
 Options options = new Options();
 options.addBooleanOption("booleanOpt", false);
 options.addOption("stringOpt", true, "s");
 options.addOptionGroup(new OptionGroup("optGroup", "Options in a group")
 .addOption(new Option("gOption", false)).addOption(new Option("gOption2", false)));
 }

 @Test
 public void testHasOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--booleanOpt", "--stringOpt", "arg"});
 assertTrue(commandLine.hasOption("booleanOpt"));
 assertTrue(commandLine.hasOption("stringOpt"));
 assertFalse(commandLine.hasOption("nonExistingOption"));
 }

 @Test
 public void testHasOptionWithShortArgument() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-s", "arg"});
 assertTrue(commandLine.hasOption("stringOpt"));
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--booleanOpt", "--stringOpt=value"});
 assertTrue(commandLine.hasOption("booleanOpt"));
 assertTrue(commandLine.getOptionValue("booleanOpt").equals("true"));
 assertTrue(commandLine.hasOption("stringOpt"));
 assertTrue(commandLine.getOptionValue("stringOpt").equals("value"));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValueForNonExistingOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--nonExistingOption"});
 assertFalse(commandLine.hasOption("nonExistingOption"));
 assertNull(commandLine.getOptionValue("nonExistingOption"));
 }

 @Test
 public void testGetOptionValues() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--stringOpt=value1,value2"});
 assertTrue(commandLine.hasOption("stringOpt"));
 String[] values = commandLine.getOptionValues("stringOpt");
 assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test
 public void testGetArgs() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--booleanOpt", "--stringOpt=value", "arg1", "arg2"});
 String[] args = commandLine.getArgs();
 assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testGetArgList() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--booleanOpt", "--stringOpt=value", "arg1", "arg2"});
 List argList = commandLine.getArgList();
 assertTrue(argList.containsAll(Arrays.asList(Arrays.asList("arg1", "arg2"))));
 }
}