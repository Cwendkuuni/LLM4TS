import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 // given
 String[] arguments = new String[]{"-a", "--optionB", "valueB", "-b", "argB"};
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B").hasArg();

 Parser parser = new DefaultParser();
 // when
 CommandLine cmd = parser.parse(options, arguments);
 // then
 assertEquals(2, cmd.getArgs().size());
 assertEquals("valueB", cmd.getOptionValue("optionB"));
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertFalse(cmd.hasOption("nonexistentOption"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 // given
 String[] arguments = new String[]{"--unknownOption"};
 Options options = new Options();
 options.addOption("a", true, "Option A");

 Parser parser = new DefaultParser();
 // when
 parser.parse(options, arguments);
 // then
 fail("An UnrecognizedOptionException should have been thrown");
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingOption() throws ParseException {
 // given
 String[] arguments = new String[]{"--optionA"};
 Options options = new Options();
 options.addOption("a", true, "Option A").isRequired();

 Parser parser = new DefaultParser();
 // when
 parser.parse(options, arguments);
 // then
 fail("A MissingOptionException should have been thrown");
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 // given
 String[] arguments = new String[]{"-a"};
 Options options = new Options();
 options.addOption("a", true, "Option A").hasArg();

 Parser parser = new DefaultParser();
 // when
 parser.parse(options, arguments);
 // then
 fail("A MissingArgumentException should have been thrown");
 }
}