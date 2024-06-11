import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Options options = new Options();
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public CommandLine parse(Options options, String[] arguments) throws ParseException {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties)
 throws ParseException {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
 throws ParseException {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
 throws ParseException {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 protected void processProperties(Properties properties) {
 // To be implemented
 }

 @Override
 protected void checkRequiredOptions() throws MissingOptionException {
 // To be implemented
 }

 @Override
 public void processArgs(Option opt, ListIterator iter) throws ParseException {
 // To be implemented
 }

 @Override
 protected void processOption(String arg, ListIterator iter) throws ParseException {
 // To be implemented
 }
 };
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("o", "opt", true, "an option");
 options.addOption(null, "no-arg", false, "no argument option");

 parser.setOptions(options);
 assertNotNull(parser.getOptions());
 assertEquals(3, parser.getOptions().size());
 assertEquals("arg1", parser.getOptions().getOption("a").getLongOpt());
 assertEquals("an argument", parser.getOptions().getOption("a").getDescription());
 assertEquals(true, parser.getOptions().getOption("a").hasArg());
 assertEquals("opt", parser.getOptions().getOption("o").getLongOpt());
 assertEquals("an option", parser.getOptions().getOption("o").getDescription());
 assertEquals(true, parser.getOptions().getOption("o").hasArg());
 assertFalse(parser.getOptions().getOption("no-arg").hasArg());
 }

 @Test
 public void testGetOptions() {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 assertNull(parser.getOptions());
 parser.setOptions(new Options());
 assertEquals(0, parser.getOptions().size());
 parser.setOptions(new Options().addOption("a", "arg1", true, "an argument"));
 assertEquals(1, parser.getOptions().size());
 }

 @Test(expected = NullPointerException.class)
 public void testNullOptions() {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 parser.setOptions(null);
 }

 @Test
 public void testGetRequiredOptions() {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("r", "required", true, "a required option");
 parser.setOptions(options);
 List requiredOptions = parser.getRequiredOptions();
 assertEquals(0, requiredOptions.size());
 options.addOption("r", "required", true, "a required option").isRequired();
 requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertTrue(requiredOptions.get(0) instanceof Option);
 assertEquals("required", ((Option) requiredOptions.get(0)).getLongOpt());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFlatten() {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"arg1", "--a", "arg2"};
 parser.flatten(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParse() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "arg2"};
 parser.parse(options, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseWithProperties() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 Properties properties = new Properties();
 properties.setProperty("a", "arg3");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a", "arg2"};
 parser.parse(options, arguments, properties);
 }

 @Test(expected = ParseException.class)
 public void testParseWithNonOption() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"nonOption"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--missingOption"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingArgument() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParseWithOptionalArgument() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a"};
 try {
 parser.parse(options, arguments, false);
 } catch (ParseException e) {
 Options parsedOptions = parser.getOptions();
 assertEquals(1, parsedOptions.size());
 assertTrue(parsedOptions.getOption("a").getValues().length > 0);
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithDoubleDash() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--", "arg1"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParseWithUnrecognizedOption() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("a", "arg1", true, "an argument");
 parser.setOptions(options);
 String[] arguments = new String[]{"--unrecognizedOption"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testCheckRequiredOptions() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("r", "required", true, "a required option");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a"};
 parser.parse(options, arguments, false);
 parser.checkRequiredOptions();
 }

 @Test(expected = MissingOptionException.class)
 public void testCheckRequiredOptionsWithMissingOption() throws ParseException {
 Parser parser = new Parser() {
 //Methods as in testSetOptions()
 };
 Options options = new Options().addOption("r", "required", true, "a required option");
 parser.setOptions(options);
 String[] arguments = new String[]{"--a"};
 parser.parse(options, arguments, false);
 parser.checkRequiredOptions();
 parser.getOptions().removeOption("r");
 parser.checkRequiredOptions();
 }
}