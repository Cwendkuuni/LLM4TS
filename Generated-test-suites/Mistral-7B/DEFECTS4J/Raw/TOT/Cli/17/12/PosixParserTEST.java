import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 3, "Option C with an integer argument");
 }

 @Test
 public void testSimpleOption() {
 String[] args = {"-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a"}, parsedArgs);
 }

 @Test
 public void testOptionWithValue() {
 String[] args = {"-a", "value"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "value"}, parsedArgs);
 }

 @Test
 public void testSingleDash() {
 String[] args = {"-"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"--"}, parsedArgs);
 }

 @Test
 public void testMultipleDashes() {
 String[] args = {"--"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"--"}, parsedArgs);
 }

 @Test
 public void testOptionWithArgument() {
 String[] args = {"-a", "arg"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "arg"}, parsedArgs);
 }

 @Test
 public void testBurstOption() {
 String[] args = {"-abc"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-c"}, parsedArgs);
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() {
 String[] args = {"-x"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 fail("Unknown option: -x");
 }

 @Test
 public void testStopAtNonOption() {
 options.addOption("stop", false, "Stop flattening at this option");
 String[] args = {"-a", "-stop", "-b"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, true);
 assertEquals(new String[]{"-a"}, parsedArgs);
 }
}