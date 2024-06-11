import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

 @Test
 public void testSetOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test(expected = NullPointerException.class)
 public void testNullOptions() {
 Parser parser = new DefaultParser();
 parser.setOptions(null);
 }

 @Test
 public void testGetRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = parser.getRequiredOptions();
 assertNull(requiredOptions);

 parser.setOptions(options);
 requiredOptions = parser.getRequiredOptions();
 assertTrue(requiredOptions.isEmpty());

 options.addRequiredOption("option1");
 requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertTrue(requiredOptions.contains("option1"));
 }

 @Test
 public void testFlatten() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"--test", "-a", "-b", "-c", "arg1", "arg2"};
 List<String> tokenList = Arrays.asList(parser.flatten(options, arguments, false));

 assertEquals(6, tokenList.size());
 assertEquals("--test", tokenList.get(0));
 assertEquals("-a", tokenList.get(1));
 assertEquals("-b", tokenList.get(2));
 assertEquals("-c", tokenList.get(3));
 assertEquals("arg1", tokenList.get(4));
 assertEquals("arg2", tokenList.get(5));
 }

 @Test
 public void testParseWithNoArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().isEmpty());
 }

 @Test
 public void testParseWithArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"--test1", "-a", "-b", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("arg2", cmd.getArgs().get(1));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"--test"};
 parser.parse(options, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = Arrays.asList("option1", "option2");
 options.setRequiredOptions(requiredOptions);
 String[] arguments = {};
 parser.parse(options, arguments);
 }

 @Test
 public void testParseWithOptionalOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = Arrays.asList("option1");
 List optionalOptions = Arrays.asList(new Option("o", "option").optionalArg(true));
 options.addOptions(optionalOptions);
 options.setRequiredOptions(requiredOptions);
 String[] arguments = {"--test", "-o", "value"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getArgs().get(0));
 assertFalse(cmd.hasOption("option"));
 }

 @Test
 public void testParseWithOptionValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = Arrays.asList("option1");
 List optionalOptions = Arrays.asList(new Option("o", "option").optionalArg(true).hasArg(true));
 options.setRequiredOptions(requiredOptions);
 options.addOptions(optionalOptions);
 String[] arguments = {"--test", "-o", "value1", "value2"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("value2", cmd.getArgs().get(0));
 assertTrue(cmd.hasOption("option"));
 assertEquals("value1", cmd.getOptionValue("option"));
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group", true);
 group.addOption(new Option("a", "optionA", true, "A"));
 group.addOption(new Option("b", "optionB", true, "B"));
 options.addOption(group);
 List requiredOptions = Arrays.asList("--group");
 options.setRequiredOptions(requiredOptions);
 String[] arguments = {"--test", "--group", "-a"};
 CommandLine cmd = parser.parse(options, arguments);

 assertTrue(cmd.hasOptionGroup("group"));
 assertTrue(cmd.getOptionGroup("group").isSelected());
 assertEquals(1, cmd.getArgs().size());
 assertEquals("true", cmd.getOptionValue("group.optionA"));
 assertFalse(cmd.hasOption("group"));
 }

 @Test
 public void testParseWithInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"--invalid"};
 parser.parse(options, arguments);
 }
}