import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws Exception {
 // Given
 String[] args = new String[]{"-a", "1", "--optionB", "valueB", "--optionC"};
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", "valueB", true, "Option B");
 options.addOption("c", null, false, "Option C");
 Parser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, args, null);

 // Then
 assertEquals(args.length, cmd.getArgs().size());
 assertEquals("1", cmd.getOptionValue("a"));
 assertNull(cmd.getOptionValue("b"));
 assertFalse(cmd.hasOption("c"));
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertFalse(cmd.hasOption("d"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws Exception {
 // Given
 String[] args = new String[]{"--optionA"};
 Options options = new Options();
 options.addOption("a", true, "Option A", true);
 Parser parser = new DefaultParser();

 // When & Then
 parser.parse(options, args, null);
 fail("Expected ParseException but no exception was thrown");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws Exception {
 // Given
 String[] args = new String[]{"--unknownOption"};
 Options options = new Options();
 Parser parser = new DefaultParser();

 // When & Then
 parser.parse(options, args, null);
 }
}

class DefaultParser extends Parser {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
 return arguments;
 }
}