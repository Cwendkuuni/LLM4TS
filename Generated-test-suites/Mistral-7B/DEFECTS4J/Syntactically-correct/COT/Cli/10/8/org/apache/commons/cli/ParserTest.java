import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS = {"--option1", "--option2=value", "-o", "option3", "--", "arg1", "--requiredOption"};

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2");
 options.addOption("o", "opt3", true, "Option 3");
 options.addRequiredOption("requiredOption", false, "Required Option");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS, null, false);
 assertEquals(5, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(4));
 assertFalse(cmd.hasOption("unknownOption"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2");
 options.addRequiredOption("requiredOption", false, "Required Option");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(options, ARGS, null, false);
 fail();
 } catch (MissingOptionException e) {
 // Expected
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(options, ARGS, null, false);
 fail();
 } catch (UnrecognizedOptionException e) {
 // Expected
 }
 }

 @Test
 public void testEmptyArguments() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0], null, false);
 assertTrue(cmd.getArgs().isEmpty());
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS, null, true);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(1));
 }

}