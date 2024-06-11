import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGUMENTS = new String[] {"--option1", "-o1", "--option2=value2", "-o2", "arg1", "arg2"};
 private static final String[] ARGUMENTS_WITH_NON_OPTION = new String[] {"nonOption", "--option1", "-o1"};
 private static final String[] ARGUMENTS_WITH_MISSING_OPTION = new String[] {"--option1"};
 private static final String[] ARGUMENTS_WITH_MISSING_VALUE = new String[] {"--option1"};
 private static final String[] ARGUMENTS_WITH_INVALID_VALUE = new String[] {"--option1=invalidValue"};

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS);
 assertNotNull(cmd);
 assertEquals(6, cmd.getArgs().size());
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("value2", cmd.getOptionValue("option2"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS_WITH_MISSING_OPTION);
 fail("A ParseException should be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingValues() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS_WITH_MISSING_VALUE);
 fail("A ParseException should be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS_WITH_INVALID_VALUE);
 fail("A ParseException should be thrown");
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS, true);
 assertNotNull(cmd);
 assertEquals(4, cmd.getArgs().size());
 }

 @Test(expected = ParseException.class)
 public void testParseWithNonOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd;

 parser.setOptions(options);
 cmd = parser.parse(options, ARGUMENTS_WITH_NON_OPTION);
 fail("A ParseException should be thrown");
 }
}