import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 3, "Option C with an integer argument");
 }

 @After
 public void tearDown() {
 parser = null;
 }

 @Test
 public void testEmpty() {
 String[] args = {};
 try {
 String[] parsedArgs = parser.flatten(options, args, false);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 assertEquals(0, parsedArgs.length);
 }

 @Test
 public void testSingleArgument() {
 String[] args = {"foo"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("foo", parsedArgs[0]);
 }

 @Test
 public void testSingleOption() {
 String[] args = {"-a"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("-a", parsedArgs[0]);
 }

 @Test
 public void testSingleLongOption() {
 String[] args = {"--optionA"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 }

 @Test
 public void testSingleLongOptionWithValue() {
 String[] args = {"--optionA=value"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("value", parser.getCurrentOption().getValue());
 }

 @Test
 public void testMultipleArguments() {
 String[] args = {"arg1", "arg2"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(2, parsedArgs.length);
 assertEquals("arg1", parsedArgs[0]);
 assertEquals("arg2", parsedArgs[1]);
 }

 @Test
 public void testMultipleOptions() {
 String[] args = {"-a", "-b", "--optionC=3"};
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("-a", parsedArgs[0]);
 assertTrue(options.getOption("a").isSet());
 assertEquals("-b", parsedArgs[1]);
 assertTrue(options.getOption("b").isSet());
 assertEquals("--optionC", parsedArgs[2]);
 assertEquals(3, parser.getCurrentOption().getValue().intValue());
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 String[] args = {"-z"};
 String[] parsedArgs = parser.flatten(options, args, false);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testMissingArgument() {
 options.getOption("c").setRequired(true);
 String[] args = {"-c"};
 String[] parsedArgs = parser.flatten(options, args, false);
 fail("Expected ParseException");
 }

 @Test
 public void testStopAtNonOption() {
 options.getOption("b").setRequired(false);
 String[] args = {"-a", "--optionB", "-d", "arg"};
 String[] parsedArgs = parser.flatten(options, args, true);
 assertEquals(4, parsedArgs.length);
 assertEquals("-a", parsedArgs[0]);
 assertEquals("--optionB", parsedArgs[1]);
 assertEquals("-d", parsedArgs[2]);
 assertEquals("arg", parsedArgs[3]);
 }
}