import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }
 };
 parser.setOptions(options);
 assertNotNull(parser.getOptions());
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }
 };
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 List<Option> requiredOptions = Arrays.asList(new Option("o", "opt", true, "option value"));
 options.addOptions(requiredOptions);
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }
 };
 parser.setOptions(options);
 List<Option> actual = parser.getRequiredOptions();
 assertEquals(1, actual.size());
 assertEquals("opt", actual.get(0).getKey());
 assertEquals(true, actual.get(0).isRequired());
 }

 @Test
 public void testParseNoArguments() throws ParseException {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 assertNotNull(cmd);
 assertFalse(cmd.hasOptions());
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseWithSimpleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", false, "option");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o"};
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseWithValueOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", true, "option value");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o", "value"};
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertEquals("value", cmd.getOptionValue("opt"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseWithMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o1", "opt1", false, "option1");
 options.addOption("o2", "opt2", false, "option2");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o1", "-o2"};
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt1"));
 assertTrue(cmd.hasOption("opt2"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseWithOneArg() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", false, "option");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o", "arg"};
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertFalse(cmd.hasArgs());
 assertNull(cmd.getArgs()[0]);
 }

 @Test
 public void testParseWithMultipleArgs() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", false, "option");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, args);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", false, "option");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[]{"-o", "arg1", "nonOption"};
 CommandLine cmd = parser.parse(options, args, true);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertEquals(1, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("o", "opt", false, "option");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 Properties props = new Properties();
 props.put("o", "arg");
 CommandLine cmd = parser.parse(options, new String[0], props);
 assertNotNull(cmd);
 assertTrue(cmd.hasOption("opt"));
 assertEquals("arg", cmd.getOptionValue("opt"));
 }

 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 Options options = new Options();
 List<Option> requiredOptions = Arrays.asList(new Option("r1", "req1", true, "required option 1"),
 new Option("r2", "req2", true, "required option 2"));
 options.addOptions(requiredOptions);
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException();
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException
 {
 return new CommandLine();
 }
 };
 parser.setOptions(options);
 String[] args = new String[0];
 assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
 }
}