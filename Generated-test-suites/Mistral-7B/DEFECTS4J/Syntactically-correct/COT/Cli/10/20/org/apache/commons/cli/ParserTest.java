import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }
 };
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }
 };
 options.addOption("option1", true, "Description of option1");
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }
 };
 options.addOption("option1", true, "Description of option1");
 options.requiredOption("option1");
 parser.setOptions(options);
 List requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("option1", requiredOptions.get(0));
 }

 @Test
 public void testParseWithNoArguments() throws ParseException {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0][0]);
 assertNull(cmd.getArgs());
 assertNull(cmd.getOptions());
 }

 @Test
 public void testParseWithSingleArgument() throws ParseException {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[]{"arg1"};
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"arg1"}, new String[0][0]);
 assertEquals(1, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertNull(cmd.getOptions());
 }

 @Test
 public void testParseWithOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Description of option1");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0][0]);
 cmd.addOption("option1");
 assertEquals(1, cmd.getOptions().size());
 assertEquals("option1", cmd.getOptions().get(0).getLongOpt());
 assertEquals(true, cmd.getOptions().get(0).getValue());
 assertNull(cmd.getArgs());
 }

 @Test
 public void testParseWithOptionArgument() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Description of option1");
 options.addOption("option2", true, "Description of option2");
 options.addOption("option3", true, "Description of option3")
 .setValueSeparator('=')
 .hasArg(true);
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[]{"arg1=value1", "arg2"};
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"--option1", "--option2=value2", "arg2"});
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg2", cmd.getArgs()[0]);
 assertEquals("value2", cmd.getArgs()[1]);
 assertEquals(2, cmd.getOptions().size());
 assertEquals("option1", cmd.getOptions().get(0).getLongOpt());
 assertEquals(true, cmd.getOptions().get(0).getValue());
 assertEquals("option2", cmd.getOptions().get(1).getLongOpt());
 assertEquals("value2", cmd.getOptions().get(1).getValue());
 }

 @Test
 public void testParseWithMissingOption() {
 Options options = new Options();
 options.addOption("option1", true, "Description of option1");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0][0]));
 }

 @Test
 public void testParseWithUnrecognizedOption() {
 Options options = new Options();
 options.addOption("option1", true, "Description of option1");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return new String[0];
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"--unknownOption"}));
 }
}