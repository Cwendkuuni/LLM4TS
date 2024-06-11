import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertThrows;

 public class ParserTest {

 private Options createTestOptions() {
 Options options = new Options();
 Option option1 = new Option("o", "option1", true, "Option 1");
 Option option2 = new Option("p", "option2", true, "Option 2");
 Option option3 = new Option("r", "requiredOption", true, "Required Option");
 Option groupOption = new Option("g", "groupOption", false, "Group Option");
 Option optionGroup = new Option("g", "group", Option.BUILD_DEPENDENCIES, "Group", true);
 optionGroup.addOption(groupOption);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOptionGroup(optionGroup);
 return options;
 }

 @Test
 public void testSetAndGetOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = createTestOptions();
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = createTestOptions();
 parser.setOptions(options);
 List requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("requiredOption", requiredOptions.get(0));
 }

 @Test
 public void testFlatten_EmptyArguments() throws ParseException {
 Parser parser = new DefaultParser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }
 };
 String[] arguments = new String[0];
 parser.parse(createTestOptions(), arguments);
 assertNull(parser.cmd.getArgs());
 }

 @Test
 public void testParse_EmptyArguments() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 parser.parse(createTestOptions(), arguments);
 assertNull(parser.cmd.getArgs());
 }

 @Test
 public void testParse_SingleOption() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-o", "optionValue1"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(1, parser.cmd.getOptions().size());
 assertEquals("optionValue1", parser.cmd.getOptionValue("option1"));
 }

 @Test
 public void testParse_SingleRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-r"};
 assertThrows(MissingOptionException.class, () -> parser.parse(createTestOptions(), arguments));
 }

 @Test
 public void testParse_SingleRequiredOptionWithValue() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-r", "requiredValue"};
 parser.parse(createTestOptions(), arguments);
 assertEquals("requiredValue", parser.cmd.getOptionValue("requiredOption"));
 }

 @Test
 public void testParse_MultipleOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-o", "optionValue1", "--option2", "optionValue2", "-r", "requiredValue"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(3, parser.cmd.getOptions().size());
 assertEquals("optionValue1", parser.cmd.getOptionValue("option1"));
 assertEquals("optionValue2", parser.cmd.getOptionValue("option2"));
 assertEquals("requiredValue", parser.cmd.getOptionValue("requiredOption"));
 }

 @Test
 public void testParse_SingleOptionWithValueAndArguments() throws ParseException {
 Parser parser = new DefaultParser() {
 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return super.parse(options, arguments, properties, false);
 }
 };
 String[] arguments = {"-o", "optionValue1", "arg1", "arg2"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(1, parser.cmd.getOptions().size());
 assertEquals("optionValue1", parser.cmd.getOptionValue("option1"));
 assertEquals(2, parser.cmd.getArgs().size());
 assertEquals("arg1", parser.cmd.getArgs()[0]);
 assertEquals("arg2", parser.cmd.getArgs()[1]);
 }

 @Test
 public void testParse_MultipleOptionsWithArguments() throws ParseException {
 Parser parser = new DefaultParser() {
 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return super.parse(options, arguments, properties, false);
 }
 };
 String[] arguments = {"-o", "optionValue1", "arg1", "-o", "optionValue2", "arg2", "-r", "requiredValue"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(4, parser.cmd.getOptions().size());
 assertEquals("optionValue1", parser.cmd.getOptionValue("option1"));
 assertEquals("arg1", parser.cmd.getArgs()[0]);
 assertEquals("optionValue2", parser.cmd.getOptionValue("option2"));
 assertEquals("arg2", parser.cmd.getArgs()[1]);
 assertEquals("requiredValue", parser.cmd.getOptionValue("requiredOption"));
 }

 @Test
 public void testParse_OptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"--group", "groupValue"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(1, parser.cmd.getOptions().size());
 assertEquals("groupValue", parser.cmd.getOptionValue("groupOption"));
 assertEquals(0, parser.cmd.getArgs().length);
 }

 @Test
 public void testParse_OptionGroupWithOtherOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-o", "optionValue1", "--group", "groupValue", "-r", "requiredValue"};
 parser.parse(createTestOptions(), arguments);
 assertEquals(3, parser.cmd.getOptions().size());
 assertEquals("optionValue1", parser.cmd.getOptionValue("option1"));
 assertEquals("groupValue", parser.cmd.getOptionValue("groupOption"));
 assertEquals("requiredValue", parser.cmd.getOptionValue("requiredOption"));
 }

 @Test
 public void testParse_MissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-x", "valueX"};
 assertThrows(UnrecognizedOptionException.class, () -> parser.parse(createTestOptions(), arguments));
 }

 @Test
 public void testParse_MissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = {"-o", "optionValue1"};
 assertThrows(MissingArgumentException.class, () -> parser.parse(createTestOptions(), arguments));
 }
 }