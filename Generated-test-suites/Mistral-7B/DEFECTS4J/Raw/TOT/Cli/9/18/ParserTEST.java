import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private String[] testArgs = new String[]{"-a", "--optionA", "--optionB=value", "-b", "--", "-c", "argC"};

 @Test
 public void testParseWithNoOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, new String[0]);
 assertTrue(cmd.getArgs().isEmpty());
 }

 @Test
 public void testParseWithSimpleOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 assertNull(cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithValueOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("b", true, "Option B");
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("b"));
 assertEquals("value", cmd.getOptionValue("b"));
 }

 @Test
 public void testParseWithOptionalArgs() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option optionA = new Option("a", true, "Option A");
 optionA.setOptionalArg(true);
 options.addOption(optionA);
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("a"));
 assertNotNull(cmd.getOptionValue("a"));
 assertEquals(1, cmd.getArgs().size());
 assertEquals("argC", cmd.getArgs()[0]);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option optionA = new Option("a", true, "Option A");
 optionA.setRequired(true);
 options.addOption(optionA);
 CommandLine cmd = parser.parse(options, testArgs);
 fail("Expected MissingOptionException");
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingValueOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option optionA = new Option("a", true, "Option A");
 optionA.setRequired(false);
 options.addOption(optionA);
 CommandLine cmd = parser.parse(options, testArgs);
 fail("Expected MissingArgumentException for option: a");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, testArgs);
 fail("Expected UnrecognizedOptionException for option: c");
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option optionA = new Option("a", true, "Option A");
 optionA.setOptionalArg(true);
 Option optionB = new Option("b", true, "Option B");
 optionB.setArgName("value");
 options.addOption(optionA);
 options.addOption(optionB);
 CommandLine cmd = parser.parse(options, testArgs);
 fail("Expected MissingArgumentException for option: b");
 }

 @Test
 public void testParseWithHelpOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option helpOption = new Option("h", "help", false, "Print this help message.");
 options.addOption(helpOption);
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("help"));
 }

 @Test
 public void testParseWithLongOptions() throws ParseException { 
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option longOptionA = new Option("long-option-a", true, "Option long-option-a");
 options.addOption(longOptionA);
 Option longOptionB = new Option("long-option-b", true, "Option long-option-b");
 options.addOption(longOptionB);
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("long-option-a"));
 assertFalse(cmd.hasOption("optionA"));
 assertTrue(cmd.hasOption("long-option-b"));
 assertFalse(cmd.hasOption("optionB"));
 }

 @Test
 public void testParseWithOptionsInOptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup();
 group.setRequired(false);
 group.setName("MyGroup");
 Option optionA = new Option("a", true, "Option A");
 Option optionB = new Option("b", true, "Option B");
 group.addOption(optionA);
 group.addOption(optionB);
 options.addOption(group);
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test
 public void testParseWithAllowedMultipleValues() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option optionA = new Option("a", true, "Option A");
 optionA.setValuesMultiple(true);
 CommandLine cmd = parser.parse(options, testArgs);
 assertEquals(2, cmd.getValues(optionA).length);
 assertEquals("value", cmd.getValues(optionA)[1]);
 }

 @Test
 public void testParseWithNestedOptionGroups() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group1 = new OptionGroup();
 group1.setRequired(false);
 group1.setName("MyGroup1");
 Option optionA = new Option("a", true, "Option A");
 Option optionB = new Option("b", true, "Option B");
 group1.addOption(optionA);
 group1.addOption(optionB);
 OptionGroup group2 = new OptionGroup();
 group2.setRequired(false);
 group2.setName("MyGroup2");
 Option optionC = new Option("c", true, "Option C");
 Option optionD = new Option("d", true, "Option D");
 group2.addOption(optionC);
 group2.addOption(optionD);
 group1.addOptionGroup(group2);
 options.addOption(group1);
 CommandLine cmd = parser.parse(options, testArgs);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.hasOption("d"));
 }
}