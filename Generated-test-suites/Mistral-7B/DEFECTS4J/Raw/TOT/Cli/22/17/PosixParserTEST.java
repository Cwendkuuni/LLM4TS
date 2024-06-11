import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private static final String[] ARGS_VALID_OPTION = {"--option1", "-o1", "--option2=value2", "-o2", "--"};
 private static final String[] ARGS_MISSING_OPTION = {"--option1", "-o1", "--option3", "-o3"};
 private static final String[] ARGS_INVALID_OPTION = {"--option1", "-o1", "--invalidOption", "-o2"};
 private static final String[] ARGS_MULTI_OPTION = {"--option1", "--option1", "--option2=value2", "--option1"};
 private static final String[] ARGS_MIXED_OPTION_VALUE = {"--option1=value1", "--option1=value2"};
 private static final String[] ARGS_LONG_OPTION_VALUE = {"--longOptionValueLong", "--longOptionValueShort"};

 @Test
 public void testValidOption() throws ParseException {
 Options options = new Options();
 options.addOption("o1", true, "Option 1");
 options.addOption("o2", true, "Option 2");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_VALID_OPTION;
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(args.length, parsedArgs.length);
 for (int i = 0; i < parsedArgs.length; i++) {
 assertEquals(args[i], parsedArgs[i]);
 }

 assertEquals(2, options.getValues("o1").size());
 assertEquals("value2", options.getValues("o2").get(0));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("o1", true, "Option 1");
 options.addOption("o2", true, "Option 2");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_MISSING_OPTION;

 parser.parse(options, args, false);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("o1", true, "Option 1");
 options.addOption("o2", true, "Option 2");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_INVALID_OPTION;

 parser.parse(options, args, false);
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testMultipleOption() throws ParseException {
 Options options = new Options();
 options.addOption("o1", true, "Option 1");
 options.addOption("o2", true, "Option 2");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_MULTI_OPTION;
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(args.length, parsedArgs.length);
 for (int i = 0; i < parsedArgs.length; i++) {
 assertEquals(args[i], parsedArgs[i]);
 }

 assertEquals(2, options.getValues("o1").size());
 assertEquals(2, options.getValues("o2").size());
 }

 @Test
 public void testMixedOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("o1", true, "Option 1");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_MIXED_OPTION_VALUE;
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(args.length, parsedArgs.length);
 for (int i = 0; i < parsedArgs.length; i++) {
 assertEquals(args[i], parsedArgs[i]);
 }

 assertEquals(2, options.getValues("o1").size());
 assertEquals("value1", options.getValues("o1").get(0));
 assertEquals("value2", options.getValues("o1").get(1));
 }

 @Test
 public void testLongOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("lo", true, "Long Option");

 PosixParser parser = new PosixParser();
 String[] args = ARGS_LONG_OPTION_VALUE;
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(args.length, parsedArgs.length);
 for (int i = 0; i < parsedArgs.length; i++) {
 assertEquals(args[i], parsedArgs[i]);
 }

 assertEquals(2, options.getValues("lo").size());
 assertEquals("LongOptionValueLong", options.getValues("lo").get(0));
 assertEquals("LongOptionValueShort", options.getValues("lo").get(1));
 }
}