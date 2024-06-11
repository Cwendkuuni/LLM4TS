import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private static final String TEST_CLASS_NAME = PosixParserTest.class.getName();

 @Test
 public void testFlattenSimpleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with boolean value");
 options.addOption("b", 1, "Option B with integer value");
 options.addOption("c", 2, "Option C with integer value and argument");

 String[] args = new String[]{"-a", "--b", "1", "-c", "5"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(6, parsedArgs.length);
 assertEquals("-a", parsedArgs[0]);
 assertEquals("--b", parsedArgs[1]);
 assertEquals("1", parsedArgs[2]);
 assertEquals("-c", parsedArgs[3]);
 assertEquals("5", parsedArgs[4]);
 assertEquals("--", parsedArgs[5]);
 }

 @Test
 public void testFlattenLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("longOptionA", true, "Long Option A with boolean value");
 options.addOption("longOptionB", 1, "Long Option B with integer value");
 options.addOption("longOptionC", 2, "Long Option C with integer value and argument");

 String[] args = new String[]{"--longOptionA", "--longOptionB=3", "--longOptionC=7"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(6, parsedArgs.length);
 assertEquals("--longOptionA", parsedArgs[0]);
 assertEquals("--longOptionB", parsedArgs[1]);
 assertEquals("3", parsedArgs[2]);
 assertEquals("--longOptionC", parsedArgs[3]);
 assertEquals("7", parsedArgs[4]);
 assertEquals("--", parsedArgs[5]);
 }

 @Test
 public void testFlattenNoOptions() throws ParseException {
 Options options = new Options();

 String[] args = new String[]{"foo", "bar", "baz"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);

 assertEquals(4, parsedArgs.length);
 assertEquals("foo", parsedArgs[0]);
 assertEquals("bar", parsedArgs[1]);
 assertEquals("baz", parsedArgs[2]);
 assertEquals("--", parsedArgs[3]);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with boolean value");
 options.addOption("b", 1, "Option B with integer value");

 String[] args = new String[]{"-x", "-c=5"}; // Option X is not defined
 PosixParser parser = new PosixParser();

 try {
 parser.parse(options, args, false);
 fail();
 } catch (ParseException e) {
 assertEquals("Unrecognized option: -x", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenNullOptions() {
 Options options = null;
 String[] args = new String[]{"-a", "--b=2", "-c"};

 try {
 new PosixParser().parse(options, args, false);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGobble() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with boolean value");
 options.addOption("b", 1, "Option B with integer value");
 options.addOption("c", 2, "Option C with integer value and argument");

 String[] args = new String[]{"-a", "--b", "1", "-c", "5", "foo", "bar"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, true);

 assertEquals(6, parsedArgs.length);
 assertEquals("-a", parsedArgs[0]);
 assertEquals("--b", parsedArgs[1]);
 assertEquals("1", parsedArgs[2]);
 assertEquals("-c", parsedArgs[3]);
 assertEquals("5", parsedArgs[4]);
 assertEquals("--", parsedArgs[5]);
 assertEquals("foo", parsedArgs[6]);
 assertEquals("bar", parsedArgs[7]);
 }
}